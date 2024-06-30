package com.example.ordermicroservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "order")
@Data
public class Order {
    @Id
    private String id;
    private Long userId;
    private List<Long> productIds;
    private BigDecimal total;
}
