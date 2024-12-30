package com.example.StartupExercise.AuthConfig;

import com.example.StartupExercise.User.User;
import com.example.StartupExercise.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // Get the user from the database
        User user = userService.getUserByUserName(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            return JwtUtil.generateToken(user.getUsername());  // Generate JWT token
        }
        throw new RuntimeException("Invalid username or password");
    }
}
