package fi.nutrifier.controllers.admin;

import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.services.FoodEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Food Entries (Admin)")
@RestController
@RequestMapping("/api/admin/food-entries")
public class AdminFoodEntryController {

    private final FoodEntryService service;

    public AdminFoodEntryController(FoodEntryService service) {
        this.service = service;
    }

    @Operation(summary = "Get all food entries by user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping
    public ResponseEntity<Page<FoodEntry>> getAllUserFoodEntries(
            @RequestParam("userId") String userId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAllByUserId(userId, page, size);
    }

    @Operation(summary = "Get food entry by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping("/{id}")
    public ResponseEntity<FoodEntry> getByFoodEntryId(@PathVariable("id") String id) {
        return service.getById(id);
    }
}