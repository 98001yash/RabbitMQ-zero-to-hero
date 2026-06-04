package com.rabbitmq.first_producer_consumer.consumer;

import com.rabbitmq.first_producer_consumer.config.RetryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Service
@RequiredArgsConstructor
public class RetryConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final RetryTracker retryTracker;

    @RabbitListener(
            queues = RetryConfig.ORDER_QUEUE
    )
    public void consumeMessage(
            String message
    ) {

        int attempt =
                retryTracker.increment(message);

        System.out.println(
                "ATTEMPT : "
                        + attempt
        );

        System.out.println(
                "MESSAGE : "
                        + message
        );

        if (attempt < 3) {

            System.out.println(
                    "PROCESSING FAILED"
            );

            rabbitTemplate.convertAndSend(
                    RetryConfig.RETRY_EXCHANGE,
                    RetryConfig.RETRY_ROUTING_KEY,
                    message
            );

            return;
        }

        System.out.println(
                "MAX RETRIES EXCEEDED"
        );

        rabbitTemplate.convertAndSend(
                RetryConfig.DLX_EXCHANGE,
                RetryConfig.DLQ_ROUTING_KEY,
                message
        );
    }
}