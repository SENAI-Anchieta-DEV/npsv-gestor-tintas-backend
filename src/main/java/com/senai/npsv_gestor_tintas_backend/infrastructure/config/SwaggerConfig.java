package com.senai.npsv_gestor_tintas_backend.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public static final String SECURITY_SCHEME_NAME = "bearer-jwt";

    @Bean
    public OpenAPI gestorTintasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NPSV - Gestor Tintas API")
                        .description("API para gerenciamento de estoque, produção e vendas de tintas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Desenvolvimento Gestor Tintas")
                                .email("suporte@gestortintas.com")))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
