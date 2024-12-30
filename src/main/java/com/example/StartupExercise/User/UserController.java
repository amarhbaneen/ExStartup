package com.example.StartupExercise.User;


import com.example.StartupExercise.UserMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing User operations.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations for managing users in the system")

public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMetricsService userMetricsService;

    /**
     *
     *   Creates a new User.
     *   @param User The User object to be created.
     *   @return The created User object in a ResponseEntity.
     */

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid user data provided")
            }
    )
    public ResponseEntity<User> createUser(@RequestBody User User){
        User CreatedUser =  userService.createUser(User);
        userMetricsService.incrementUserCreated();
        return  ResponseEntity.ok(CreatedUser);

    }
    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users.
     */
    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all users from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched all users"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<User>> GetAllUsers(){
        List<User> users = userService.getAllUsers();
        return  ResponseEntity.ok(users);
    }

    /**
     * Retrieves a UserAccount by its ID.
     *
     * @param id The ID of the UserAccount to retrieve.
     * @return The UserAccount object in a ResponseEntity, or 404 if not found.
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by their ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
         Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing UserAccount.
     *
     * @param id          The ID of the UserAccount to update.
     * @param user The updated UserAccount details.
     * @return The updated UserAccount object in a ResponseEntity, or 404 if not found.
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update user by ID",
            description = "Updates an existing user based on the provided ID and user data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid user data provided")
            }
    )
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            userMetricsService.incrementUserUpdated();
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * delete user using the ID
     * @param id the ID of the user to be deleted
     */
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete user by ID",
            description = "Delete an existing user based on the provided ID ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
            }
    )
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            userMetricsService.incrementUserDeleted();
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " does not exist.");
        }
    }


    /**
     * Endpoint to update the password of the currently authenticated user.
     * Only the user themselves can update their password.
     *
     * @param username The username of the user whose password is being updated.
     * @param newPassword The new password.
     * @return A success message.
     */
    @PutMapping("/updatePassword/{username}")
    public ResponseEntity updatePassword(@PathVariable String username, @RequestBody String newPassword) {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authenticatedUsername.equals(username)) {
            return ResponseEntity.badRequest().body("You are not authorized to update the password for another user");
        }
        User user = userService.getUserByUserName(authenticatedUsername);
        user.setPassword(newPassword);  // Update the password
        userService.updateUser(user.getId(), user);  // Save the updated user
        return ResponseEntity.ok().body("Password updated successfully!");

    }

}
