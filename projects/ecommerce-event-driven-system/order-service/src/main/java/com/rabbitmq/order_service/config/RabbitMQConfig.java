package com.rabbitmq.order_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";

    public static final String INVENTORY_QUEUE = "inventory.queue";

    public static final String PAYMENT_QUEUE = "payment.queue";

    public static final String ORDER_ROUTING_KEY = "order.created";

    @Bean
    public Queue inventoryQueue() {
        System.out.println("CREATING INVENTORY QUEUE");
        return new Queue(INVENTORY_QUEUE);
    }

    @Bean
    public Queue paymentQueue() {
        System.out.println("CREATING PAYMENT QUEUE");
        return new Queue(PAYMENT_QUEUE);
    }

    @Bean
    public DirectExchange orderExchange() {
        System.out.println("CREATING ORDER EXCHANGE");
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding inventoryBinding() {
        return BindingBuilder
                .bind(inventoryQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding paymentBinding() {
        return BindingBuilder
                .bind(paymentQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory
    ) {

        RabbitTemplate rabbitTemplate =
                new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(
                messageConverter()
        );

        return rabbitTemplate;
    }
}