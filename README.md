# Holiday Program Platform

## Team Members
- Ammar Eltayeb Elbashir Ahmedtaha
- Nico Knünz
- Umut Caglayan
- Yagmur Sagdic

## Services
- AccountingService
- TermService

## Repository Links
- [AccountingService Repository](https://github.com/yagmursagdic/holiday-program-platform/tree/main/AccountingService)
- [TermService Repository](https://github.com/yagmursagdic/holiday-program-platform/tree/main/TermService)

## Technology Considerations
- **Programming Language**: Java
- **Framework**: Micronaut
- **Database**: PostgreSQL
- **Messaging**: Apache Kafka

### Why This Technology?
We chose Java for its robustness and Micronaut for its lightweight microservice capabilities. PostgreSQL is a reliable relational database, and Kafka will facilitate asynchronous communication between our services.

## Current Progress
- Created the project structure for both microservices.
- Set up basic configurations for PostgreSQL and Kafka.
- Implemented Docker setup for containerization.
- Prepared initial `README.md` with team information and service details.
- Implemented Controller and Entity Class for each service.
- Developed initial REST endpoints for basic operations (e.g., "Hello from AccountingService").

## How to Build and Run
1. **Clone the repository**
```bash
   git clone https://github.com/yagmursagdic/holiday-program-platform.git
   cd holiday-program-platform
```
2. **Prerequisites**

   Before you can build and run the application, ensure you have the following installed on your machine:

- **Docker**: Make sure Docker is installed and running.
- **Docker Compose**: This is usually included with Docker Desktop, but you can also install it separately if needed. 
- **Java Development Kit (JDK)**: Ensure you have JDK 11 or higher installed if you plan to build the project locally. 

3. **Build the Project**

   If you want to build the project locally (optional), you can use the following command:

```bash
   ./gradlew build
```

This command will compile the code and run tests. Ensure that you have Gradle installed, or use the Gradle wrapper included in the project.

4. **Start the Services**

   To start the application and its dependencies (like the PostgreSQL database), use Docker Compose. Run the following command in the root directory of the project:

```bash
  docker-compose up --build
```

This command will: Build the Docker images for your services and start the containers defined in the docker-compose.yml file.

5. **Access the Application**

Once the services are up and running, you can access the application in your web browser. 

6. **Stopping the Services**

To stop the running services, you can use the following command in the terminal where Docker Compose is running:

```bash
docker-compose down
```
This command will stop and remove the containers, networks, and volumes defined in the docker-compose.yml file.