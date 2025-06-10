# TermService

## Overview

The **TermService** manages scheduled event sessions, called terms. A term represents a specific occurrence of an event and includes details such as date, time, location, participant limits, and assigned caregivers.

---

## Technologies

- **Language**: Java

- **Framework**: Micronaut

- **Database**: PostgreSQL

- **Messaging**: Apache Kafka

Micronaut is chosen for its fast startup time, low memory footprint, and excellent support for microservice architecture.
Kafka is used for asynchronous, event-driven communication.

---

## Build & Run

### Prerequisites

- Java 11+
- Docker + Docker Compose
- PostgreSQL
- Apache Kafka

### Local Build

```
cd termservice
./gradlew build
```

---

## REST API Endpoints

### Naming Convention 
- All paths use lowercase and kebab-case.
- Resource identifiers (e.g. termId, eventId) are passed as path parameters.
- JSON uses camelCase notation.
- Base path: /terms

### Create Term

**POST /terms/**

Creates a new event session.

**Request Body:**
```
{
  "eventId": "event123",
  "startTime": "2025-08-01T10:00:00",
  "endTime": "2025-08-01T12:00:00",
  "location": "City Hall",
  "meetingPoint": "Main Entrance",
  "minParticipants": 5,
  "maxParticipants": 20,
  "price": 15.0,
  "caregiverIds": ["cg001", "cg002"]
}
```


**Response:**
- 201 Created – Term successfully created, event sent
- 500 Internal Server Error – Failed to persist or send event

---

### Get Term

**GET /terms/{termId}**

Retrieves a specific term by ID.

**Response:**
- 200 OK – Returns the term
- 404 Not Found – Term not found
- 500 Internal Server Error – Retrieval failure

---

### Get Terms by Event

**GET /terms/by-event/{eventId}**

Retrieves all terms associated with a specific event.

**Response:**

- 200 OK – Returns a list of terms
- 500 Internal Server Error – Retrieval failure



### Update Term

**PUT /terms/{termId}**

Updates an existing term.

**Request Body:** Same structure as in Create Term.

**Response:**
- 200 OK – Term updated and event sent
- 404 Not Found – Term not found
- 500 Internal Server Error – Retrieval failure

---

### Delete Term

**DELETE /terms/{termId}**

Deletes a term (only possible if no pending payments exist).

**Response:**
- 200 OK – Term deleted and event sent
- 404 Not Found – Term not found
- 500 Internal Server Error – Retrieval failure

---

### Assign Caregiver
**POST /terms/assign-caregiver/{termId}/{caregiverId}**

Assigns a caregiver to a term.

Response:

```
{
  "termId": "abc123",
  "caregiverId": "cg001",
  "status": "assigned"
}
```

- 200 OK – Caregiver assigned and event sent
- 409 Conflict – Already assigned
- 404 Not Found – Term not found
- 500 Internal Server Error – Assignment failed

---

### Unassign Caregiver
**POST /terms/unassign-caregiver/{termId}/{caregiverId}**

Removes a caregiver from a term.

Response:
```
{
  "termId": "abc123",
  "caregiverId": "cg001",
  "status": "unassigned"
}
```

- 200 OK – Caregiver removed and event sent
- 404 Not Found – Term not found
- 500 Internal Server Error – Unassignment failed

---

## Kafka Events

### Produced Topics & Payloads

#### term.created
``` 
{
  "termId": "abc123",
  "eventId": "event456",
  "startTime": "2025-08-01T10:00:00",
  "endTime": "2025-08-01T12:00:00",
  "location": "City Hall",
  "meetingPoint": "Main Entrance",
  "minParticipants": 5,
  "maxParticipants": 20,
  "price": 15.0,
  "caregivers": ["cg001", "cg002"]
}
```

#### term.updated
``` 
{
  "termId": "abc123",
  "eventId": "event456",
  "startTime": "2025-08-01T10:00:00",
  "endTime": "2025-08-01T12:00:00",
  "location": "City Hall",
  "meetingPoint": "Main Entrance",
  "minParticipants": 5,
  "maxParticipants": 20,
  "price": 15.0,
  "caregivers": ["cg001", "cg002"]
}
```

#### term.deleted
```
{
"termId": "abc123",
}
```


#### term.caregiver.assigned
```
{
"termId": "abc123",
"caregiverId": "cg001"
}
```

#### term.caregiver.unassigned
```
{
"termId": "abc123",
"caregiverId": "cg001"
}
```

---

## Use Case: Assigning Caregivers

- The event owner selects a caregiver for a specific term.
- The system checks whether the term exists.
- It verifies whether the caregiver is already assigned.
- If not, the caregiver is added and the term is updated.
- A Kafka event term.caregiver.assigned is produced.

---

## Tests

- Unit Tests – TermCommandService
  - Successfully assigns a caregiver
  - Duplicate caregiver assignment → throws DuplicateAssignmentException
  - Non-existent term → throws TermNotFoundException
  - Kafka producer is invoked as expected
  - Kafka send exception is handled – exception occurs but logic executes up to that point

- Controller Test – TermController 
  - Assigning caregiver to non-existent term → returns HTTP 404 Not Found with appropriate error message

---

## Design Notes

Architecture:
The project uses the Micronaut framework for dependency injection, aspect-oriented programming, and microservice communication.

Service Layer:
The TermCommandService handles all business logic related to terms (creating, updating, deleting, and assigning caregivers). It interacts with the repository for database operations.

Repository:
TermRepository abstracts the CRUD operations for terms. In tests, it is mocked using Mockito.

Domain Model:
The Term entity represents a term, including fields like termId, caregiverIds, start/end times, and location.

Event Handling:
Kafka producer is invoked in the controller to emit events (e.g., when a caregiver is assigned).

Testing Strategy:
Unit tests use JUnit 5 and Mockito, focusing on service logic, error handling, and event triggering.

Error Handling:
Custom exceptions (TermNotFoundException, DuplicateAssignmentException) are used for clear error management.

Configuration:
Environment variables (like DATABASE_PASSWORD) are injected via Micronaut's property resolver and must be set for both runtime and tests.

Special Notes:
Reflection is used in tests to set private fields such as termId.

---

