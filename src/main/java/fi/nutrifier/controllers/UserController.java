package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserDto;
import fi.nutrifier.dto.UserGoalsUpdateRequest;
import fi.nutrifier.dto.UserSettingsUpdateRequest;
import fi.nutrifier.entities.User;
import fi.nutrifier.services.UserService;
import fi.nutrifier.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<User> getById(Authentication authentication) {
        String userId = authentication.getName();
        return service.getById(userId);
    }

    @Operation(summary = "Update user data")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<User> update(Authentication authentication, @Valid @RequestBody UserDto item) {
        String userId = authentication.getName();
        return service.update(userId, item);
    }
}
