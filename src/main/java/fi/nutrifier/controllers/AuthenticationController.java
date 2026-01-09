package fi.nutrifier.controllers;

import fi.nutrifier.dto.AuthRequest;
import fi.nutrifier.dto.AuthResponse;
import fi.nutrifier.dto.UserDto;
import fi.nutrifier.entities.Role;
import fi.nutrifier.entities.User;
import fi.nutrifier.services.UserService;
import fi.nutrifier.utils.JwtTokenUtil;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller")
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
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody AuthRequest authRequest) throws JOSEException {
        ResponseEntity<Boolean> response = userService.isEmailTaken(authRequest.getEmail());
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {

            UserDto newUser = new UserDto();
            newUser.initialize(authRequest.getEmail(), authRequest.getPassword(), Role.ROLE_USER);
            ResponseEntity<User> created = userService.create(newUser);

            if (created.getStatusCode() == HttpStatus.CREATED) {
                User user = created.getBody();
                if (user != null) {
                    String token = jwtTokenUtil.generateToken(authRequest.getEmail(), Role.ROLE_USER);
                    AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getEmail());
                    return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Login to the application")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws JOSEException {
        ResponseEntity<User> response = userService.login(authRequest.getEmail(), authRequest.getPassword());

        System.out.println("Login response: " + response);

        if (response.getStatusCode() == HttpStatus.OK) {

            User user = response.getBody();
            if (user != null) {
                String token = jwtTokenUtil.generateToken(authRequest.getEmail(), user.getRole());

                System.out.println("Login token: " + token);

                AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getEmail());
                return new ResponseEntity<>(authResponse, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
