package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private User user;
    private String token;
    private Long userId;
    private UserDto userDto;

    private String parseTokenFromResponse(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("token").asText();
    }

    @BeforeEach
    void setUp() throws Exception{
            user = new User();
            user.setId(1L);
            user.setEmail("test@example.com");
            user.setLastName("Doe");
            user.setFirstName("John");
            user.setPassword(passwordEncoder.encode("password"));
            user.setAdmin(false);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println(response);
        token = parseTokenFromResponse(response);

        userId = 1L;
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setEmail(user.getEmail());
    }

    @Test
    void shouldFetchUserById() throws Exception {
        

        given(userService.findById(userId)).willReturn(user);
        given(userMapper.toDto(user)).willReturn(userDto);
    
        this.mockMvc.perform(get("/api/user/{id}", userId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));   
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        final Long userId = 1L;

        given(userService.findById(userId)).willReturn(user);

        this.mockMvc.perform(delete("/api/user/{id}", userId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowBadRequestExceptionForDuplicateUsername() throws Exception {
        String username = "test@example.com";
        String password = "password";
        //final Long userId = 1L;

        this.mockMvc.perform(post("/api/auth/register")
            .header("Authorization", "Bearer " + token)
            .param("username", username)
            .param("password", password))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
