package com.rabbitmq.first_producer_consumer.producer;


import com.rabbitmq.first_producer_consumer.config.RetryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetryProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {

        rabbitTemplate.convertAndSend(
                RetryConfig.MAIN_EXCHANGE,
                RetryConfig.ORDER_ROUTING_KEY,
                message
        );

        System.out.println(
                "MESSAGE SENT : "
                        + message
        );
    }
}