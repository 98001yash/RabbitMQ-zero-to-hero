package com.rabbitmq.first_producer_consumer.producer;


import com.rabbitmq.first_producer_consumer.config.ExchangeConfig;
import com.rabbitmq.first_producer_consumer.config.QueueConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrdersMessage(String message){
//        rabbitTemplate.convertAndSend(
//                QueueConfig.ORDERS_QUEUE,
//                message
//        );
        rabbitTemplate.convertAndSend(
                ExchangeConfig.ORDER_EXCHANGE,
                ExchangeConfig.ORDER_ROUTING_KEY,
                message
        );
        System.out.println(
                "Order Message Sent : " + message
        );
    }

    public void sendNotificationMessage(String message){
        rabbitTemplate.convertAndSend(
                QueueConfig.NOTIFICATIONS_QUEUE,
                message
        );
        System.out.println(
                "Notification Message Sent : " + message
        );
    }
}
