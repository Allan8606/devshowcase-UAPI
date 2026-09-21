package com.devshowcase.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "DevShowcase API",
                version = "1.0",
                description = "API para cadastro e compartilhamento de projetos de desenvolvedores."
        )
)
public class OpenApiConfig {
}