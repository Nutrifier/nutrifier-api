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
        info = @Info(title = "Nutrifier API", version = "1.0"),
        tags = {
                @Tag(name = "Authentication", description = "Endpoints for user registration and authentication. Publicly accessible."),
                @Tag(name = "Daily Summaries", description = "Endpoints for managing daily summaries as a regular user. Only for authenticated users."),
                @Tag(name = "Foods", description = "Endpoints for managing foods as a regular user. Only for authenticated users."),
                @Tag(name = "Foods (Admin)", description = "Administrative endpoints for managing foods. Only for authenticated admin users."),
                @Tag(name = "Food Entries", description = "Endpoints for managing authenticated user's food entries."),
                @Tag(name = "Food Entries (Admin)", description = "Administrative endpoints for managing food entries. Only for authenticated admin users."),
                @Tag(name = "Goals", description = "Endpoints for managing authenticated user's goals. Access to other users' goals is not permitted."),
                @Tag(name = "Meals", description = "Endpoints for managing meals."),
                @Tag(name = "Profile", description = "Endpoints for managing authenticated user's profile data. Access to other users' data is not permitted."),
                @Tag(name = "Recipes", description = "Endpoints for managing recipes."),
                @Tag(name = "Settings", description = "Endpoints for managing authenticated user's settings. Access to other users' settings is not permitted."),
                @Tag(name = "User", description = "Endpoints for getting authenticated user's credentials. Access to other users' details is not permitted."),
                @Tag(name = "User Feedbacks", description = "Endpoints for managing user feedback."),
                @Tag(name = "Weights", description = "Endpoints for managing authenticated user's weight. Access to other users' weight is not permitted."),
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
