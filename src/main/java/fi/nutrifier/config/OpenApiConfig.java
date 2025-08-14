package fi.nutrifier.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Recipe App API", version = "1.0"),
        tags = {
                @Tag(name = "Authentication Controller", description = "Endpoints for managing user creation and login. Anyone can access these."),
                @Tag(name = "Foods Controller", description = "Endpoints for managing foods as a regular user. Only authenticated user can access these."),
                @Tag(name = "Logs Controller", description = "Endpoints for managing logs as a regular user. Only authenticated user can access these."),
                @Tag(name = "Admin Users Controller", description = "Endpoints for managing users as an admin. Only authenticated admin can access these."),
                @Tag(name = "Admin Foods Controller", description = "Endpoints for managing foods as an admin. Only authenticated admin can access these."),
                @Tag(name = "Admin Logs Controller", description = "Endpoints for managing logs as an admin. Only authenticated admin can access these."),
        }
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }
}
