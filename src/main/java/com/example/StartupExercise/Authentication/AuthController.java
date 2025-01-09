package com.example.StartupExercise.Authentication;

import com.example.StartupExercise.User.User;
import com.example.StartupExercise.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle user login and JWT token generation.
 */

@RestController
@RequestMapping("/Auth")
public class AuthController {
    @Autowired
    private UserService userService;
    /**
     * Login endpoint for user authentication.
     *
     * @param loginRequest the login request containing username and password.
     * @return a JWT token if authentication is successful.
     * @throws RuntimeException if the username or password is invalid.
     */
    @Operation(summary = "Authenticate user and generate JWT token", description = "Authenticates a user using their username and password, and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)
    })
    @PostMapping("/login")
    @Cacheable(value = "Response" , key = "#loginRequest")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.getUserByUserName(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(JwtUtil.generateToken(user.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}
