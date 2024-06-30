package com.example.productmicroservice.service.impl;

import com.example.productmicroservice.dto.ProductDto;
import com.example.productmicroservice.entity.Product;
import com.example.productmicroservice.exception.ErrorFoundException;
import com.example.productmicroservice.exception.ProductNotFoundException;
import com.example.productmicroservice.repository.ProductRepository;
import com.example.productmicroservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Flux<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .map(this::convertToDto)
                .onErrorMap(e -> new ErrorFoundException("Error fetching products: " + e));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDto)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)));
    }

    @Override
    public Mono<ProductDto> subtractStockProduct(Long id) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    product.setQuantity(product.getQuantity() - 1);
                    return productRepository.save(product);
                })
                .map(this::convertToDto)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)));
    }

    @Override
    public Mono<ProductDto> addStockProduct(Long id) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    product.setQuantity(product.getQuantity() + 1);
                    return productRepository.save(product);
                })
                .map(this::convertToDto)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)));
    }

    private ProductDto convertToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
