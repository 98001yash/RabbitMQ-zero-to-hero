package com.rabbitmq.first_producer_consumer.consumer;


import com.rabbitmq.first_producer_consumer.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {

        System.out.println("Message Received : " + message);
    }
}
