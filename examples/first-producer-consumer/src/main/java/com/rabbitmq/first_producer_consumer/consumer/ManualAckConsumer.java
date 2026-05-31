package com.rabbitmq.first_producer_consumer.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.first_producer_consumer.config.QueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ManualAckConsumer {

    @RabbitListener(
            queues = QueueConfig.MANUAL_ACK_QUEUE,
            containerFactory = "manualAckContainerFactory"
    )
    public void consumeMessage(
            Message message,
            Channel channel
    ) throws IOException {

        String body =
                new String(
                        message.getBody()
                );

        long deliveryTag =
                message.getMessageProperties()
                        .getDeliveryTag();

        System.out.println(
                "MANUAL ACK RECEIVED : "
                        + body
        );

        channel.basicAck(
                deliveryTag,
                false
        );

        System.out.println(
                "ACK SENT FOR : "
                        + body
        );
    }
}