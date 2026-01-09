package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserDto;
import fi.nutrifier.entities.User;
import fi.nutrifier.services.UserService;
import fi.nutrifier.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    @Operation(summary = "Get user by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") String id) {
        return service.getById(id);
    }

    @Operation(summary = "Update user data")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") String id, @Valid @RequestBody UserDto item) {
        return service.update(id, item);
    }
}
