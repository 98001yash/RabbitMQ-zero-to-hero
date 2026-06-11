package com.rabbitmq.inventory_service.consumer;

import com.rabbitmq.inventory_service.config.RabbitMQConfig;
import com.rabbitmq.inventory_service.dto.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    @RabbitListener(
            queues = RabbitMQConfig.ORDER_QUEUE
    )
    public void consumeOrderCreatedEvent(
            OrderCreatedEvent event
    ) {

        System.out.println();
        System.out.println("ORDER RECEIVED");
        System.out.println("Order Id : " + event.getOrderId());
        System.out.println("Product  : " + event.getProductName());
        System.out.println("Quantity : " + event.getQuantity());
        System.out.println("Amount   : " + event.getAmount());
        System.out.println();
        System.out.println("Inventory Reserved Successfully");
        System.out.println();
    }
}