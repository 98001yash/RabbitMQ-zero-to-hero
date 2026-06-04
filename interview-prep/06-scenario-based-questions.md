# Chapter 06: Scenario-Based RabbitMQ Interview Questions

## Introduction

Scenario-based questions are very common in backend, microservices, and system design interviews.

The interviewer is not testing whether you know RabbitMQ definitions.

Instead, they want to evaluate:

* Problem Solving
* System Design Skills
* RabbitMQ Knowledge
* Reliability Strategies
* Scalability Approaches

This chapter covers common real-world scenarios and their solutions.

---

# Scenario 1: Design an Order Processing System

## Question

Design an ecommerce order processing system using RabbitMQ.

---

## Solution

### Components

```text
Order Service
Inventory Service
Payment Service
Notification Service
RabbitMQ
```

---

### Flow

```text
Order Service
      |
      v
OrderCreatedEvent
      |
      v
RabbitMQ
      |
      +------------------+
      |                  |
      v                  v
Inventory          Payment
 Service           Service
                       |
                       v
             PaymentCompletedEvent
                       |
                       v
              Notification Service
```

---

## Why RabbitMQ?

Benefits:

* Loose Coupling
* Scalability
* Reliability
* Asynchronous Processing

---

# Scenario 2: Payment Service Is Down

## Question

What happens if Payment Service is temporarily unavailable?

---

## Problem

```text
OrderCreatedEvent
       |
       v
Payment Service
```

Payment Service crashes.

---

## Solution

Store messages in RabbitMQ Queue.

```text
RabbitMQ Queue
      |
      v
Payment Service
```

When Payment Service comes back:

```text
Pending Messages Processed
```

No data loss occurs.

---

# Scenario 3: Notification Service Keeps Failing

## Question

Email provider is down.

Notification processing fails repeatedly.

What should you do?

---

## Solution

Use:

```text
Retry Queue
```

and

```text
Dead Letter Queue
```

Flow:

```text
Notification Queue
       |
       X Failure
       |
       v
Retry Queue
       |
       X Retry Failed
       |
       v
DLQ
```

---

## Benefit

Messages are not lost.

Failed messages can be reprocessed later.

---

# Scenario 4: Process One Million Orders Per Day

## Question

Your ecommerce platform receives one million orders daily.

How would you scale RabbitMQ consumers?

---

## Solution

Use Competing Consumers.

```text
payment.queue

Consumer-1
Consumer-2
Consumer-3
Consumer-4
```

RabbitMQ distributes messages.

---

## Benefit

Higher throughput.

Better scalability.

---

# Scenario 5: Multiple Services Need The Same Event

## Question

OrderCreatedEvent should be consumed by:

* Payment Service
* Inventory Service
* Analytics Service

How would you design it?

---

## Solution

Use a Direct Exchange.

```text
OrderCreatedEvent
        |
        v
order.exchange
   /      |      \
  /       |       \
Inventory Payment Analytics
```

Each service gets a copy.

---

# Scenario 6: Prevent Duplicate Payment Processing

## Question

RabbitMQ redelivers a message.

Payment Service receives the same message twice.

How do you prevent duplicate charges?

---

## Problem

```text
Order #101
```

Processed twice.

Customer gets charged twice.

---

## Solution

Use Idempotency.

Store:

```text
OrderId
```

or

```text
EventId
```

in database.

Before processing:

```text
Already Processed?
```

If yes:

```text
Ignore Message
```

---

# Scenario 7: RabbitMQ Restarts Unexpectedly

## Question

RabbitMQ server crashes and restarts.

How do you prevent message loss?

---

## Solution

Use:

### Durable Queue

```java
new Queue(
    "orders",
    true
);
```

---

### Persistent Messages

```text
DeliveryMode.PERSISTENT
```

---

### Publisher Confirms

Guarantee broker received message.

---

# Scenario 8: Inventory Service Is Slow

## Question

Inventory processing takes several seconds.

Queue size keeps increasing.

How do you solve it?

---

## Solution

Increase consumers.

```text
Inventory Queue

Consumer-1
Consumer-2
Consumer-3
Consumer-4
```

RabbitMQ load balances messages.

---

# Scenario 9: Order Service Should Not Wait For Payment

## Question

Why should Order Service not directly call Payment Service?

---

## Bad Design

```text
Order Service
      |
      v
Payment Service
```

Problems:

* Tight Coupling
* Blocking Calls
* Service Dependency

---

## Better Design

```text
Order Service
      |
      v
RabbitMQ
      |
      v
Payment Service
```

Benefits:

* Loose Coupling
* Async Processing
* Better Scalability

---

# Scenario 10: RabbitMQ Queue Contains Invalid Messages

## Question

Some messages have invalid JSON format.

What should happen?

---

## Solution

Move message to:

```text
Dead Letter Queue
```

Do not retry forever.

---

## Why?

Invalid messages usually never succeed.

These are called:

```text
Poison Messages
```

---

# Scenario 11: Audit Every Order Event

## Question

Every order event should be stored for reporting.

How would you design it?

---

## Solution

Bind multiple queues.

```text
OrderCreatedEvent
       |
       v
order.exchange
   /      \
  /        \
Payment   Analytics
 Queue      Queue
```

Analytics receives the same event.

---

# Scenario 12: Send Notifications Through Multiple Channels

## Question

Notification event should trigger:

* Email
* SMS
* Push Notification

---

## Solution

Use Fanout Exchange.

```text
Notification Event
         |
         v
Fanout Exchange
   /      |      \
  /       |       \
Email    SMS     Push
```

Every consumer receives the message.

---

# Scenario 13: Route Events Based On Category

## Question

Messages:

```text
order.created
order.cancelled
payment.completed
```

Need different routing.

---

## Solution

Use Topic Exchange.

Example:

```text
order.*
```

Receives:

```text
order.created
order.cancelled
```

---

# Scenario 14: Guarantee Event Delivery

## Question

How do you guarantee RabbitMQ received the message?

---

## Solution

Use:

```text
Publisher Confirms
```

Flow:

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

---

# Scenario 15: Consumer Crashes During Processing

## Question

Consumer crashes after receiving message.

What happens?

---

## Solution

Use Manual ACK.

Flow:

```text
RabbitMQ
     |
     v
Consumer
     |
     X Crash
```

No ACK received.

RabbitMQ:

```text
Redelivers Message
```

---

# Scenario 16: Build a Food Delivery System

## Question

How would RabbitMQ fit into Swiggy or Zomato?

---

## Solution

Events:

```text
Order Placed
Restaurant Accepted
Food Prepared
Delivery Assigned
Order Delivered
```

Each step publishes events.

RabbitMQ connects services.

---

# Scenario 17: Design an Email Notification Platform

## Question

How would you build an email platform using RabbitMQ?

---

## Architecture

```text
Application
     |
     v
RabbitMQ
     |
     v
Email Service
```

---

## Benefits

* Async Processing
* Retry Support
* DLQ Support
* Scalability

---

# Scenario 18: Handle Traffic Spikes

## Question

Black Friday Sale generates 100,000 orders in one hour.

How would RabbitMQ help?

---

## Solution

RabbitMQ acts as a buffer.

```text
Orders
    |
    v
Queue
    |
    v
Consumers
```

Messages wait safely in queue.

Consumers process them gradually.

---

# Scenario 19: RabbitMQ vs Direct Database Polling

## Question

Why not poll database instead of using RabbitMQ?

---

## Database Polling

```text
Service
   |
Repeated Queries
   |
Database
```

Problems:

* Expensive
* Slow
* Inefficient

---

## RabbitMQ

```text
Producer
   |
   v
RabbitMQ
   |
   v
Consumer
```

Efficient event-driven communication.

---

# Scenario 20: Design a Reliable Messaging System

## Question

What features would you enable?

---

## Recommended Setup

```text
Durable Queues
Persistent Messages
Manual ACK
Publisher Confirms
Retry Mechanism
Dead Letter Queue
Idempotent Consumers
```

This is the standard production-grade RabbitMQ architecture.

---

# Frequently Asked Interview Question

## Design a scalable ecommerce order processing system using RabbitMQ.

### Expected Answer

Use:

* Order Service
* Inventory Service
* Payment Service
* Notification Service

Communication:

```text
RabbitMQ Exchanges
RabbitMQ Queues
Routing Keys
```

Reliability:

```text
Publisher Confirms
Manual ACK
DLQ
Retries
```

Scalability:

```text
Competing Consumers
Horizontal Scaling
```

---

# Summary

Scenario-based interviews focus on applying RabbitMQ concepts to real systems.

Key themes:

* Reliability
* Scalability
* Failure Handling
* Event-Driven Design
* DLQ
* Retries
* Publisher Confirms
* Idempotency

Being able to explain these scenarios clearly is often more valuable than memorizing RabbitMQ definitions.
