package com.rabbitmq.first_producer_consumer.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitListenerConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory manualAckContainerFactory(
            ConnectionFactory connectionFactory
    ) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(
                connectionFactory
        );

        factory.setAcknowledgeMode(
                AcknowledgeMode.MANUAL
        );

        return factory;
    }
}