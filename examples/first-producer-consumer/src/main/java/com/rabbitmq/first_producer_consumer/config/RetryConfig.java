package com.rabbitmq.first_producer_consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryConfig {

    public static final String MAIN_EXCHANGE =
            "main.exchange";

    public static final String RETRY_EXCHANGE =
            "retry.exchange";

    public static final String DLX_EXCHANGE =
            "retry.dead-letter.exchange";

    public static final String ORDER_QUEUE =
            "retry.order.queue";

    public static final String RETRY_QUEUE =
            "retry.queue";

    public static final String DEAD_LETTER_QUEUE =
            "retry.dead-letter.queue";

    public static final String ORDER_ROUTING_KEY =
            "order.routing";

    public static final String RETRY_ROUTING_KEY =
            "retry.routing";

    public static final String DLQ_ROUTING_KEY =
            "dead.routing";

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(RETRY_EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {

        return QueueBuilder
                .durable(ORDER_QUEUE)
                .build();
    }

    @Bean
    public Queue retryQueue() {

        return QueueBuilder
                .durable(RETRY_QUEUE)

                .ttl(10000)

                .deadLetterExchange(
                        MAIN_EXCHANGE
                )

                .deadLetterRoutingKey(
                        ORDER_ROUTING_KEY
                )

                .build();
    }

    @Bean
    public Queue deadLetterQueue() {

        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Binding orderBinding() {

        return BindingBuilder
                .bind(orderQueue())
                .to(mainExchange())
                .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding retryBinding() {

        return BindingBuilder
                .bind(retryQueue())
                .to(retryExchange())
                .with(RETRY_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding() {

        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }
}