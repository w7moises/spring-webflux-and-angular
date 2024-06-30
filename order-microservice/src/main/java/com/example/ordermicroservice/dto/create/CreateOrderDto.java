package com.example.ordermicroservice.dto.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderDto {

    @NotNull(message = "user is required")
    private Long userId;

    @NotEmpty(message = "product is required")
    private List<Long> productIds;

    @NotNull(message = "total is required")
    private BigDecimal total;

}
