package fi.nutrifier.controllers;

import fi.nutrifier.dto.LoginRequest;
import fi.nutrifier.dto.LoginResponse;
import fi.nutrifier.dto.RegisterRequest;
import fi.nutrifier.dto.UserResponse;
import fi.nutrifier.enums.Role;
import fi.nutrifier.entities.User;
import fi.nutrifier.services.UserService;
import fi.nutrifier.utils.JwtTokenUtil;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthenticationController(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Operation(summary = "Register to the application")
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest registerRequest) throws JOSEException {
        ResponseEntity<Boolean> response = userService.isEmailTaken(registerRequest.getEmail());

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            ResponseEntity<UserResponse> created = userService.create(registerRequest);

            if (created.getStatusCode() == HttpStatus.CREATED) {
                UserResponse userResponse = created.getBody();

                if (userResponse != null) {
                    String token = jwtTokenUtil.generateToken(userResponse.getId(), Role.REGULAR);
                    LoginResponse loginResponse = new LoginResponse(token, userResponse.getId());

                    return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Login to the application")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws JOSEException {
        ResponseEntity<UserResponse> response = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (response.getStatusCode() == HttpStatus.OK) {

            UserResponse user = response.getBody();
            if (user != null) {
                String token = jwtTokenUtil.generateToken(user.getId(), user.getRole());

                LoginResponse loginResponse = new LoginResponse(token, user.getId());
                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Validate authorization token")
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtTokenUtil.validateToken(token)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
