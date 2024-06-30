package com.example.ordermicroservice.service;

import com.example.ordermicroservice.dto.OrderDto;
import com.example.ordermicroservice.dto.ProductDto;
import com.example.ordermicroservice.dto.create.CreateOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Flux<OrderDto> findAll();

    Flux<OrderDto> findByUserId(Long userId);

    Mono<OrderDto> findById(String id);

    Mono<OrderDto> save(CreateOrderDto createOrderDto);

    Mono<OrderDto> update(String id, CreateOrderDto createOrderDto);

    Mono<String> delete(String id);
}
