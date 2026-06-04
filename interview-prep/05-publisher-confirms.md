# Chapter 05: Publisher Confirms

## Introduction

Publisher Confirms are a reliability mechanism that allows a producer to know whether RabbitMQ successfully received and stored a message.

Without Publisher Confirms:

```text
Producer
    |
    v
RabbitMQ
```

The producer sends a message but has no guarantee that RabbitMQ actually received it.

With Publisher Confirms:

```text
Producer
    |
    v
RabbitMQ
    |
 Confirm
    |
    v
Producer
```

The producer receives confirmation from RabbitMQ.

---

# Why Do We Need Publisher Confirms?

Consider an ecommerce application:

```text
Order Service
      |
      v
RabbitMQ
      |
      v
Payment Service
```

When an order is created:

```text
OrderCreatedEvent
```

is published.

Suppose:

```text
RabbitMQ crashes
```

immediately after the producer sends the message.

Question:

```text
Did RabbitMQ receive the message?
```

Without Publisher Confirms:

```text
Producer Doesn't Know
```

This can result in lost orders.

---

# The Reliability Problem

Without confirms:

```text
Producer
   |
   | Send Message
   |
   v
RabbitMQ
```

If RabbitMQ crashes:

```text
Message Status Unknown
```

Producer cannot determine whether the message was stored.

---

# What Are Publisher Confirms?

Publisher Confirms are acknowledgements sent by RabbitMQ back to the producer.

RabbitMQ says:

```text
Message Successfully Received
```

or

```text
Message Could Not Be Processed
```

---

# Publisher Confirm Workflow

```text
Producer
    |
Publish Message
    |
    v
RabbitMQ
    |
Store Message
    |
ACK
    |
    v
Producer
```

Producer receives confirmation.

---

# What Is an ACK in Publisher Confirms?

ACK means:

```text
Message Successfully Accepted
```

RabbitMQ has received and stored the message.

Producer can safely continue.

---

# What Is a NACK in Publisher Confirms?

NACK means:

```text
Message Could Not Be Processed
```

Possible reasons:

* Internal broker failure
* Resource exhaustion
* Exchange issue

Producer should retry.

---

# Message Flow With ACK

```text
Producer
    |
    v
RabbitMQ
    |
ACK
    |
    v
Producer
```

Result:

```text
Message Successfully Stored
```

---

# Message Flow With NACK

```text
Producer
    |
    v
RabbitMQ
    |
NACK
    |
    v
Producer
```

Result:

```text
Message Publish Failed
```

Producer can retry.

---

# Publisher Confirms vs Consumer ACK

Many developers confuse these concepts.

---

## Publisher Confirm

Between:

```text
Producer
    |
    v
RabbitMQ
```

Purpose:

```text
Did RabbitMQ receive the message?
```

---

## Consumer ACK

Between:

```text
RabbitMQ
    |
    v
Consumer
```

Purpose:

```text
Did the consumer process the message?
```

---

# Publisher Confirm Example

```text
Order Service
      |
      v
RabbitMQ
```

Order Service publishes:

```text
OrderCreatedEvent
```

RabbitMQ responds:

```text
ACK
```

Order Service knows the event is safe.

---

# Why Are Publisher Confirms Important?

Without confirms:

```text
Message May Be Lost
```

Producer never knows.

With confirms:

```text
Guaranteed Broker Acceptance
```

Producer gets certainty.

---

# Publisher Confirm Modes

RabbitMQ supports three approaches.

---

## Individual Confirms

Every message waits for confirmation.

```text
Send Message
Wait ACK

Send Message
Wait ACK
```

Very reliable.

Lower throughput.

---

## Batch Confirms

Publish multiple messages.

```text
Message 1
Message 2
Message 3
```

Wait for confirmation after the batch.

Better performance.

---

## Asynchronous Confirms

Producer continues publishing.

RabbitMQ confirms later.

```text
Publish
Publish
Publish

ACK arrives later
```

Highest throughput.

Most common in production.

---

# Spring Boot Publisher Confirms

Enable confirms:

```properties
spring.rabbitmq.publisher-confirm-type=correlated
```

RabbitMQ will send acknowledgements back to the producer.

---

# ConfirmCallback

Spring provides:

```java
ConfirmCallback
```

Example:

```java
rabbitTemplate.setConfirmCallback(
    (correlationData, ack, cause) -> {

        if (ack) {
            System.out.println(
                "Message Confirmed"
            );
        } else {
            System.out.println(
                "Message Failed"
            );
        }
    }
);
```

---

# What Happens If ACK Is False?

RabbitMQ rejected the message.

Example:

```java
if (!ack) {
    retryMessage();
}
```

Producer can retry publishing.

---

# What Is Correlation Data?

Correlation Data identifies messages.

Example:

```java
CorrelationData correlationData =
        new CorrelationData(
                UUID.randomUUID()
                      .toString()
        );
```

Useful when tracking confirms.

---

# What Are Publisher Returns?

Publisher Returns handle a different problem.

Question:

```text
RabbitMQ received the message,
but could not route it.
```

What happens now?

Publisher Returns provide the answer.

---

# Example of Unroutable Message

Producer sends:

```text
Routing Key = payment.failed
```

Exchange only supports:

```text
payment.completed
```

Result:

```text
No Matching Queue
```

Message cannot be delivered.

---

# Publisher Returns Flow

```text
Producer
    |
    v
Exchange
    |
No Queue Match
    |
Return Message
    |
    v
Producer
```

Producer gets notified.

---

# Mandatory Flag

To receive returned messages:

```java
rabbitTemplate.setMandatory(true);
```

Without mandatory:

```text
Message Discarded
```

With mandatory:

```text
Message Returned
```

---

# Publisher Confirm vs Publisher Return

## Publisher Confirm

Checks:

```text
Did RabbitMQ Receive Message?
```

---

## Publisher Return

Checks:

```text
Could RabbitMQ Route Message?
```

---

# Example

Message Published:

```text
OrderCreatedEvent
```

RabbitMQ receives it.

Result:

```text
Publisher Confirm = ACK
```

However:

```text
No Queue Binding Exists
```

Result:

```text
Publisher Return Triggered
```

---

# Common Production Pattern

Companies use:

```text
Publisher Confirms
+
Publisher Returns
```

Together.

This guarantees:

```text
Message Received
Message Routed
```

---

# Benefits of Publisher Confirms

## Reliability

Producer knows message status.

---

## Error Detection

Failures are detected immediately.

---

## Retry Capability

Failed messages can be retried.

---

## Better Monitoring

Publishing failures become visible.

---

# Real World Use Cases

### Ecommerce

```text
OrderCreatedEvent
```

Must never be lost.

---

### Banking

```text
Transaction Event
```

Requires guaranteed delivery.

---

### Payment Systems

```text
PaymentCompletedEvent
```

Must be confirmed.

---

### Logistics

```text
ShipmentCreatedEvent
```

Needs reliable delivery.

---

# Common Interview Questions

## What are Publisher Confirms?

Publisher Confirms allow RabbitMQ to acknowledge whether a message was successfully received and stored.

---

## Why use Publisher Confirms?

To ensure producers know whether RabbitMQ accepted the message.

---

## What is ACK?

RabbitMQ successfully accepted the message.

---

## What is NACK?

RabbitMQ failed to process the message.

---

## What is Publisher Return?

Notification sent when a message cannot be routed to any queue.

---

## Difference Between Publisher Confirm and Consumer ACK?

Publisher Confirm:

```text
Producer ↔ RabbitMQ
```

Consumer ACK:

```text
RabbitMQ ↔ Consumer
```

---

## Why use Mandatory Flag?

To receive unroutable messages back from RabbitMQ.

---

# Real Interview Answer

"Publisher Confirms are a RabbitMQ reliability mechanism that allows producers to verify whether RabbitMQ successfully received and stored a message. RabbitMQ responds with ACK or NACK. Publisher Returns complement this by notifying producers when a message cannot be routed to any queue. Together they ensure reliable message publishing."

---

# Summary

Publisher Confirms help producers verify message delivery to RabbitMQ.

Key Concepts:

* Publisher Confirms
* ACK
* NACK
* ConfirmCallback
* Correlation Data
* Publisher Returns
* Mandatory Flag
* Reliable Publishing

Publisher Confirms are essential for building reliable event-driven systems and are widely used in production RabbitMQ deployments.
