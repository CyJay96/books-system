<h1 align="center">Books Management System RESTful Web Service</h1>

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

---

### :zap: **Description**

This is a RESTful web service built with Spring Boot, Java, Gradle and PostgreSQL,
which provides functionality for managing libraries and books. The service includes support
for creating, updating and deleting libraries, books and users. Contains a composition of
"Library" and "Book" objects connected by a one-to-many relationship.

---

### :white_check_mark: **The stack of technologies used**
:star: **API Technologies:**
- SOLID
- OOP
- DI
- REST

:desktop_computer: **Backend technologies:**
- Java 17
- Spring Boot 3
- MapStruct
- Lombok
- OpenApi documentation

:hammer_and_wrench: **Build Tool:**
- Gradle 7.6

:floppy_disk: **DataBase:**
- PostgreSQL
- LiquiBase Migration

:heavy_check_mark: **Testing:**
- Junit 5
- Mockito
- TestContainers

:whale: **Containerization:**
- Docker

:safety_pin: **Dependencies used**
- `spring-boot-starter-web` - Starter of Spring web uses Spring MVC, REST and Tomcat as a default embedded server.  
  This dependency transitively pulls in all dependencies related to web development
- `spring-boot-starter-validation` - Add a dependency to a compatible version of hibernate validator.  
  Defines constraints to the fields of a class by annotating them with certain annotations.
- `spring-boot-starter-data-jpa` - Spring Data JPA handles most of the complexity of JDBC-based database access and ORM.
- `postgresql` - This dependency provides connection and work with the PostgreSQL database.
- `liquibase-core` - Enables database migrations to track, manage, and apply database schema changes.
- `springdoc-openapi-starter-webmvc-ui` - Provides an API specification encoded in a JSON or YAML document with the Swagger UI interface.
- `lombok` - Minimizes or removes the boilerplate Java code.
- `mapstruct` - Code generator that greatly simplifies the implementation of mapping between Java bean types.
- `testcontainers:postgresql` - Allows to run isolated tests in a Docker container with PostgreSQL database support.
- `spring-boot-starter-test` - The primary starter dependency for testing Spring Boot application.  
   It contains the majority of libraries that are required for tests including JUnit Jupiter and Mockito.

---

### :rocket: **Get Started**

**Build with Gradle**

Build application:

    $ ./gradlew clean build

If in case face any issues while building application because of test cases, then try build with disabling test cases:

    $ ./gradlew clean build -x test

Run Application using Docker Compose:

    $ docker-compose up

Browse OpenAPI Documentation:  
`http://localhost:8080/swagger-ui.html`  
`http://localhost:8080/v3/api-docs`  


---

### :pushpin: **API Endpoints**

**Users**

| **HTTP METHOD** |             **ENDPOINT**              |      **DESCRIPTION**      |
|:---------------:|:-------------------------------------:|:-------------------------:|
|    **POST**     |            `/api/v0/users`            |       Save new User       |
|     **GET**     |            `/api/v0/users`            |      Find all Users       |
|     **GET**     |         `/api/v0/users/{id}`          |      Find User by ID      |
|     **PUT**     |         `/api/v0/users/{id}`          |     Update User by ID     |
|    **PATCH**    |         `/api/v0/users/{id}`          | Partial Update User by ID |
|   **DELETE**    |         `/api/v0/users/{id}`          |     Delete User by ID     |

**Libraries**

| **HTTP METHOD** |                    **ENDPOINT**                     |           **DESCRIPTION**           |
|:---------------:|:---------------------------------------------------:|:-----------------------------------:|
|    **POST**     |                 `/api/v0/libraries`                 |          Save new Library           |
|     **GET**     |                 `/api/v0/libraries`                 |         Find all Libraries          |
|     **GET**     |              `/api/v0/libraries/{id}`               |         Find Library by ID          |
|     **PUT**     |              `/api/v0/libraries/{id}`               |        Update Library by ID         |
|    **PATCH**    |              `/api/v0/libraries/{id}`               |    Partial Update Library by ID     |
|    **PATCH**    |  `/api/v0/libraries/addUser/{libraryId}/{userId}`   |   Add User to Library by User ID    |
|    **PATCH**    | `/api/v0/libraries/deleteUser/{libraryId}/{userId}` | Delete User from Library by User ID |
|   **DELETE**    |              `/api/v0/libraries/{id}`               |        Delete Library by ID         |

**Books**

| **HTTP METHOD** |        **ENDPOINT**         |      **DESCRIPTION**      |
|:---------------:|:---------------------------:|:-------------------------:|
|    **POST**     | `/api/v0/books/{libraryId}` |       Save new Book       |
|     **GET**     |       `/api/v0/books`       |      Find all Books       |
|     **GET**     |    `/api/v0/books/{id}`     |      Find Book by ID      |
|     **PUT**     |    `/api/v0/books/{id}`     |     Update Book by ID     |
|    **PATCH**    |    `/api/v0/books/{id}`     | Partial Update Book by ID |
|   **DELETE**    |    `/api/v0/books/{id}`     |     Delete Book by ID     |
