# Chapter 07: RabbitMQ System Design Interview Questions

## Introduction

System Design interviews focus on evaluating a candidate's ability to design scalable, reliable, fault-tolerant, and maintainable systems.

RabbitMQ is commonly used in:

* Ecommerce Platforms
* Banking Systems
* Food Delivery Applications
* Ride Booking Systems
* Notification Platforms
* Logistics Systems
* Event-Driven Microservices

This chapter covers common system design questions involving RabbitMQ.

---

# Question 1: Design an Ecommerce Order Processing System

## Requirements

When a customer places an order:

* Reserve Inventory
* Process Payment
* Send Notification

Services should be loosely coupled.

---

## Architecture

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
* Independent Service Deployment

---

## Reliability Features

Use:

```text
Durable Queues
Persistent Messages
Publisher Confirms
Manual ACK
DLQ
```

---

# Question 2: Design a Food Delivery System

## Requirements

Support:

* Order Placement
* Restaurant Acceptance
* Food Preparation
* Delivery Assignment
* Order Completion

---

## Architecture

```text
Order Service
      |
      v
OrderPlacedEvent
      |
      v
RabbitMQ
      |
      +------------+
      |            |
      v            v
Restaurant    Delivery
 Service       Service
```

---

## Event Flow

```text
OrderPlaced

RestaurantAccepted

FoodPrepared

DeliveryAssigned

OrderDelivered
```

Each service reacts to events.

---

## Benefits

* Services remain independent
* Easy to scale
* Supports high traffic

---

# Question 3: Design a Ride Booking System

## Requirements

Support:

* Ride Requests
* Driver Matching
* Ride Tracking
* Payment Processing

---

## Architecture

```text
Passenger App
       |
       v
Ride Service
       |
       v
RideRequestedEvent
       |
       v
RabbitMQ
       |
       v
Driver Matching Service
```

---

## Event Flow

```text
Ride Requested
      |
Driver Assigned
      |
Ride Started
      |
Ride Completed
      |
Payment Processed
```

---

## RabbitMQ Usage

RabbitMQ handles communication between:

* Driver Service
* Ride Service
* Payment Service
* Notification Service

---

# Question 4: Design a Banking Transaction System

## Requirements

Support:

* Fund Transfer
* Transaction Logging
* Notifications
* Fraud Detection

---

## Architecture

```text
Transaction Service
         |
         v
TransactionCreatedEvent
         |
         v
RabbitMQ
    /      |       \
   /       |        \
Audit   Fraud   Notification
```

---

## Benefits

One transaction event can trigger multiple workflows.

---

## Reliability Requirements

Must use:

```text
Publisher Confirms
Manual ACK
DLQ
Persistent Messages
```

Banking systems cannot lose messages.

---

# Question 5: Design an Email Notification Platform

## Requirements

Applications should send:

* Welcome Emails
* OTP Emails
* Promotional Emails

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

## Why RabbitMQ?

Applications don't wait for email delivery.

Instead:

```text
Publish Event
Continue Processing
```

Email is sent asynchronously.

---

## Reliability

Use:

```text
Retry Queue
DLQ
```

to handle SMTP failures.

---

# Question 6: Design a Real-Time Notification System

## Requirements

Support:

* Email
* SMS
* Push Notifications

---

## Architecture

```text
Notification Event
        |
        v
Fanout Exchange
   /       |       \
  /        |        \
Email     SMS      Push
```

---

## Why Fanout Exchange?

Every consumer receives the same message.

---

# Question 7: Design a Logistics Tracking System

## Requirements

Track:

* Shipment Created
* Package Picked Up
* In Transit
* Delivered

---

## Architecture

```text
Shipment Service
        |
        v
Shipment Events
        |
        v
RabbitMQ
   /         \
  /           \
Tracking    Notification
 Service      Service
```

---

## Benefits

Multiple systems react independently.

---

# Question 8: Design a Scalable Order Processing System

## Requirements

Handle:

```text
1 Million Orders Per Day
```

---

## Solution

Use Competing Consumers.

```text
payment.queue

Consumer-1
Consumer-2
Consumer-3
Consumer-4
Consumer-5
```

RabbitMQ distributes messages.

---

## Scaling Strategy

Increase:

```text
Consumers
Pods
Instances
```

instead of increasing server size.

---

# Question 9: Design a High Availability Messaging System

## Requirements

No message loss.

---

## Solution

Enable:

```text
Durable Queues
Persistent Messages
Publisher Confirms
Manual ACK
```

---

## Architecture

```text
Producer
    |
RabbitMQ
    |
Consumer
```

Every stage confirms successful delivery.

---

# Question 10: Design a Payment Processing Workflow

## Requirements

Support:

* Payment Initiation
* Payment Processing
* Payment Confirmation

---

## Architecture

```text
Order Service
      |
      v
Payment Queue
      |
      v
Payment Service
      |
      v
PaymentCompletedEvent
      |
      v
Notification Service
```

---

## Failure Handling

If payment fails:

```text
Retry Queue
      |
      v
DLQ
```

---

# Question 11: Design a Multi-Service Event-Driven Architecture

## Requirements

Services:

* Orders
* Inventory
* Payment
* Shipping
* Notifications

---

## Architecture

```text
Order Service
      |
      v
RabbitMQ
      |
      +-------------------+
      |        |          |
      v        v          v
Inventory Payment Shipping
```

---

## Why Event-Driven?

Advantages:

* Loose Coupling
* Better Scalability
* Independent Deployments

---

# Question 12: Design a Large-Scale Notification Platform

## Requirements

Send:

```text
10 Million Notifications Daily
```

---

## Solution

Use:

```text
Topic Exchange
```

Routing Examples:

```text
email.notification
sms.notification
push.notification
```

---

## Consumers

```text
Email Consumer Pool

SMS Consumer Pool

Push Consumer Pool
```

Each can scale independently.

---

# Question 13: How Would You Prevent Duplicate Processing?

## Problem

RabbitMQ redelivers messages.

Example:

```text
Order #101
```

processed twice.

---

## Solution

Implement Idempotency.

Store:

```text
Event ID
```

Check:

```text
Already Processed?
```

before processing.

---

# Question 14: How Would You Handle Poison Messages?

## Problem

Invalid messages fail repeatedly.

Example:

```json
{
  "amount": "INVALID"
}
```

---

## Solution

Move to:

```text
Dead Letter Queue
```

instead of retrying forever.

---

# Question 15: How Would You Handle Traffic Spikes?

## Example

Black Friday Sale:

```text
100,000 Orders Per Hour
```

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

Consumers process messages gradually.

---

# Frequently Asked Interview Questions

## Why RabbitMQ in Microservices?

Answer:

* Loose Coupling
* Asynchronous Communication
* Reliability
* Scalability

---

## How Do You Prevent Message Loss?

Answer:

```text
Durable Queues
Persistent Messages
Publisher Confirms
Manual ACK
```

---

## How Do You Handle Failed Messages?

Answer:

```text
Retry Queue
Dead Letter Queue
```

---

## How Do You Scale RabbitMQ Consumers?

Answer:

```text
Competing Consumers
Horizontal Scaling
```

---

## How Do You Prevent Duplicate Processing?

Answer:

```text
Idempotent Consumers
Event IDs
Database Checks
```

---

# Complete Production Architecture

```text
Producer
    |
    v
Exchange
    |
    v
Queue
    |
    v
Consumer
    |
Success -----> ACK
    |
Failure
    |
Retry Queue
    |
Failure
    |
DLQ
```

---

# Real Interview Answer

"When designing systems with RabbitMQ, I focus on scalability, reliability, and fault tolerance. I use exchanges and routing keys for decoupling, durable queues and publisher confirms for reliability, competing consumers for scalability, and DLQs with retries for failure handling. This allows services to communicate asynchronously while remaining independent and resilient."

---

# Summary

RabbitMQ is widely used in modern distributed systems because it enables:

* Event-Driven Architecture
* Asynchronous Communication
* Scalability
* Reliability
* Fault Tolerance

Understanding how RabbitMQ fits into real-world system design is critical for Backend, Microservices, and Senior Software Engineer interviews.
