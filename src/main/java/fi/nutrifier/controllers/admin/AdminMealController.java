package fi.nutrifier.controllers.admin;

import fi.nutrifier.dto.*;
import fi.nutrifier.services.FoodService;
import fi.nutrifier.services.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Meals (Admin)")
@RestController
@RequestMapping("/api/v1/admin/meals")
public class AdminMealController {

    private final MealService service;

    public AdminMealController(MealService service) {
        this.service = service;
    }

    @Operation(summary = "Get all meals")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Page<MealResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }
}