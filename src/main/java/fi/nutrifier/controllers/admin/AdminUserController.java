package fi.nutrifier.controllers.admin;

import fi.nutrifier.dto.UserDto;
import fi.nutrifier.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fi.nutrifier.entities.User;

import java.util.UUID;

@Tag(name = "User Profile (Admin)")
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService service;

    public AdminUserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Get all users")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping
    public ResponseEntity<Page<User>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }

    @Operation(summary = "Get user by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") String id) {
        return service.getById(id);
    }

    @Operation(summary = "Update user")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") String id, @Valid @RequestBody UserDto item) {
        return service.update(id, item);
    }

    @Operation(summary = "Delete user")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable("id") String id) {
        return service.delete(id);
    }
}