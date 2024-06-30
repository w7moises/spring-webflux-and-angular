package com.example.ordermicroservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDto {

    private String id;

    private Long userId;

    private List<Long> productIds;

    private BigDecimal total;

}
