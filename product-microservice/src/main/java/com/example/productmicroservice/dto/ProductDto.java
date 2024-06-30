package com.example.productmicroservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

}
