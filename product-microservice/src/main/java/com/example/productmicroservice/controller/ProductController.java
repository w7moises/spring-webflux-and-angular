package com.example.productmicroservice.controller;

import com.example.productmicroservice.dto.ProductDto;
import com.example.productmicroservice.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Product Api", description = "Api for testing the endpoint for products")
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;


    @GetMapping
    public ResponseEntity<Flux<ProductDto>> getAllProducts() {
        Flux<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/restar-stock/{id}")
    public Mono<ResponseEntity<ProductDto>> restarStock(@PathVariable Long id) {
        return productService.subtractStockProduct(id).map(ResponseEntity::ok);
    }

    @PutMapping("/sumar-stock/{id}")
    public Mono<ResponseEntity<ProductDto>> sumarStock(@PathVariable Long id) {
        return productService.addStockProduct(id).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable Long id) {
        return productService.getProductById(id).map(ResponseEntity::ok);
    }

}
