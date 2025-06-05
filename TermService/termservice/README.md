# TermService

## Overview

The **TermService** manages the scheduling and structure of event sessions. Each session (called a *term*) represents a specific occurrence of an event, including date, time, location, participant limits, and assigned caregivers.

---

## Technologies

- **Language**: Java

- **Framework**: Micronaut

- **Database**: PostgreSQL

- **Messaging**: Apache Kafka

Micronaut was chosen for its fast startup time, low memory footprint, and modularity. Kafka is used for asynchronous event communication.

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
- All endpoints use lowercase and kebab-case.
- Resource identifiers (e.g. termId, eventId) are passed as path parameters.
- JSON keys use camelCase notation.
- Base path: /terms

### Create Term

**POST /terms/create**

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
- 200 OK – Term successfully created, event sent
- 400 Bad Request – Invalid input (e.g., constraint violations)

**Example (cURL)**


---

### Get Term

**GET /terms/{termId}**

Retrieves a specific term by ID.

**Response:**
- 200 OK – Returns the term
- 404 Not Found – Term not found

---

### Get Terms by Event

**GET /terms/{eventId}**

Retrieves all terms associated with a specific event.

**Response:**

- 200 OK – Returns a list of terms
- 404 Not Found – No terms found for this event



### Update Term

**PUT /terms/update/{termId}**

Updates an existing term.

**Request Body:** Same structure as in Create Term.

**Response:**
- 200 OK – Term updated and event sent
- 404 Not Found – Term not found
- 400 Bad Request – Invalid input

---

### Delete Term

**DELETE /terms/delete/{termId}**

Deletes a term (only possible if no pending payments exist).

**Response:**
- 200 OK – Termin gelöscht
- 400 Bad Request – Löschen nicht möglich
- 404 Not Found – Termin nicht gefunden

---

### Assign Caregiver
**POST /terms/assign-caregiver/{termId}/{caregiverId}**

Assigns a caregiver to a term.

Response:

- 200 OK – Caregiver assigned and event sent
- 400 Bad Request – Already assigned or invalid caregiver
- 404 Not Found – Term not found

---

### Unassign Caregiver
**POST /terms/unassign-caregiver/{termId}/{caregiverId}**

Removes a caregiver from a term.

Response:

- 200 OK – Caregiver removed and event sent
- 404 Not Found – Term not found

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

- Unit tests for the use case assigning caregiver:
  - Caregiver is successfully assigned
  - Duplicate assignment throws DuplicateAssignmentException 
  - Non-existent term throws TermNotFoundException 
  - Kafka producer is called as expected 
  - Kafka send failures do not prevent the assignment

- Controller test for assignCaregiver with mocked Kafka producer

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

