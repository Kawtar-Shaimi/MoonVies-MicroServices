# MoonVies Microservices Backend

MoonVies is a modern backend video streaming platform built with a microservices architecture using Spring Boot and Spring Cloud. The system manages users, video contents, watchlists, and watch histories via a set of robust REST APIs.

## Architecture
The platform is composed of the following main services:
- **`config-service` (Port 8888)**: Centralized configuration management using Spring Cloud Config, backed by a local Git repository (`moonvies-config-repo`).
- **`discovery-service` (Port 8761)**: Netflix Eureka server for service registration and discovery.
- **`gateway-service` (Port 8080)**: Spring Cloud Gateway acting as the single entry point for all client requests.
- **`video-service` (Port 8081)**: Manages video contents (CRUD). Connected to its own MySQL database (`video_db`).
- **`user-service` (Port 8082)**: Manages users, watchlists, and watch history. Connected to its own MySQL database (`user_db`). Communicates with `video-service` via OpenFeign.

## Technologies Used
- Java 17
- Spring Boot 3.2.4
- Spring Cloud (Eureka, Config, Gateway, OpenFeign)
- Spring Data JPA
- MySQL (Dockerized)
- Lombok, MapStruct (or manual mapping)
- Docker & Docker Compose
- JUnit & Mockito for Unit Testing
- Postman for API Testing

## Prerequisites
- Java 17+ installed
- Maven installed
- Docker and Docker Compose installed
- Git installed (for configuration repository)

## How to Run the Application

### 1. Start the Databases
In the root directory of the project, run:
```bash
docker-compose up -d
```
This will start two MySQL containers (`video_db` on port 3306, and `user_db` on port 3307).

### 2. Run the Microservices
You must run the services in a specific order to ensure they can find each other and get their configurations:

1. **Config Service**:
   ```bash
   cd config-service
   mvn spring-boot:run
   ```
2. **Discovery Service (Eureka)**:
   ```bash
   cd discovery-service
   mvn spring-boot:run
   ```
3. **Video Service**:
   ```bash
   cd video-service
   mvn spring-boot:run
   ```
4. **User Service**:
   ```bash
   cd user-service
   mvn spring-boot:run
   ```
5. **Gateway Service**:
   ```bash
   cd gateway-service
   mvn spring-boot:run
   ```

### 3. Testing the APIs
Import the provided `MoonVies_Postman_Collection.json` file into Postman. All requests in the collection go through the API Gateway on `http://localhost:8080`.
Remember to create a video first, then create a user, before trying to add a video to a user's watchlist using their respective IDs!

## Testing (Unit Tests)
You can run the unit tests for each service using Maven:
```bash
cd video-service
mvn test

cd ../user-service
mvn test
```

## Agile Methodology
This project was developed iteratively, with features broken down into small, logical commits to simulate an Agile development workflow.
