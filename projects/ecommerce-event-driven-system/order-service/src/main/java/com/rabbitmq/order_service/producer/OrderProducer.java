package com.rabbitmq.order_service.producer;

import com.rabbitmq.order_service.config.RabbitMQConfig;
import com.rabbitmq.order_service.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderCreatedEvent(
            OrderCreatedEvent event
    ) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                event
        );

        System.out.println();
        System.out.println("ORDER CREATED EVENT PUBLISHED");
        System.out.println(event);
        System.out.println();
    }
}