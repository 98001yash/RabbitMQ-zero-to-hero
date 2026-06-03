package com.rabbitmq.first_producer_consumer.controller;

import com.rabbitmq.first_producer_consumer.producer.DlqProducer;
import com.rabbitmq.first_producer_consumer.producer.MessageProducer;
import com.rabbitmq.first_producer_consumer.producer.PublisherConfirmProducer;
import com.rabbitmq.first_producer_consumer.producer.QueueProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageProducer messageProducer;
    private final QueueProducer producer;
    private final DlqProducer dlqProducer;
    private final PublisherConfirmProducer publisherConfirmProducer;


    @PostMapping
    public String sendMessage(@RequestParam String message) {
        messageProducer.sendMessage(message);
        return "Message Published Successfully";
    }

    @PostMapping("/topic/order/create")
    public String publishOrderCreated(
            @RequestParam String message
    ) {

        producer.publishOrderCreated(message);

        return "Order Created Event Published Successfully";
    }

    @PostMapping("/topic/order/update")
    public String publishOrderUpdated(
            @RequestParam String message
    ) {

        producer.publishOrderUpdated(message);

        return "Order Updated Event Published Successfully";
    }

    @PostMapping("/topic/payment/complete")
    public String publishPaymentCompleted(
            @RequestParam String message
    ) {

        producer.publishPaymentCompleted(message);

        return "Payment Completed Event Published Successfully";
    }

    @PostMapping("/topic/payment/fail")
    public String publishPaymentFailed(
            @RequestParam String message
    ) {

        producer.publishPaymentFailed(message);

        return "Payment Failed Event Published Successfully";
    }


    @PostMapping("/headers/finance")
    public String publishFinanceMessage(
            @RequestParam String message
    ) {

        producer.publishFinanceMessage(
                message
        );

        return "Finance Message Published Successfully";
    }

    @PostMapping("/headers/support")
    public String publishSupportMessage(
            @RequestParam String message
    ) {

        producer.publishSupportMessage(
                message
        );

        return "Support Message Published Successfully";
    }

    @PostMapping("/ack/auto")
    public String publishAutoAckMessage(
            @RequestParam String message
    ) {

        messageProducer.publishAutoAckMessage(
                message
        );
        return "Auto Ack Message Published";
    }

    @PostMapping("/ack/manual")
    public String publishManualAckMessage(
            @RequestParam String message
    ) {

        messageProducer.publishManualAckMessage(
                message
        );
        return "Manual Ack Message Published";
    }

    @PostMapping("/requeue")
    public String sendRequeueMessage(
            @RequestParam String message
    ) {

        messageProducer.publishManualAckMessage(
                message
        );

        return "Message Published";
    }

    @PostMapping("/dlq")
    public String sendDlqMessage(
            @RequestParam String message
    ) {

        dlqProducer.publishMessage(
                message
        );

        return "DLQ Message Published";
    }


    @PostMapping("/publisher-confirm")
    public String publishMessage(
            @RequestParam String message
    ) {

        publisherConfirmProducer.publishMessage(
                message
        );

        return "Publisher Confirm Message Sent";
    }
}