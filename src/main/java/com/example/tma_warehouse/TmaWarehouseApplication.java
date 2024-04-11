package com.example.tma_warehouse;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "Apply Default Global SecurityScheme in springdoc-openapi", version = "1.0.0"), security = { @SecurityRequirement(name = "api_key") })
public class TmaWarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmaWarehouseApplication.class, args);
    }

}
