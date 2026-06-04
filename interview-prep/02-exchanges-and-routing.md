# Exchanges and Routing Interview Questions

## 1. What is an Exchange in RabbitMQ?

An Exchange is responsible for receiving messages from producers and routing them to one or more queues.

A producer never sends messages directly to a queue.

### Message Flow

```text
Producer
    |
    v
Exchange
    |
    v
Queue
```

The exchange decides where the message should go.

---

## 2. Why do we need an Exchange?

Without exchanges, producers would need to know every queue.

Example:

```text
Producer
   |
   +--> payment.queue
   |
   +--> inventory.queue
   |
   +--> notification.queue
```

This creates tight coupling.

With exchanges:

```text
Producer
    |
    v
order.exchange
   /     \
  /       \
inventory payment
```

The producer only knows the exchange.

---

## 3. What are the types of Exchanges in RabbitMQ?

RabbitMQ provides four exchange types:

```text
Direct Exchange
Fanout Exchange
Topic Exchange
Headers Exchange
```

Each routing strategy serves a different purpose.

---

## 4. What is a Direct Exchange?

A Direct Exchange routes messages using an exact routing key match.

### Example

Producer sends:

```text
Routing Key = order.created
```

Binding:

```text
payment.queue -> order.created
inventory.queue -> order.created
```

Result:

```text
Message delivered to both queues
```

---

## 5. How does Direct Exchange work?

### Exchange

```text
order.exchange
```

### Queues

```text
inventory.queue
payment.queue
```

### Routing Key

```text
order.created
```

Flow:

```text
Producer
   |
   | order.created
   v
Direct Exchange
   |
   +--> inventory.queue
   |
   +--> payment.queue
```

This is the exchange used in our Ecommerce Event Driven System.

---

## 6. When should Direct Exchange be used?

Use Direct Exchange when routing decisions are simple and based on exact matches.

Examples:

```text
order.created
order.cancelled
payment.completed
```

Best for:

* Ecommerce
* Order Processing
* Notifications
* Payment Events

---

## 7. What is a Fanout Exchange?

A Fanout Exchange broadcasts messages to all bound queues.

It ignores routing keys.

### Example

```text
Producer
    |
    v
Fanout Exchange
   /    |    \
  /     |     \
 Q1     Q2     Q3
```

Every queue receives a copy.

---

## 8. How does Fanout Exchange work?

Producer:

```text
Send Message
```

Exchange:

```text
logs.exchange
```

Queues:

```text
serviceA.queue
serviceB.queue
serviceC.queue
```

Result:

```text
All queues receive the message
```

---

## 9. When should Fanout Exchange be used?

Use Fanout Exchange when every consumer should receive the same message.

Examples:

```text
System Logs
Application Events
Notifications
Monitoring
Broadcast Messages
```

---

## 10. What is a Topic Exchange?

A Topic Exchange routes messages using pattern matching.

Wildcards are supported.

### Wildcards

```text
*  -> one word
#  -> zero or more words
```

---

## 11. Example of Topic Exchange

Routing Keys:

```text
order.created
order.cancelled
payment.completed
```

Bindings:

```text
order.*
```

Receives:

```text
order.created
order.cancelled
```

Does not receive:

```text
payment.completed
```

---

## 12. What does '*' mean in Topic Exchange?

Asterisk matches exactly one word.

Example:

```text
order.*
```

Matches:

```text
order.created
order.cancelled
order.updated
```

Does not match:

```text
order.payment.completed
```

---

## 13. What does '#' mean in Topic Exchange?

Hash matches zero or more words.

Example:

```text
order.#
```

Matches:

```text
order.created
order.payment.completed
order.updated.inventory
```

All are valid matches.

---

## 14. When should Topic Exchange be used?

Topic Exchange is ideal when routing rules are dynamic.

Examples:

```text
user.created
user.updated
user.deleted

order.created
order.cancelled

payment.completed
```

Large systems often use Topic Exchanges.

---

## 15. What is a Headers Exchange?

Headers Exchange routes messages based on message headers rather than routing keys.

Example:

```text
department = finance
region = india
```

The exchange checks header values before routing.

---

## 16. When should Headers Exchange be used?

Headers Exchange is useful when routing depends on metadata.

Examples:

```text
Country
Region
Department
Customer Type
Priority
```

However, it is less common than Direct or Topic Exchanges.

---

## 17. Which Exchange is used most frequently?

In real-world applications:

```text
1. Direct Exchange
2. Topic Exchange
3. Fanout Exchange
4. Headers Exchange
```

Most projects use Direct or Topic Exchanges.

---

## 18. What is a Binding?

A Binding connects:

```text
Exchange
    |
    v
Queue
```

The binding determines how messages are routed.

Example:

```text
payment.queue
     |
payment.completed
```

---

## 19. What is a Binding Key?

A Binding Key is associated with a queue binding.

Example:

```text
payment.completed
```

RabbitMQ compares:

```text
Routing Key
```

with

```text
Binding Key
```

to determine routing.

---

## 20. What is a Routing Key?

A Routing Key is a string attached to a message.

Example:

```text
order.created
payment.completed
notification.sent
```

The exchange uses the routing key to decide where the message should go.

---

## 21. Difference Between Routing Key and Binding Key

### Routing Key

Sent by producer.

Example:

```text
order.created
```

### Binding Key

Configured between queue and exchange.

Example:

```text
order.created
```

If they match, the message is routed.

---

## 22. Which Exchange Type Was Used In Our Project?

We used:

```text
Direct Exchange
```

Exchange:

```text
order.exchange
```

Routing Key:

```text
order.created
```

Queues:

```text
inventory.queue
payment.queue
```

Result:

```text
OrderCreatedEvent

      |
      v

order.exchange

      |
      +--> inventory.queue

      |
      +--> payment.queue
```

Both services receive the event.

---

## 23. Direct vs Fanout vs Topic Exchange

### Direct Exchange

```text
Exact Match
```

Best for:

```text
Order Processing
Payments
Inventory
```

---

### Fanout Exchange

```text
Broadcast
```

Best for:

```text
Logs
Notifications
Monitoring
```

---

### Topic Exchange

```text
Pattern Matching
```

Best for:

```text
Large Event Driven Systems
Microservices
```

---

## 24. Which Exchange Type Should I Choose?

### Direct

Simple routing.

### Fanout

Broadcasting.

### Topic

Flexible routing.

### Headers

Metadata-based routing.

In most microservice systems, Direct and Topic Exchanges are the preferred choices.

---

# Summary

RabbitMQ Exchanges are responsible for routing messages to queues.

The four exchange types are:

```text
Direct Exchange
Fanout Exchange
Topic Exchange
Headers Exchange
```

Understanding exchange types, bindings, and routing keys is essential for designing scalable event-driven systems.
