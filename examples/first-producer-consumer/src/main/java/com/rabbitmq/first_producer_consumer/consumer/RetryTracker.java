package com.rabbitmq.first_producer_consumer.consumer;


import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RetryTracker {

    private final ConcurrentHashMap<String, Integer>
            retryMap = new ConcurrentHashMap<>();

    public int increment(String message) {

        return retryMap.merge(
                message,
                1,
                Integer::sum
        );
    }
}
