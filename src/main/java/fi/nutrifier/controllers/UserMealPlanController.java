package fi.nutrifier.controllers;

import fi.nutrifier.entities.MealPlan;
import fi.nutrifier.entities.User;
import fi.nutrifier.services.UserMealPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "User Meal Plan")
@RestController
@RequestMapping("/api/users/meal-plan")
public class UserMealPlanController {

    private final UserMealPlanService userMealPlanService;

    public UserMealPlanController(UserMealPlanService userMealPlanService) {
        this.userMealPlanService = userMealPlanService;
    }

    @Operation(summary = "Add new meal plan")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<MealPlan> addMealPlan(Authentication authentication, @Valid @RequestBody MealPlan mealPlan) {
        String userId = authentication.getName();
        return userMealPlanService.create(userId, mealPlan);
    }

    @Operation(summary = "Get all meal plans")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<List<MealPlan>> getAll(Authentication authentication) {
        String userId = authentication.getName();
        return userMealPlanService.getAllByUserId(userId);
    }

    @Operation(summary = "Delete a meal plan")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        return userMealPlanService.delete(id);
    }
}
