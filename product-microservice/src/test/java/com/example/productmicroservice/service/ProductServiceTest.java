package com.example.productmicroservice.service;

import com.example.productmicroservice.dto.ProductDto;
import com.example.productmicroservice.entity.Product;
import com.example.productmicroservice.exception.ProductNotFoundException;
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
public class ProductServiceTest {
    @Autowired
    private WebTestClient webClient;
    @MockBean
    private ProductService productService;
    private static ProductDto product = new ProductDto();
    private final static Long id = 1L;
    private final static String name = "Product";
    private final static String description = "Product Description";
    private final static BigDecimal price = BigDecimal.valueOf(100.0);
    private final static Integer quantity = 10;

    @BeforeAll
    public static void setUp() {
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
    }

    @Test
    @DisplayName("Test Get Product By Id")
    public void testGetProductById() {
        when(productService.getProductById(id)).thenReturn(Mono.just(product));
        webClient.get().uri("/products/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDto.class)
                .isEqualTo(product);
    }

    @Test
    @DisplayName("Test Get Product By Id Not Found")
    public void testGetProductByIdNotFound() {
        when(productService.getProductById(id)).thenReturn(Mono.error(new ProductNotFoundException("Product not found with id: " + id)));
        webClient.get().uri("/products/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Test Get All Products")
    public void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(Flux.just(product));
        webClient.get().uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDto.class)
                .isEqualTo(List.of(product));
    }
}
