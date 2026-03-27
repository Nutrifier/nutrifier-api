package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserResponse;
import fi.nutrifier.entities.User;
import fi.nutrifier.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User Profile")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    @Operation(summary = "Get user by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<UserResponse> getById(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.getById(userId);
    }

    // TODO: Implement changing email and password
}
