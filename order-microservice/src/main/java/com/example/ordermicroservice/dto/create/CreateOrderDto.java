package com.example.ordermicroservice.dto.create;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderDto {

    private Long userId;

    private List<Long> productIds;

    private BigDecimal total;
}
