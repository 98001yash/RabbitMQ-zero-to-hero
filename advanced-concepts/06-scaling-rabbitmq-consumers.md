# Chapter 06: Scaling RabbitMQ Consumers

## Introduction

One consumer can process only a limited number of messages.

Consider an ecommerce system:

- 10 Orders/Minute → Fine
- 100 Orders/Minute → Fine
- 1000 Orders/Minute → Problem
- 10000 Orders/Minute → System Bottleneck

To handle increasing traffic, RabbitMQ allows consumers to scale horizontally.

---

# The Problem

Single Consumer Architecture

Producer
|
v
RabbitMQ Queue
|
v
Consumer

---

Example

Queue contains:

Message-1
Message-2
Message-3
Message-4
Message-5

Only one consumer processes all messages.

This creates:

- High Latency
- Slow Processing
- Backlog Growth

---

# Consumer Scaling

Instead of:

1 Consumer

Use:

3 Consumers

Producer
|
v
RabbitMQ Queue
|
+------------+
|            |
v            v
Consumer-1   Consumer-2
|
v
Consumer-3

---

# Work Queue Pattern

RabbitMQ automatically distributes messages across consumers.

Example:

Queue:

Order-1
Order-2
Order-3
Order-4
Order-5
Order-6

Consumers:

Consumer-1 → Order-1, Order-4

Consumer-2 → Order-2, Order-5

Consumer-3 → Order-3, Order-6

---

# Round Robin Distribution

By default RabbitMQ uses:

Round Robin

Example:

Message-1 → Consumer-1

Message-2 → Consumer-2

Message-3 → Consumer-3

Message-4 → Consumer-1

Message-5 → Consumer-2

---

# Problem With Round Robin

Suppose:

Consumer-1 → 1 sec processing

Consumer-2 → 20 sec processing

Consumer-3 → 1 sec processing

RabbitMQ still distributes equally.

Result:

Consumer-2 becomes overloaded.

---

# Solution: Prefetch Count

Prefetch Count controls how many unacknowledged messages a consumer can hold.

Example:

prefetch = 1

RabbitMQ sends next message only after acknowledgment.

---

Without Prefetch

Consumer-1 → 100 messages

Consumer-2 → 100 messages

Consumer-3 → 100 messages

Uneven load possible.

---

With Prefetch = 1

Fast consumers receive more work.

Slow consumers receive less work.

Better utilization.

---

# Spring Boot Configuration

application.properties

spring.rabbitmq.listener.simple.prefetch=1

---

# Competing Consumers Pattern

Multiple consumers listening to the same queue.

Only one consumer receives a specific message.

Example:

Inventory Consumer-1
Inventory Consumer-2
Inventory Consumer-3

All consume from:

inventory.queue

Message processed exactly once.

---

# Horizontal Scaling

Increase consumer instances.

Example:

Inventory Service

1 Pod
2 Pods
5 Pods
10 Pods

All consuming from same queue.

---

# Kubernetes Example

Deployment

replicas: 5

RabbitMQ automatically distributes messages.

---

# Throughput Improvement

Single Consumer

100 msg/sec

5 Consumers

500 msg/sec

10 Consumers

1000 msg/sec

(Approximate values)

---

# Queue Backlog

Monitor:

Ready Messages

If queue size continuously grows:

Consumers are slower than producers.

Scale consumers.

---

# Auto Scaling

In Kubernetes:

HPA (Horizontal Pod Autoscaler)

Scale based on:

- CPU Usage
- Memory Usage
- Queue Length

---

# Real-World Example

Order Processing

Order Service
|
v
order.queue
|
+--------------+
|      |       |
v      v       v
Order Worker-1
Order Worker-2
Order Worker-3

Higher throughput.

---

# Interview Questions

## How do you scale RabbitMQ consumers?

By running multiple consumer instances on the same queue.

---

## What is Competing Consumers Pattern?

Multiple consumers compete for messages from a single queue.

---

## What is Prefetch Count?

The maximum number of unacknowledged messages assigned to a consumer.

---

## Why use Prefetch=1?

To prevent slow consumers from receiving too many messages.

---

## How does RabbitMQ distribute messages?

Using Round Robin (combined with prefetch behavior).

---

# Summary

Scaling RabbitMQ Consumers involves:

- Multiple Consumers
- Work Queue Pattern
- Competing Consumers Pattern
- Prefetch Count
- Horizontal Scaling
- Kubernetes Replicas

These concepts are heavily used in production microservices and are common interview topics.