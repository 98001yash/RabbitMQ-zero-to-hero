package com.rabbitmq.first_producer_consumer.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfig {

    public static final String DURABLE_ORDER_QUEUE =
            "durable-order.queue";

    @Bean
    public Queue durableOrderQueue() {

        return QueueBuilder
                .durable(DURABLE_ORDER_QUEUE)
                .build();
    }
}
