# URL Shortener Service

This RESTful API allows users to create short URLs for target URLs, redirect to the corresponding
target URL for a short URL, and retrieve a paged list of existing short URLs. This service is
written in Java 11 using the Spring Boot framework, uses H2 as an in-memory database, and uses Maven
for project and dependency management.

## How to Run This API Locally

1. Clone this repository.
2. Make sure you have Java 11 and Maven installed and setup on your machine. Instructions for
   installing Java 11 using Homebrew can be
   found [here](https://medium.com/w-logs/installing-java-11-on-macos-with-homebrew-7f73c1e9fadf).
   Instructions for installing Maven can be found [here](https://maven.apache.org/install.html).
   
### How to Run This Project in the Terminal

3. Inside the terminal, navigate to the root directory of this project.
4. In the root directory of this project inside the terminal, run the command `mvn spring-boot:run`.

### How to Run This Project in IntelliJ

3. Open IntelliJ and open the root directory of the cloned repository in IntelliJ.
4. Wait for IntelliJ to finish indexing the project and importing the dependencies.
5. Make sure you have a JDK selected for this project in IntelliJ. Instructions on how to do this can be found [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).
6. Navigate to the `src/main/java/co.bulletin.urlshortener` directory, right
   click the UrlShortenerApplication.class file and click `Run 'UrlShortenerApplication'`.
6. If IntelliJ automatically created a run configuration, or if you created it manually, you can
   either press `ctrl + R` on your keyboard to run the application, or you can click the small green
   play icon near the top right of the IntelliJ window.

## API Routes (While Running Locally)

This API has 3 routes, and the routes below assume you are running this API locally on the default
port 8080. To view the OpenAPI documentation in Swagger UI, run this API locally on port 8080 and
click [here](http://localhost:8080/api/swagger-ui/index.html?configUrl=%2Fapi%2Fv3%2Fapi-docs%2Fswagger-config)
. It is recommended to view the OpenAPI documentation in Swagger UI for this API if you want to find
more details about the 3 routes, such as request and response bodies. Swagger UI also makes it easy
and convenient to send requests to the API and receive responses.

#### GET http://localhost:8080/api/short-urls

This route will return a paged list of all existing short URLs currently stored in the H2 in-memory
database. This route allows the requester to set the page number, page size, and sort by fields as
query parameters. More details about this route can be found in the Swagger UI.
Ex: `http://localhost:8080/api/short-urls?page=1&size=5&sort=targetUrl,desc` will retrieve page 1 of
size 5 of the short URLs sorted by targetUrl in descending order, along with the total short URL
count and total page count corresponding to the size query parameter. The default value for the page
query parameter is 0, and the default value for the size query parameter is 20. If a sort query
parameter is specified, the default sort order is ascending, and multiple sort criteria are
supported.

#### GET http://localhost:8080/api/short-urls/{shortUrlId}

This route will redirect the requester to the target URL that corresponds to the short URL ID path
variable. This route accomplishes the redirect by setting the HTTP status of the response to 301,
and setting the `Location` header value in the response to the target URL. It is important to note
that this route **WILL NOT** redirect if called through Swagger UI, and should ideally be entered
into a browser's address bar directly in order for redirecting to work. Ex:
Entering `http://localhost:8080/api/short-urls/b` in a browser address bar will redirect the
requester to the target URL that corresponds to the short URL with ID `b`.

#### POST http://localhost:8080/api/short-urls

This route will either create a new short URL for the given target URL, or it will return the
existing short URL for the given target URL if it already exists. The API does perform validation on
the targetUrl field in the request body. The targetUrl must not be blank, and must be a valid full
URL with a length <= 2048 characters. Example request body:

```
{
  "targetUrl": "https://www.bulletin.co"
}
```

## URL Shortener Design, Implementation, and Analysis

### URL Shortener Database Design and Analysis

- The short URL entity primary key data type is an int, this can easily be switched to a bigint if a
  larger range of entity IDs are required.
- The auto increment method is used to generate the primary key for the short URL entities stored in
  the database.
- A secondary index is created on the target_url column to speed up target URL lookups. Target URL
  lookups are used to check if a short URL already exists when receiving a request to create a new
  short URL.

### URL Shortener Database Design Trade-Offs and Alternatives

#### Trade-Offs and Alternatives to Auto Incremented Primary Keys

- Generating the primary key via auto increment is very easy and fast to implement.
- Choosing to generate the primary key using auto increment can impact the concurrency and
  scalability of the application due to the auto increment locks required to ensure unique primary
  key generation.
- Auto increment may not reliably produce unique keys in a sharded database.
- Auto incremented keys make primary key enumeration very easy, which may be a security risk.
- Some alternatives to generating primary keys via auto increment include using UUIDs as the primary
  key, or to generate keys using
  the [Snowflake algorithm](https://en.wikipedia.org/wiki/Snowflake_ID).

#### Trade-Offs and Alternatives to Spring Data JPA

- Spring Data JPA makes it very easy and convenient to interface with a database using application
  code.
- Because Spring Data JPA adds extra layers of abstraction over the database queries, performance
  may reduced as a result.
- Alternatives include Spring Data JDBC and Spring JDBC, both of which offer fewer features but
  better performance.

### URL Shortener API Design and Analysis

- To generate the short URL ID that gets returned to the user, the short URL entity ID is encoded to
  a Base62 string. To find the corresponding short URL entity when given a Base62 encoded short URL
  ID from a request, the ID is Base62 decoded to the short URL's int primary key, which is used to
  find the corresponding short URL entity in the database.
- The Base62 encoded short URL ID is not stored in the database because then 2 database queries, a
  save and update, would be required for new short URLs. This is because the short URL would have to
  be saved first in order to get its primary key due to auto-increment primary key generation, then
  the Base62 encoded ID would need to be calculated, and the new short URL entity would need to be
  updated to store its corresponding Base62 encoded ID. Because the Base62 encoding and decoding are
  in-memory operations, they are faster than having to make another database update query to set the
  short URL ID. One way to save a new short URL ID in the database in the initial save query is to
  generate the primary key in the application code using one of the methods described in
  the `Trade-Offs and Alternatives to Auto Incremented Primary Keys`
  section. This way the short URL ID can be determined without first having to save the short URL
  entity in the database.
- For the short URL redirect route `GET http://localhost:8080/api/short-urls/{shortUrlId}`, an HTTP
  status of 301 is returned to match the behavior of [TinyURL](https://tinyurl.com/app)
  and [Bitly](https://bitly.com/). One advantage of returning a 301 HTTP status is that browsers
  will usually cache the short URL to target URL redirect, sometimes indefinitely, which will reduce
  the response times of subsequent redirects and reduce the number of redirect requests sent to the
  URL shortener service. ***NOTE***: Because browsers will cache the redirects, unexpected redirect
  behavior may happen if you create some short urls and redirect to their target URLs, then restart
  the service and perform the previous actions again. This is because an in-memory database is
  currently used, so if you restart the service then the database is cleared, and creating more
  short URLs may cause overlap with previously created short URLs whose redirects have been cached
  by the browser. To avoid this scenario, if an application restart occurs then you can either clear
  the browser cache or open a private browser window to avoid using previously cached redirects.

## Project Code Standards

- The code style for this project mostly follows
  the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). For criteria
  not explicitly listed in the guide, I used my best judgement to style to code to make it as
  readable as I could, ex: adding empty lines to separate code blocks into logical units of work.
- This project also used code suggestions from [SonarLint](https://www.sonarlint.org/).

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

## Resources Used

- [DZone](https://dzone.com/articles/url-shortener-detailed-explanation)
- [GeeksforGeeks](https://www.geeksforgeeks.org/how-to-design-a-tiny-url-or-url-shortener/)
  


