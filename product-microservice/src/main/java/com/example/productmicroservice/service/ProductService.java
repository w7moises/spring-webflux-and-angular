package com.example.productmicroservice.service;

import com.example.productmicroservice.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<ProductDto> getAllProducts();

    Mono<ProductDto> getProductById(Long id);

    Mono<ProductDto> subtractStockProduct(Long id);

    Mono<ProductDto> addStockProduct(Long id);
}
