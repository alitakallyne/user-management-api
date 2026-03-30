package com.alita.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

     @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
               
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Informe o token JWT no formato: Bearer {token}")
                        )
                )

                
                .info(new Info()
                        .title("User Management API")
                        .version("1.0.0")
                        .description("API de autenticação com JWT e controle de acesso por roles (USER / ADMIN)")
                        .contact(new Contact()
                                .name("Alita")
                                .email("seu-email@email.com")
                        )
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )

                // 🌐 Servidor
                .addServersItem(new Server()
                        .url("http://localhost:8080/api")
                        .description("Servidor Local")
                );
    }
}
