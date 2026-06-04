package com.rabbitmq.first_producer_consumer.consumer;


import com.rabbitmq.first_producer_consumer.config.RetryConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RetryDeadLetterConsumer {

    @RabbitListener(
            queues = RetryConfig.DEAD_LETTER_QUEUE
    )
    public void consumeDeadLetter(
            String message
    ) {

        System.out.println(
                "MESSAGE MOVED TO DLQ : "
                        + message
        );
    }
}