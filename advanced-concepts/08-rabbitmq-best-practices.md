# Chapter 08: RabbitMQ Best Practices

## Introduction

RabbitMQ can handle millions of messages per day when designed correctly.

However, achieving a reliable, scalable, and production-ready system requires following industry best practices.

This chapter covers the practices commonly used in large-scale systems at companies like Amazon, Uber, Netflix, Paytm, Swiggy, and Zomato.

---

# 1. Design for Asynchronous Communication

One of the biggest mistakes developers make is using RabbitMQ while still keeping services tightly coupled.

## Bad Design

```text
Order Service
      |
      v
Payment Service
      |
      v
Inventory Service
      |
      v
Notification Service
```

If one service goes down, the entire flow fails.

---

## Good Design

```text
Order Service
      |
      v
RabbitMQ
      |
      +------> Payment Service
      |
      +------> Inventory Service
      |
      +------> Notification Service
```

Services become independent.

---

# 2. Use Dedicated Queues Per Service

Avoid sharing one queue among multiple business domains.

## Bad

```text
events.queue
```

Contains:

* Orders
* Payments
* Inventory
* Notifications

---

## Good

```text
order.queue

payment.queue

inventory.queue

notification.queue
```

Benefits:

* Easier scaling
* Easier monitoring
* Easier troubleshooting

---

# 3. Always Use Durable Queues

Non-durable queues disappear when RabbitMQ restarts.

## Bad

```java
new Queue("orders", false);
```

---

## Good

```java
new Queue("orders", true);
```

Durable queues survive broker restarts.

---

# 4. Use Persistent Messages

Durable queues alone are not enough.

Messages must also be persistent.

---

## Example

```java
rabbitTemplate.convertAndSend(
        exchange,
        routingKey,
        event
);
```

Configure publisher to send persistent messages.

Benefits:

* Better reliability
* Survives broker restart

---

# 5. Always Configure Dead Letter Queues

Every production queue should have a DLQ.

## Why?

Messages can fail because of:

* Invalid payload
* Database issues
* Business validation errors
* Third-party API failures

---

## Architecture

```text
Main Queue
     |
     v
Consumer Failure
     |
     v
DLX
     |
     v
DLQ
```

Failed messages are isolated safely.

---

# 6. Implement Retry Mechanisms

Temporary failures happen.

Examples:

* Database down
* Payment gateway unavailable
* External API timeout

---

## Retry Flow

```text
Consumer Failure
      |
      v
Retry Queue
      |
      v
Consumer Retry
      |
      +---- Success
      |
      +---- DLQ
```

Never retry forever.

---

# 7. Configure Prefetch Count

RabbitMQ distributes messages efficiently when prefetch is configured.

---

## Recommended

```properties
spring.rabbitmq.listener.simple.prefetch=1
```

Benefits:

* Fair dispatch
* Better load balancing
* Reduced consumer starvation

---

# 8. Use Idempotent Consumers

RabbitMQ guarantees:

```text
At-Least-Once Delivery
```

This means duplicate messages can occur.

---

## Example

Consumer:

```text
Charge Customer
```

Consumer crashes before ACK.

RabbitMQ redelivers message.

Customer charged twice.

---

## Solution

Store processed IDs.

Example:

```text
orderId
paymentId
transactionId
```

Ignore duplicates.

---

# 9. Keep Messages Small

RabbitMQ performs best with lightweight messages.

---

## Bad

```text
10 MB PDF
20 MB Video
50 MB ZIP File
```

---

## Good

```json
{
  "fileId": "123",
  "fileUrl": "https://storage.com/file.pdf"
}
```

Store large files elsewhere.

---

# 10. Version Your Events

Event contracts evolve.

---

## Version 1

```json
{
  "orderId": 1
}
```

---

## Version 2

```json
{
  "orderId": 1,
  "customerId": 101
}
```

---

## Best Practice

Add version field.

```json
{
  "version": 2,
  "orderId": 1,
  "customerId": 101
}
```

Prevents breaking consumers.

---

# 11. Monitor Queue Depth

Queue growth indicates problems.

---

## Example

```text
Queue Size

0
10
100
1000
10000
```

Consumers cannot keep up.

---

## Monitor

* Ready Messages
* Unacked Messages
* Message Rate
* Consumer Count

---

# 12. Monitor Dead Letter Queues

DLQ growth is a warning sign.

---

## Example

```text
DLQ Size

0
2
5
100
1000
```

Something is failing repeatedly.

Investigate immediately.

---

# 13. Use Separate Exchanges for Different Domains

Avoid one giant exchange.

---

## Bad

```text
events.exchange
```

Everything goes through one exchange.

---

## Better

```text
order.exchange

payment.exchange

inventory.exchange

notification.exchange
```

Improves maintainability.

---

# 14. Use Meaningful Routing Keys

Routing keys should describe business events.

---

## Bad

```text
event1

event2

event3
```

---

## Good

```text
order.created

order.cancelled

payment.completed

inventory.reserved
```

Easy to understand.

---

# 15. Scale Consumers Horizontally

One consumer is rarely enough.

---

## Example

```text
Queue
  |
  +---- Consumer 1
  +---- Consumer 2
  +---- Consumer 3
  +---- Consumer 4
```

RabbitMQ automatically distributes work.

---

# 16. Separate Business Logic from Messaging Logic

Consumer should not contain complex processing.

---

## Bad

```java
@RabbitListener(...)
public void consume(...) {

    // Huge business logic here

}
```

---

## Good

```java
@RabbitListener(...)
public void consume(Event event) {

    orderService.process(event);

}
```

Cleaner architecture.

---

# 17. Use Structured Logging

Avoid logs like:

```text
Received message
```

---

## Better

```text
Order Created Event Received

orderId=101
customerId=200
amount=5000
```

Makes debugging easier.

---

# 18. Use Correlation IDs

Correlation IDs help trace messages across services.

---

## Example

```text
Correlation-ID:
abc-123-xyz
```

Flow:

```text
Order Service
      |
Payment Service
      |
Notification Service
```

All logs share same ID.

---

# 19. Secure RabbitMQ

Never expose RabbitMQ publicly.

---

## Use

* Authentication
* Authorization
* TLS
* VPN
* Firewalls

---

## Avoid

```text
guest / guest
```

In production.

---

# 20. Document Event Contracts

Every event should be documented.

---

## Example

OrderCreatedEvent

Fields:

```text
orderId
productName
quantity
amount
```

Consumers know exactly what to expect.

---

# Real Production Example

E-Commerce Platform

```text
Order Service
      |
      v
order.exchange
      |
      +------> Inventory Queue
      |
      +------> Payment Queue
      |
      +------> Audit Queue
```

Payment Service

```text
payment.exchange
      |
      +------> Notification Queue
```

Benefits:

* Loose Coupling
* Scalability
* Reliability
* Maintainability

---

# Interview Questions

## Why should queues be durable?

To survive RabbitMQ restarts.

---

## Why should consumers be idempotent?

RabbitMQ may redeliver messages.

---

## Why use DLQ?

To isolate failed messages.

---

## Why use prefetch count?

To distribute workload fairly.

---

## Why keep messages small?

Large messages reduce RabbitMQ performance.

---

## What should be monitored in RabbitMQ?

* Queue Depth
* Consumers
* Message Rate
* Unacked Messages
* DLQ Growth

---

# Summary

Production-ready RabbitMQ systems should:

* Use asynchronous communication
* Have dedicated queues
* Use durable queues
* Use persistent messages
* Configure DLQs
* Configure retries
* Use idempotent consumers
* Monitor queue health
* Scale consumers
* Secure RabbitMQ
* Version events
* Document contracts

Following these best practices results in reliable, scalable, and maintainable event-driven systems.
