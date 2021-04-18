package co.bulletin.urlshortener.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    servers = {@Server(url = "http://localhost:8080/api", description = "localhost")},
    info =
    @Info(
        title = "Bulletin URL Shortener Service",
        version = "v1.0.0",
        description = "API documentation for the Bulletin URL shortener service assignment",
        contact =
        @Contact(
            name = "Jerry Ye",
            email = "yejerry333@gmail.com")))
public class OpenApiConfiguration {

}
