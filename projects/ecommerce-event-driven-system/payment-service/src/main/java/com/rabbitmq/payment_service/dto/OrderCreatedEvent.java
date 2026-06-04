package com.rabbitmq.payment_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String productName;
    private Integer quantity;
    private Double amount;
}
