# Bulletin URL Shortener Service

This RESTful API allows users to create short URLs for target URLs, redirect to the corresponding
target URL for a short URL, and retrieve a paged list of existing short URLs. This service is
written in Java using the Spring Boot framework, uses H2 as an in-memory database, and maven for
project and dependency management.

## How to Run This API Locally

1. Clone this repository

### How To Run This Project in IntelliJ (Recommended)

2. Open IntelliJ and open the cloned repository in Intellij.
3. Wait for IntelliJ to finish indexing the project.
4. IntelliJ should be able to automatically tell that this project is a Spring Boot project and
   create a run configuration automatically. In should also be able to tell that the project uses
   Maven and auto-import the dependencies. If IntelliJ does not auto-import the dependencies, there
   may be a small icon on the right side of the editor that you wil need to click to import the
   dependencies.
5. To run the project in IntelliJ, you can either press `ctrl + R` on your keyboard, or you can
   click the small green play icon in near the top right of the Intellij window

### How to Run this Project in the Terminal

2. Make sure you have Maven installed on your machine and have added it to your environment
   variable. Instructions for this can be found [here](https://maven.apache.org/install.html).
3. Inside the terminal, navigate to the root directory of this project.
4. In the root directory of this project inside the terminal, run the command `mvn spring-boot:run`.

## API Routes (While Running Locally)

This API has 3 routes, and the routes below assume you are running this API locally on the default
port 8080. To view the OpenAPI documentation in Swagger UI, run this API locally on port 8080 and
click [here](http://localhost:8080/api/swagger-ui/index.html?configUrl=%2Fapi%2Fv3%2Fapi-docs%2Fswagger-config)
.

#### GET http://localhost:8080/api/short-urls

This route will return a paged list of all existing short URLs currently stored in the H2 in-memory
database. This route allows the requester to set the page number, page size, and sort by fields as
query parameters. More details about this can be found in the Swagger UI.

#### GET http://localhost:8080/api/short-urls/{shortUrlId}

This route will redirect the requester to the target URL that corresponds to the short URL ID path
variable. It is important to note that this route WILL NOT redirect if called through Swagger UI,
and should ideally be entered into a browser's address bar directly in order for redirecting to
work.

#### POST http://localhost:8080/api/short-urls

This route will either create a new short URL for the given target URL, or it will return the
existing short URL for the given target URL if it already exists.

It is recommended to view the OpenAPI documentation in Swagger UI for this API if you want to find
more details about 3 routes ,such as request and response bodies. Swagger UI also makes it easy and
convenient to send requests to the API.

## Project Code Standards

- This code style for this project mostly follows
  the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). For criteria
  not explicitly listed in the guide, I used my best judgement to style to code to make it as
  readable as I could, ex: adding empty lines to separate code blocks into logical units of work.
- This project also uses code suggestions from [SonarLint](https://www.sonarlint.org/).

## Major Dependencies Used in This Project

- Spring Boot Starter Data JPA - Makes it easy and convenient to access and persist data between
  Java objects and a relational database (H2).
- Spring Boot Starter Web - Makes it easy and convenient to run this API locally with the embedded
  Tomcat server.
- Spring Boot Starter Validation - Makes it easy and convenient to add request validation.
- Spring Doc OpenAPI - Used to create the OpenAPI documentation for this API.
- H2 - In-memory relational database management system used by this API.
- Lombok - Dynamically generates a lot of the repetitive, boilerplate code such as getters, setters,
  etc.
- ModelMapper - Used to map Java objects to each other.
  


