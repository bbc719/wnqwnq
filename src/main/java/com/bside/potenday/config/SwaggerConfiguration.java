package com.bside.potenday.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "비사이드 프로젝트 API 명세서",
                description = "비사이드 프로젝트에 사용되는 API 명세서",
                version = "v1"
        ),
        servers = {
                @Server(url = "https://localhost:8443"),
                @Server(url = "https://jubjub.kr"),
                @Server(url = "http://jubjub.kr"),
                @Server(url = "http://localhost:8081")
        }
)
@Configuration
public class SwaggerConfiguration {
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .components(new Components()
                                .addSecuritySchemes("bearer-key",
                                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                        .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
        }
}
