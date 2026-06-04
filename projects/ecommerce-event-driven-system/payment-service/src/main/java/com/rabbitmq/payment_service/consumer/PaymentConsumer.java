package com.rabbitmq.payment_service.consumer;

import com.rabbitmq.payment_service.config.RabbitMQConfig;
import com.rabbitmq.payment_service.dto.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    @RabbitListener(
            queues = RabbitMQConfig.PAYMENT_QUEUE
    )
    public void consumeOrderCreatedEvent(
            OrderCreatedEvent event
    ) {

        System.out.println();
        System.out.println("================================");
        System.out.println("PAYMENT RECEIVED");
        System.out.println("Order Id : " + event.getOrderId());
        System.out.println("Product  : " + event.getProductName());
        System.out.println("Amount   : " + event.getAmount());
        System.out.println();
        System.out.println("Payment Processed Successfully");
        System.out.println("================================");
        System.out.println();
    }
}