package com.rabbitmq.first_producer_consumer.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.created";

    public static final String ORDER_UPDATED_KEY = "order.updated";

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

    @Bean
    public Binding auditBinding(
            Queue auditQueue,
            DirectExchange orderExchange
    ) {

        return BindingBuilder
                .bind(auditQueue)
                .to(orderExchange)
                .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding updatesBinding(
            Queue updatesQueue,
            DirectExchange orderExchange
    ) {

        return BindingBuilder
                .bind(updatesQueue)
                .to(orderExchange)
                .with(ORDER_UPDATED_KEY);
    }
}
