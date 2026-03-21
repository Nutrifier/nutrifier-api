package fi.nutrifier.controllers.admin;

import fi.nutrifier.dto.MealResponse;
import fi.nutrifier.dto.RecipeResponse;
import fi.nutrifier.services.MealService;
import fi.nutrifier.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recipes (Admin)")
@RestController
@RequestMapping("/api/admin/recipes")
public class AdminRecipeController {

    private final RecipeService service;

    public AdminRecipeController(RecipeService service) {
        this.service = service;
    }

    @Operation(summary = "Get all recipes")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Page<RecipeResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }
}