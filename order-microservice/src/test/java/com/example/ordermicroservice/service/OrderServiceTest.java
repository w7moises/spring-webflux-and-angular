package com.example.ordermicroservice.service;

import com.example.ordermicroservice.dto.OrderDto;
import com.example.ordermicroservice.dto.create.CreateOrderDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "20000")
public class OrderServiceTest {
    @Autowired
    private WebTestClient webClient;
    @MockBean
    private OrderService orderService;
    private static CreateOrderDto createOrderDto = new CreateOrderDto();
    private static OrderDto orderDto = new OrderDto();
    private static final String id = "1";
    private final static Long userId = 1L;
    private final static List<Long> productIds = List.of(1L, 2L, 3L);
    private final static BigDecimal total = BigDecimal.valueOf(300.0);

    @BeforeAll
    public static void setUp() {
        createOrderDto.setUserId(userId);
        createOrderDto.setProductIds(productIds);
        createOrderDto.setTotal(total);
        orderDto.setId(id);
        orderDto.setUserId(userId);
        orderDto.setProductIds(productIds);
        orderDto.setTotal(total);
    }

    @Test
    @DisplayName("Test Get All Orders")
    public void testGetAllOrders() {
        when(orderService.findAll()).thenReturn(Flux.just(orderDto));
        webClient.get().uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderDto.class)
                .isEqualTo(List.of(orderDto));
    }

    @Test
    @DisplayName("Test Get Order By User Id")
    public void testGetOrderByUserId() {
        when(orderService.findByUserId(userId)).thenReturn(Flux.just(orderDto));
        webClient.get().uri("/orders/user/{userId}", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderDto.class)
                .isEqualTo(List.of(orderDto));
    }

    @Test
    @DisplayName("Test Create Order")
    public void testCreateOrder() {
        when(orderService.save(createOrderDto)).thenReturn(Mono.just(orderDto));
        webClient.post().uri("/orders")
                .bodyValue(createOrderDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDto.class)
                .isEqualTo(orderDto);
    }

    @Test
    @DisplayName("Test Update Order")
    public void testUpdateOrder() {
        when(orderService.update(id, createOrderDto)).thenReturn(Mono.just(orderDto));
        webClient.put().uri("/orders/{id}", id)
                .bodyValue(createOrderDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDto.class)
                .isEqualTo(orderDto);
    }

    @Test
    @DisplayName("Test Delete Order")
    public void testDeleteOrder() {
        when(orderService.delete(id)).thenReturn(Mono.just("Order deleted successfully"));
        webClient.delete().uri("/orders/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Order deleted successfully");
    }
}
