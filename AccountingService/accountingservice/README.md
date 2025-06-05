# AccountingService

## Overview

The **AccountingService** handles user payments, tracks payment statuses, and notifies users about pending or received payments through Kafka events.

---

## Technologies Used

- **Language:** Java
- **Framework:** Micronaut
- **Database:** PostgreSQL
- **Messaging:** Apache Kafka

Kafka is used to asynchronously notify other services of payment status changes.

---

## How to Build and Run

### Prerequisites

- Java 11+
- Docker + Docker Compose
- PostgreSQL
- Apache Kafka

### Local Build

```
cd accountingservice
./gradlew build
```
---

## REST API Endpoints
### Create Payment
**POST /payments**

Creates a user payment entry.

**Request Body:**
```
{
  "termId": "term123",
  "userId": "user456",
  "amount": 25.00,
  "paymentDeadline": "2025-06-15"
}
```

**Response:**

- 201 Created – Payment entry created
- 400 Bad Request – Duplicate or invalid
---

### Get Payments by Term
**GET /payments/term/{termId}**

Returns all payments for a term.

**Response:**

- 200 OK – List of payments
---

### Get Payments by User
**GET /payments/user/{userId}**

Returns all payments for a user.
---

### Update Payment
**PATCH /payments/{paymentId}**

Updates an existing payment.

**Request Body:**
```
{
  "amount": 30.00,
  "paymentDeadline": "2025-06-20"
}
```
---

### Delete Payment
**DELETE /payments/{paymentId}**

Deletes a payment entry.

**Response:**

- 200 OK – Deleted
- 400 Bad Request – Cannot delete
- 404 Not Found – Payment not found
---

### Get Payment Status
**GET /payments/status/{userId}/{termId}**

Returns the current payment status of a user for a specific term.
---

## Kafka Events

### Produced Topics & Payloads

#### payment.received
```
{
  "termId": "term123",
  "userId": "user456",
  "amount": 25.00,
  "date": "2025-06-01"
}
```

#### payment.pending
```
{
  "termId": "term123",
  "userId": "user456",
  "amount": 25.00,
  "paymentDeadline": "2025-06-15"
}
```
---

## Use Case

---

## Tests

---

## Design Notes


---


## Micronaut 4.8.2 Documentation

- [User Guide](https://docs.micronaut.io/4.8.2/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.8.2/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.8.2/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Shadow Gradle Plugin](https://gradleup.com/shadow/)
- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
## Feature test-resources documentation

- [Micronaut Test Resources documentation](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/)


## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature kafka documentation

- [Micronaut Kafka Messaging documentation](https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html)


