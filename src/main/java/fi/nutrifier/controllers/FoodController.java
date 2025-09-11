package fi.nutrifier.controllers;

import fi.nutrifier.services.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fi.nutrifier.entities.Food;
import java.util.List;
import java.util.UUID;

@Tag(name = "Foods Controller")
@RestController
@RequestMapping("/api/foods")
public class FoodController {

    protected final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @Operation(summary = "Create a food")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<Food> create(@Valid @RequestBody Food entity) {
        return service.create(entity);
    }

    @Operation(summary = "Get food by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/{id}")
    public ResponseEntity<Food> getById(@PathVariable("id") String id) {
        return service.getById(id);
    }

    @Operation(summary = "Search for foods by name")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/query")
    public ResponseEntity<List<Food>> getFoodsByQuery(@RequestParam String query) {
        return service.getFoodsByQuery(query);
    }

    @Operation(summary = "Search for foods by barcode")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/barcode")
    public ResponseEntity<List<Food>> getFoodsByBarcode(@RequestParam String query) {
        return service.getFoodsByBarcode(query);
    }

    @Operation(summary = "Get all foods")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Page<Food>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }
}