# Ecommerce Event-Driven System

A Spring Boot microservices project demonstrating asynchronous communication using RabbitMQ and event-driven architecture.

## Overview

This project simulates a simplified ecommerce order processing workflow using multiple independent microservices communicating through RabbitMQ events.

The goal is to demonstrate how real-world distributed systems can be decoupled using asynchronous messaging instead of direct service-to-service communication.

---

## Architecture

The system consists of four microservices:

### Order Service

Responsible for accepting new orders and publishing an `OrderCreatedEvent`.

### Inventory Service

Consumes the `OrderCreatedEvent` and reserves inventory for the ordered product.

### Payment Service

Consumes the `OrderCreatedEvent`, processes payment, and publishes a `PaymentCompletedEvent`.

### Notification Service

Consumes the `PaymentCompletedEvent` and sends customer notifications.

---

## Event Flow

```text
Order Service
      |
      | OrderCreatedEvent
      v

RabbitMQ
      |
      +-------------------+
      |                   |
      v                   v

Inventory Service    Payment Service
                           |
                           | PaymentCompletedEvent
                           v

                     RabbitMQ
                           |
                           v

                  Notification Service
```

---

## Workflow

### Step 1

A client places an order.

```http
POST /orders
```

Example Request:

```json
{
  "orderId": 1,
  "productName": "Laptop",
  "quantity": 1,
  "amount": 75000
}
```

---

### Step 2

Order Service publishes:

```text
OrderCreatedEvent
```

---

### Step 3

Inventory Service receives the event and reserves inventory.

```text
Inventory Reserved Successfully
```

---

### Step 4

Payment Service receives the same event and processes payment.

```text
Payment Processed Successfully
```

---

### Step 5

Payment Service publishes:

```text
PaymentCompletedEvent
```

---

### Step 6

Notification Service receives the event and sends notifications.

```text
EMAIL SENT
SMS SENT
ORDER CONFIRMATION SENT
```

---

## RabbitMQ Components

### Exchanges

```text
order.exchange
payment.exchange
```

### Queues

```text
inventory.queue
payment.queue
payment.completed.queue
```

### Routing Keys

```text
order.created
payment.completed
```

---

## Technologies Used

* Java 21
* Spring Boot 3
* RabbitMQ
* Spring AMQP
* Maven
* Lombok

---

## Project Structure

```text
ecommerce-event-driven-system
│
├── order-service
│
├── inventory-service
│
├── payment-service
│
└── notification-service
```

---

## Key Concepts Implemented

### Event-Driven Architecture

Services communicate through events instead of direct API calls.

### Publish-Subscribe Pattern

A single event can be consumed by multiple services.

### Asynchronous Messaging

Services remain loosely coupled and independently scalable.

### RabbitMQ Exchanges

Events are routed through exchanges using routing keys.

### RabbitMQ Queues

Each service consumes messages from its dedicated queue.

### Message Routing

RabbitMQ delivers messages based on exchange bindings and routing keys.

### JSON Event Serialization

Events are exchanged as JSON payloads between microservices.

---

## Sample Execution Flow

```text
ORDER CREATED EVENT PUBLISHED

↓

ORDER RECEIVED
Inventory Reserved Successfully

↓

PAYMENT RECEIVED
Payment Processed Successfully

↓

PAYMENT COMPLETED EVENT PUBLISHED

↓

NOTIFICATION RECEIVED

EMAIL SENT
SMS SENT
ORDER CONFIRMATION SENT
```

---

## Learning Outcomes

This project demonstrates:

* Microservices communication using RabbitMQ
* Event-driven system design
* Exchange and Queue configuration
* Routing Key based message delivery
* Multiple consumers for a single event
* Event chaining between services
* Spring Boot integration with RabbitMQ
* Real-world asynchronous workflow implementation

---

## Future Enhancements

Potential improvements:

* Retry Mechanism
* Dead Letter Queue (DLQ)
* Dead Letter Exchange (DLX)
* Publisher Confirms
* Message Persistence
* Manual Acknowledgements
* Docker Compose Setup
* Monitoring with Prometheus and Grafana
* Centralized Event Contracts Module

---

## License

This project is licensed under the MIT License.
