package fi.nutrifier.controllers.admin;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.Food;
import fi.nutrifier.services.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Foods (Admin)")
@RestController
@RequestMapping("/api/v1/admin/foods")
public class AdminFoodController {

    private final FoodService service;

    public AdminFoodController(FoodService service) {
        this.service = service;
    }

    @Operation(summary = "Update food")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @PatchMapping("/{id}")
    public ResponseEntity<FoodResponse> update(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody FoodRequest updated
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.update(UUID.fromString(id), userId, updated);
    }

    @Operation(summary = "Delete food")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        return service.delete(UUID.fromString(id));
    }

    @Operation(summary = "List all food reports")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping("/report")
    public ResponseEntity<Page<FoodReportResponse>> getAllReports(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAllReports(page, size);
    }

    @Operation(summary = "Review a food report")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @PatchMapping("/report/{id}")
    public ResponseEntity<String> reviewReport(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody FoodReportReviewRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.reviewReport(UUID.fromString(id), userId, request);
    }
}