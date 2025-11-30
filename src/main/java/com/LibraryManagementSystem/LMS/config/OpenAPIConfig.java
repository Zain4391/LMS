package com.LibraryManagementSystem.LMS.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management System API")
                        .version("1.0.0")
                        .description("Comprehensive REST API for Library Management System with 115+ endpoints covering all library operations including book management, user management, librarian management, borrowing system, fines, and payments.")
                        .contact(new Contact()
                                .name("LMS Development Team")
                                .email("manutdfast91@gmail.com")
                                .url("https://github.com/Zain4391/LMS"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server")))
                .tags(List.of(
                        new Tag().name("Authentication").description("Authentication endpoints for Users and Librarians"),
                        new Tag().name("Users").description("User management endpoints - Library patron accounts"),
                        new Tag().name("Librarians").description("Librarian management endpoints - Library staff accounts"),
                        new Tag().name("Authors").description("Author management endpoints"),
                        new Tag().name("Books").description("Book management endpoints - Catalog management"),
                        new Tag().name("Book Copies").description("Book copy management endpoints - Physical inventory tracking"),
                        new Tag().name("Borrowed").description("Borrowing system endpoints - Book checkout and returns"),
                        new Tag().name("Fines").description("Fine management endpoints - Overdue penalties"),
                        new Tag().name("Payments").description("Payment processing endpoints - Fine payments"),
                        new Tag().name("Genres").description("Genre/Category management endpoints"),
                        new Tag().name("Publishers").description("Publisher management endpoints")
                ));
    }
}
