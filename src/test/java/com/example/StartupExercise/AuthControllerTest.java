package com.example.StartupExercise;
import com.example.StartupExercise.AuthConfig.AuthController;
import com.example.StartupExercise.User.User;
import com.example.StartupExercise.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }
    /**
     * Test the login method in AuthController
     * Verifies that login is successful and received a valid JWT
     */
    @Test
    public void testLogin_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("testpassword");
        when(userService.getUserByUserName("testuser")).thenReturn(mockUser);
        // Create login request
        String loginRequest = "{\"username\":\"testuser\", \"password\":\"testpassword\"}";
        // Perform the POST request and assert the result
        MvcResult result = mockMvc.perform(post("/Auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn();
        String jwtToken = result.getResponse().getContentAsString();
        assertTrue(jwtToken.matches("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$"));  // This regex checks for a JWT format
    }

    /**
     * Test the login method in AuthController
     * Verifies that login is unsuccessful and received a 401 error
     */

    @Test
    public void testLogin_Failure_InvalidCredentials() throws Exception {
        when(userService.getUserByUserName("wronguser")).thenReturn(null);
        // Create login request with incorrect credentials
        String loginRequest = "{\"username\":\"wronguser\", \"password\":\"wrongpassword\"}";
        // Perform the POST request and assert the result
        mockMvc.perform(post("/Auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }
}