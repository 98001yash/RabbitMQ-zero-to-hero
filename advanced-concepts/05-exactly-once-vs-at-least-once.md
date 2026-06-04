# Chapter 05: Exactly-Once vs At-Least-Once Delivery

## Introduction

One of the most frequently asked RabbitMQ and Distributed Systems interview questions is:

> What is the difference between At-Most-Once, At-Least-Once, and Exactly-Once delivery?

Many developers assume messaging systems guarantee:

```text id="r7y9km"
Exactly Once Delivery
```

In reality, achieving exactly-once delivery in distributed systems is extremely difficult.

Most production systems use:

```text id="x4hj9v"
At-Least-Once Delivery
+
Idempotent Consumers
```

to achieve practical reliability.

---

# Message Delivery Guarantees

Messaging systems generally provide three delivery models:

```text id="a6zt1w"
At-Most-Once

At-Least-Once

Exactly-Once
```

Each has different trade-offs.

---

# At-Most-Once Delivery

## Definition

A message is delivered:

```text id="qv0rhc"
Zero or One Time
```

Meaning:

* Message may be delivered
* Message may be lost
* No duplicates

---

## Flow

```text id="3mp8wo"
Producer
    |
    v
RabbitMQ
    |
    v
Consumer
```

If consumer crashes:

```text id="d6yl6u"
Message Lost
```

---

## Example

Consumer receives:

```text id="f1e8nb"
OrderCreatedEvent
```

RabbitMQ immediately removes it.

Before processing:

```text id="w7i2sv"
Consumer Crashes
```

Result:

```text id="1m6xmt"
Message Lost Forever
```

---

## Advantages

* Simple
* Fast
* No Duplicates

---

## Disadvantages

* Message Loss Possible
* Not Suitable for Critical Systems

---

## Use Cases

```text id="3v7n2q"
Metrics

Logs

Analytics Data
```

where occasional loss is acceptable.

---

# At-Least-Once Delivery

## Definition

A message is delivered:

```text id="yk4zxl"
One or More Times
```

Meaning:

* No message loss
* Duplicate messages possible

---

## RabbitMQ Delivery Model

RabbitMQ primarily provides:

```text id="clw7tz"
At-Least-Once Delivery
```

---

## Flow

```text id="9rxnkg"
RabbitMQ
    |
    v
Consumer
    |
Process Message
    |
ACK
```

Message removed only after ACK.

---

## Failure Scenario

Consumer receives:

```text id="z4fxu7"
OrderCreatedEvent
```

Processes successfully.

Before ACK reaches RabbitMQ:

```text id="1e9xtr"
Network Failure
```

RabbitMQ assumes:

```text id="8n8yfr"
Message Not Processed
```

and redelivers.

---

## Result

```text id="m4y3zn"
Same Message
Delivered Again
```

Duplicate processing becomes possible.

---

## Advantages

* No Message Loss
* High Reliability
* Production Friendly

---

## Disadvantages

* Duplicate Messages Possible

---

## Use Cases

```text id="yn4h5q"
Orders

Payments

Inventory

Notifications
```

---

# Exactly-Once Delivery

## Definition

A message is delivered:

```text id="g6k8ym"
Exactly One Time
```

Meaning:

* No Message Loss
* No Duplicates

Ideal but difficult.

---

# Why Is Exactly-Once Hard?

Distributed systems have many failure points.

Example:

```text id="f1r2de"
Producer
   |
Network
   |
RabbitMQ
   |
Consumer
```

Failures can happen anywhere.

---

# Example

Consumer processes payment.

```text id="4lnvzi"
Charge Customer
```

Success.

Before ACK:

```text id="0rxj0u"
Consumer Crashes
```

RabbitMQ redelivers.

Question:

```text id="j5n8xu"
Was Payment Already Processed?
```

Hard to determine.

---

# The Distributed Systems Problem

Two operations occur:

```text id="mnwdxw"
Business Transaction

Message ACK
```

Both must succeed together.

---

## Failure Scenario

```text id="mns2jk"
Payment Success
```

but

```text id="afhr9m"
ACK Failure
```

RabbitMQ redelivers.

Result:

```text id="fdjjrx"
Duplicate Processing Risk
```

---

# Why Most Systems Avoid Exactly-Once

Achieving exactly-once often requires:

* Distributed Transactions
* Two-Phase Commit
* Complex Coordination
* Performance Trade-Offs

Most companies avoid this complexity.

---

# Real-World Strategy

Most companies use:

```text id="jz0ylg"
At-Least-Once Delivery
```

combined with:

```text id="0r97jb"
Idempotent Consumers
```

---

# Example

Message:

```text id="7vmqj2"
Order #101
```

delivered twice.

Consumer checks:

```text id="nt5z3q"
Already Processed?
```

If yes:

```text id="7a6d2o"
Ignore Duplicate
```

Business result remains correct.

---

# RabbitMQ Delivery Guarantee

RabbitMQ generally supports:

```text id="zdr0ho"
At-Least-Once Delivery
```

when using:

* Durable Queues
* Persistent Messages
* Manual ACK

---

# RabbitMQ Example

Consumer:

```text id="l1w8vb"
Receive Message
Process Message
ACK
```

If consumer crashes:

```text id="hj4f0x"
RabbitMQ Redelivers
```

No message loss.

---

# Kafka and Delivery Guarantees

Interviewers often compare RabbitMQ and Kafka.

---

## RabbitMQ

Default model:

```text id="4xvb0o"
At-Least-Once
```

---

## Kafka

Supports:

```text id="ajz2jw"
At-Most-Once

At-Least-Once

Exactly-Once Semantics
```

with additional configuration.

---

# Ecommerce Example

Our Ecommerce Event-Driven System:

```text id="q1n7xm"
Order Service
      |
      v
RabbitMQ
      |
      +------------+
      |            |
      v            v
Inventory     Payment
```

---

## Duplicate Scenario

RabbitMQ delivers:

```text id="g5ytxj"
OrderCreatedEvent
```

twice.

Without idempotency:

```text id="h9c6vq"
Inventory Reduced Twice

Payment Charged Twice
```

---

## Correct Design

Consumer stores:

```text id="d7x0kp"
OrderId
```

or

```text id="v9m2ts"
EventId
```

If duplicate:

```text id="x3g7vy"
Skip Processing
```

---

# Delivery Models Comparison

| Feature      | At-Most-Once | At-Least-Once    | Exactly-Once        |
| ------------ | ------------ | ---------------- | ------------------- |
| Message Loss | Possible     | No               | No                  |
| Duplicates   | No           | Possible         | No                  |
| Reliability  | Low          | High             | Very High           |
| Complexity   | Low          | Medium           | Very High           |
| Performance  | Fast         | Good             | Slower              |
| Common Usage | Analytics    | Business Systems | Specialized Systems |

---

# Banking Example

## At-Most-Once

```text id="q0r1bx"
Transfer Event Lost
```

Unacceptable.

---

## At-Least-Once

```text id="9zqvkb"
Transfer Event Delivered Again
```

Requires idempotency.

---

## Exactly-Once

Ideal but expensive to implement.

---

# Notification Example

Email:

```text id="9x8qoh"
Welcome Email
```

Duplicate email:

```text id="r2hm7w"
Acceptable
```

Message loss:

```text id="4n4v7t"
Less Critical
```

At-Most-Once may be sufficient.

---

# Payment Example

Payment:

```text id="v4wrn9"
₹1000 Charge
```

Duplicate charge:

```text id="zv6bq8"
Not Acceptable
```

Use:

```text id="f6o7lu"
At-Least-Once
+
Idempotency
```

---

# Common Interview Questions

## What delivery guarantee does RabbitMQ provide?

RabbitMQ primarily provides:

```text id="b4m6zo"
At-Least-Once Delivery
```

---

## Can RabbitMQ deliver duplicate messages?

Yes.

Examples:

* Consumer Crash
* Missing ACK
* Network Failure
* Retry

---

## Why is Exactly-Once difficult?

Distributed systems can fail between:

```text id="l3vn7x"
Business Processing

ACK Processing
```

making coordination extremely difficult.

---

## How do companies handle duplicates?

Using:

```text id="k5x2gu"
Idempotent Consumers
```

---

## Which model is most common?

```text id="ux6g0z"
At-Least-Once Delivery
```

with

```text id="w9l8vy"
Idempotency
```

---

# Real Interview Answer

"RabbitMQ primarily provides at-least-once delivery, meaning messages are guaranteed not to be lost but may be delivered more than once. Exactly-once delivery is difficult in distributed systems because failures can occur between business processing and acknowledgements. In production systems, the common approach is to use at-least-once delivery combined with idempotent consumers to safely handle duplicate messages."

---

# Key Interview Takeaway

If an interviewer asks:

> How do real companies achieve exactly-once processing?

A strong answer is:

> "Most companies do not rely on true exactly-once delivery. Instead, they use at-least-once delivery with idempotent consumers, unique event IDs, database checks, and deduplication mechanisms to achieve exactly-once business outcomes."

---

# Summary

Message delivery guarantees are fundamental to distributed systems.

Key Concepts:

* At-Most-Once Delivery
* At-Least-Once Delivery
* Exactly-Once Delivery
* Duplicate Messages
* Idempotency
* Reliability Trade-Offs

Understanding these guarantees is essential for designing reliable RabbitMQ-based systems and answering backend, microservices, and system design interview questions.
