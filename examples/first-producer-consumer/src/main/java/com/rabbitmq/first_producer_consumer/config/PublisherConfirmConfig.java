package com.rabbitmq.first_producer_consumer.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PublisherConfirmConfig {

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void configurePublisherConfirms() {

        rabbitTemplate.setConfirmCallback(

                (correlationData, ack, cause) -> {

                    if (ack) {

                        System.out.println(
                                "BROKER ACK RECEIVED"
                        );

                    } else {

                        System.out.println(
                                "BROKER NACK RECEIVED : "
                                        + cause
                        );
                    }
                }
        );
    }
}