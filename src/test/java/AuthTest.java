import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import passwordApp.repository.UserRepository;

import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testRegister_withValidData() throws Exception {
        String json = "{\"username\":\"wyser\",\"email\":\"wyser@example.com\",\"password\":\"secure123\"}";

        when(userRepository.existsByEmail("wyser@example.com")).thenReturn(false);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testRegister_withDuplicateEmail() throws Exception {
        when(userRepository.existsByEmail("wyser@example.com")).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wyser@example.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_withInvalidCredentials() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wrong@example.com\",\"password\":\"badpass\"}"))
                .andExpect(status().isUnauthorized());
    }
}