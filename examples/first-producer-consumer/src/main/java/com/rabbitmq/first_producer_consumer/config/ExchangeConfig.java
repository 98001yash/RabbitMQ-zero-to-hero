package com.rabbitmq.first_producer_consumer.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.created";

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(
                ORDER_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Binding orderBinding(
            Queue ordersQueue,
            DirectExchange orderExchange
    ) {

        return BindingBuilder
                .bind(ordersQueue)
                .to(orderExchange)
                .with(ORDER_ROUTING_KEY);
    }
}
