package com.rabbitmq.first_producer_consumer.producer;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherConfirmProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishMessage(
            String message
    ) {

        rabbitTemplate.convertAndSend(
                "demo.queue",
                message
        );

        System.out.println(
                "MESSAGE SENT : "
                        + message
        );
    }
}