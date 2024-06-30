package com.example.ordermicroservice.controller;

import com.example.ordermicroservice.dto.OrderDto;
import com.example.ordermicroservice.dto.ProductDto;
import com.example.ordermicroservice.dto.create.CreateOrderDto;
import com.example.ordermicroservice.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Product Api", description = "Api for testing the endpoint for orders")
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Flux<OrderDto>> getAllOrders() {
        Flux<OrderDto> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Flux<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        Flux<OrderDto> orders = orderService.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrderDto>> getOrderById(@PathVariable String id) {
        return orderService.findById(id).map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<OrderDto>> saveOrder(@RequestBody @Valid CreateOrderDto createOrderDto) {
        return orderService.save(createOrderDto).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrderDto>> updateOrder(@PathVariable String id, @RequestBody @Valid CreateOrderDto createOrderDto) {
        return orderService.update(id, createOrderDto).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteOrder(@PathVariable String id) {
        return orderService.delete(id).map(ResponseEntity::ok);
    }
}
