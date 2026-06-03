package com.rabbitmq.first_producer_consumer.producer;


import com.rabbitmq.first_producer_consumer.config.PersistenceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersistenceProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishMessage(String message) {

        rabbitTemplate.convertAndSend(
                PersistenceConfig.DURABLE_ORDER_QUEUE,
                message,
                msg -> {

                    msg.getMessageProperties()
                            .setDeliveryMode(
                                    MessageDeliveryMode.PERSISTENT
                            );

                    return msg;
                }
        );

        System.out.println(
                "PERSISTENT MESSAGE SENT : "
                        + message
        );
    }
}
