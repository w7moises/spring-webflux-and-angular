package com.example.ordermicroservice.service.impl;

import com.example.ordermicroservice.dto.OrderDto;
import com.example.ordermicroservice.dto.ProductDto;
import com.example.ordermicroservice.dto.create.CreateOrderDto;
import com.example.ordermicroservice.entity.Order;
import com.example.ordermicroservice.exception.ErrorFoundException;
import com.example.ordermicroservice.exception.OrderNotFoundException;
import com.example.ordermicroservice.repository.OrderRepository;
import com.example.ordermicroservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final WebClient webClientProduct;

    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, WebClient.Builder webClientProduct, ModelMapper modelMapper, @Value("${product.url}") String url) {
        this.orderRepository = orderRepository;
        this.webClientProduct = webClientProduct.baseUrl(url).build();
        this.modelMapper = modelMapper;
    }

    private Mono<ProductDto> findProductId(Long id) {
        return webClientProduct.get().uri("/products/" + id).
                retrieve().bodyToMono(ProductDto.class);
    }

    private Mono<ProductDto> subtractStockProduct(Long id) {
        return webClientProduct.put().uri("/products/restar-stock/" + id)
                .retrieve().bodyToMono(ProductDto.class);
    }

    private Mono<ProductDto> addStockProduct(Long id) {
        return webClientProduct.put().uri("/products/sumar-stock/" + id)
                .retrieve().bodyToMono(ProductDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OrderDto> findAll() {
        return orderRepository.findAll()
                .map(this::convertToDto)
                .onErrorMap(e -> new ErrorFoundException("Error fetching orders: " + e));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OrderDto> findByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .map(this::convertToDto)
                .onErrorMap(e -> new OrderNotFoundException("Order not found with userId: " + userId));
    }

    @Override
    public Mono<OrderDto> findById(String id) {
        return orderRepository.findById(id)
                .map(this::convertToDto)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Order not found with id: " + id)));
    }

    @Override
    @Transactional
    public Mono<OrderDto> save(CreateOrderDto createOrderDto) {
        Order order = modelMapper.map(createOrderDto, Order.class);

        Flux<ProductDto> productDetails = Flux.fromIterable(createOrderDto.getProductIds())
                .flatMap(productId -> findProductId(productId)
                        .flatMap(productDto -> {
                            if (productDto.getQuantity() > 0) {
                                return Mono.just(productDto);
                            } else {
                                return Mono.error(new ErrorFoundException("Product with ID " + productId + " has insufficient quantity"));
                            }
                        }));

        return productDetails
                .flatMap(productDto -> subtractStockProduct(productDto.getId()))
                .collectList()
                .flatMap(updatedProducts -> orderRepository.save(order))
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public Mono<OrderDto> update(String orderId, CreateOrderDto createOrderDto) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Order not found with id: " + orderId)))
                .flatMap(existingOrder -> {
                    List<Long> existingProductIds = existingOrder.getProductIds();
                    List<Long> newProductIds = createOrderDto.getProductIds();

                    List<Long> addedProductIds = newProductIds.stream()
                            .filter(productId -> !existingProductIds.contains(productId))
                            .collect(Collectors.toList());

                    List<Long> removedProductIds = existingProductIds.stream()
                            .filter(productId -> !newProductIds.contains(productId))
                            .collect(Collectors.toList());

                    Mono<Void> handleStockUpdates = Flux.fromIterable(addedProductIds)
                            .flatMap(this::subtractStockProduct)
                            .thenMany(Flux.fromIterable(removedProductIds))
                            .flatMap(this::addStockProduct)
                            .then();

                    existingOrder.setProductIds(newProductIds);
                    existingOrder.setTotal(createOrderDto.getTotal());

                    return handleStockUpdates
                            .then(orderRepository.save(existingOrder))
                            .map(this::convertToDto);
                });
    }

    @Override
    @Transactional
    public Mono<String> delete(String id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Order not found with id: " + id)))
                .flatMap(existingOrder -> {
                    List<Long> productIds = existingOrder.getProductIds();
                    return Flux.fromIterable(productIds)
                            .flatMap(this::addStockProduct)
                            .then(orderRepository.delete(existingOrder))
                            .then(Mono.just("Order deleted successfully"));
                });
    }

    private OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
