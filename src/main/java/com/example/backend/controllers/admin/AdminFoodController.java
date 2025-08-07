package com.example.backend.controllers.admin;

import com.example.backend.entities.Food;
import com.example.backend.services.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin Foods Controller")
@RestController
@RequestMapping("/api/admin/foods")
public class AdminFoodController {

    private final FoodService service;

    public AdminFoodController(FoodService service) {
        this.service = service;
    }

    @Operation(summary = "Update food")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @PatchMapping("/{id}")
    public ResponseEntity<Food> update(@PathVariable("id") UUID id, @Valid @RequestBody Food updated) {
        ResponseEntity<Food> response = service.getById(id);

        if (!response.hasBody()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Food existingFood = response.getBody();

        if (existingFood == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingFood.setName(updated.getName());
        existingFood.setCalories(updated.getCalories());
        existingFood.setBarcode(updated.getBarcode());
        existingFood.setServingSize(updated.getServingSize());
        existingFood.setCarbs(updated.getCarbs());
        existingFood.setProtein(updated.getProtein());
        existingFood.setFat(updated.getFat());
        existingFood.setEditedBy(updated.getEditedBy());
        existingFood.setEdited(updated.getEdited());
        // NOTE: Not updating createdBy and created

        return service.update(id, existingFood);
    }

    @Operation(summary = "Delete food")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @DeleteMapping("/{id}")
    public ResponseEntity<Food> delete(@PathVariable("id") UUID id) {
        return service.delete(id);
    }
}