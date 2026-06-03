package com.rabbitmq.first_producer_consumer.consumer;


import com.rabbitmq.first_producer_consumer.config.PersistenceConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PersistenceConsumer {

    @RabbitListener(
            queues = PersistenceConfig.DURABLE_ORDER_QUEUE
    )
    public void consumeMessage(
            String message
    ) {

        System.out.println(
                "PERSISTENT MESSAGE RECEIVED : "
                        + message
        );
    }
}
