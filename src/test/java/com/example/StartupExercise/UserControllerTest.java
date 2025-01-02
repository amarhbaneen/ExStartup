package com.example.StartupExercise;
import com.example.StartupExercise.User.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@WebMvcTest(UserController.class)  // Ensure you use @WebMvcTest to focus on the controller layer
@AutoConfigureMockMvc(addFilters = false) // to disable the Auth
@ExtendWith(MockitoExtension.class)

public class UserControllerTest {

    @InjectMocks
    private UserController userController;  // Controller we are testing

    @MockBean
    private UserService userService;  // Mocked service for user operations

    @MockBean
    private UserMetricsService userMetricsService;  // Mocked service for user metrics

    private User testUser;  // Sample user for testing

    /**
     * Setup method to initialize the test data before each test case.
     */
    @BeforeEach
    public void setUp() {
        testUser = new User(1, "testUser", "password123", "Test", "User", Role.USER);
    }

    /**
     * Tests the createUser method in UserController.
     * Verifies that a user is created successfully and the metrics are incremented.
     */
    @Test
    public void testCreateUser() {
        // Arrange: Mock the behavior of the userService to return the created user
        when(userService.createUser(testUser)).thenReturn(testUser);

        // Act: Call the createUser method in the controller
        ResponseEntity<User> response = userController.createUser(testUser);

        // Assert: Verify the response status and the body content
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testUser);

        // Verify that the userMetricsService incremented the user creation count
        verify(userMetricsService).incrementUserCreated();
    }

    /**
     * Tests the GetAllUsers method in UserController.
     * Verifies that all users are fetched and returned correctly.
     */
    @Test
    public void testGetAllUsers() {
        // Arrange: Mock the userService to return a list of users
        List<User> users = List.of(testUser);
        when(userService.getAllUsers()).thenReturn(users);

        // Act: Call the GetAllUsers method in the controller
        ResponseEntity<List<User>> response = userController.GetAllUsers();

        // Assert: Verify the response status and the returned users
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testUser);
    }

    /**
     * Tests the getUserById method in UserController.
     * Verifies that a user is retrieved by their ID.
     */
    @Test
    public void testGetUserById() {
        // Arrange: Mock the userService to return the user with the specified ID
        when(userService.getUserById(1)).thenReturn(Optional.of(testUser));

        // Act: Call the getUserById method in the controller
        ResponseEntity<User> response = userController.getUserById(1);

        // Assert: Verify the response status and the returned user
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testUser);
    }

    /**
     * Tests the getUserById method in UserController when the user is not found.
     * Verifies that the method returns a 404 status when the user does not exist.
     */
    @Test
    public void testGetUserByIdNotFound() {
        // Arrange: Mock the userService to return an empty Optional
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        // Act: Call the getUserById method in the controller
        ResponseEntity<User> response = userController.getUserById(1);

        // Assert: Verify the response status is 404
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Tests the updateUser method in UserController.
     * Verifies that an existing user is updated successfully.
     */
    @Test
    public void testUpdateUser() {
        // Arrange: Create a new User with updated values
        User updatedUser = new User(1, "userTest", "PasswordTest", "UserNameTest", "lastNameTest", Role.USER);

        // Mock the userService to return the updated user when updateUser is called
        when(userService.updateUser(1, updatedUser)).thenReturn(updatedUser);

        // Act: Call the updateUser method in the controller
        ResponseEntity<User> response = userController.updateUser(1, updatedUser);

        // Assert: Verify the response status and the updated user content
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedUser);

        // Verify that the userMetricsService incremented the user update count
        verify(userMetricsService).incrementUserUpdated();
    }

    /**
     * Tests the updateUser method in UserController when the user is not found.
     * Verifies that the method returns a 404 status when the user does not exist.
     */
    @Test
    public void testUpdateUserNotFound() {
        // Arrange: Create a new User with updated values
        User updatedUser = new User(1, "userTest", "PasswordTest", "UserNameTest", "lastNameTest", Role.USER);

        // Mock the userService to throw an exception when the user is not found
        when(userService.updateUser(1, updatedUser)).thenThrow(new RuntimeException("User not found"));

        // Act: Call the updateUser method in the controller
        ResponseEntity<User> response = userController.updateUser(1, updatedUser);

        // Assert: Verify the response status is 404
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Tests the deleteUser method in UserController.
     * Verifies that the user is deleted successfully.
     */
    @Test
    public void testDeleteUser() {
        // Arrange: Mock the userService to delete the user successfully
        doNothing().when(userService).deleteUser(1);

        // Act: Call the deleteUser method in the controller
        ResponseEntity<String> response = userController.deleteUser(1);

        // Assert: Verify the response status is 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify that the userMetricsService incremented the user delete count
        verify(userMetricsService).incrementUserDeleted();
    }

    /**
     * Tests the deleteUser method in UserController when the user is not found.
     * Verifies that the method returns a 404 status when the user does not exist.
     */
    @Test
    public void testDeleteUserNotFound() {
        // Arrange: Mock the userService to throw an exception when the user is not found
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(1);

        // Act: Call the deleteUser method in the controller
        ResponseEntity<String> response = userController.deleteUser(1);

        // Assert: Verify the response status is 404 and the message content
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User with ID 1 does not exist.");
    }
}


