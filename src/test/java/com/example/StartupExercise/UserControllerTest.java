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
    private UserController userController;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMetricsService userMetricsService;
    private User testUser;
    /**
     * Setup method to initialize the test data before each test case.
     */
    @BeforeEach
    public void setUp() {

        testUser = new User();
        testUser.setId(1);
        testUser.setPassword("password123");
        testUser.setFirstName("Test");
        testUser.setSurName("Test");
        testUser.setRole(Role.USER);
    }

    /**
     * Tests the createUser method in UserController.
     * Verifies that a user is created successfully and the metrics are incremented.
     */
    @Test
    public void testCreateUser() {
        when(userService.createUser(testUser)).thenReturn(testUser);
        ResponseEntity<User> response = userController.createUser(testUser);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testUser);
        verify(userMetricsService).incrementUserCreated();
    }

    /**
     * Tests the GetAllUsers method in UserController.
     * Verifies that all users are fetched and returned correctly.
     */
    @Test
    public void testGetAllUsers() {
        List<User> users = List.of(testUser);
        when(userService.getAllUsers()).thenReturn(users);
        ResponseEntity<List<User>> response = userController.GetAllUsers();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testUser);
    }

    /**
     * Tests the getUserById method in UserController.
     * Verifies that a user is retrieved by their ID.
     */
    @Test
    public void testGetUserById() {
        when(userService.getUserById(1)).thenReturn(Optional.of(testUser));
        ResponseEntity<User> response = userController.getUserById(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testUser);
    }

    /**
     * Tests the getUserById method in UserController when the user is not found.
     * Verifies that the method returns a 404 status when the user does not exist.
     */
    @Test
    public void testGetUserByIdNotFound() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());
        ResponseEntity<User> response = userController.getUserById(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Tests the updateUser method in UserController.
     * Verifies that an existing user is updated successfully.
     */
    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setPassword("password123");
        updatedUser.setFirstName("Test");
        updatedUser.setSurName("Test");
        updatedUser.setRole(Role.USER);
        when(userService.updateUser(1, updatedUser)).thenReturn(updatedUser);
        ResponseEntity<User> response = userController.updateUser(1, updatedUser);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedUser);
        verify(userMetricsService).incrementUserUpdated();
    }

    /**
     * Tests the updateUser method in UserController when the user is not found.
     * Verifies that the method returns a 404 status when the user does not exist.
     */
    @Test
    public void testUpdateUserNotFound() {
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setPassword("password123");
        updatedUser.setFirstName("Test");
        updatedUser.setSurName("Test");
        updatedUser.setRole(Role.USER);
        updatedUser.setUsername("UserNameTest");
        when(userService.updateUser(1, updatedUser)).thenThrow(new RuntimeException("User not found"));
        ResponseEntity<User> response = userController.updateUser(1, updatedUser);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Tests the deleteUser method in UserController.
     * Verifies that the user is deleted successfully.
     */
    @Test
    public void testDeleteUser() {
        doNothing().when(userService).deleteUser(1);
        ResponseEntity<String> response = userController.deleteUser(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userMetricsService).incrementUserDeleted();
    }

    /**
     * Tests the deleteUser method in UserController when the user is not found.
     * Verifies that the method returns a 404 status when the user does not exist.
     */
    @Test
    public void testDeleteUserNotFound() {
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(1);
        ResponseEntity<String> response = userController.deleteUser(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User with ID 1 does not exist.");
    }
}


