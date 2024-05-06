// Ce package contient les tests d'intégration pour le contrôleur d'utilisateur
package com.openclassrooms.starterjwt.controllers.integration;

// Importations nécessaires
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

// Annotation pour indiquer qu'il s'agit d'un test Spring Boot
@SpringBootTest
// Annotation pour configurer automatiquement le MockMvc
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    // Injection des dépendances
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock du service d'utilisateur et du mapper d'utilisateur
    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    // Variables pour stocker les données de l'utilisateur, le token d'authentification, l'ID de l'utilisateur et le DTO de l'utilisateur
    private User user;
    private String token;
    private Long userId;
    private UserDto userDto;

    // Méthode pour extraire le token de la réponse JSON
    private String parseTokenFromResponse(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("token").asText();
    }

    // Méthode exécutée avant chaque test
    @BeforeEach
    void setUp() throws Exception{
        // Création d'un nouvel utilisateur
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAdmin(false);
        userRepository.save(user);

        // Création d'une demande de connexion
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // Exécution de la demande de connexion et récupération du token
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println(response);
        token = parseTokenFromResponse(response);

        // Création d'un DTO d'utilisateur
        userId = 1L;
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setEmail(user.getEmail());
    }

    // Test pour vérifier que l'API renvoie un utilisateur par son ID
    @Test
    void shouldFetchUserById() throws Exception {
        // Configuration des mocks pour renvoyer l'utilisateur et le DTO de l'utilisateur
        given(userService.findById(userId)).willReturn(user);
        given(userMapper.toDto(user)).willReturn(userDto);

        // Exécution d'une requête GET sur l'API d'utilisateur avec le token et l'ID de l'utilisateur et vérification que les données de l'utilisateur sont correctes
        this.mockMvc.perform(get("/api/user/{id}", userId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));   
    }

    // Test pour vérifier que l'API supprime un utilisateur par son ID
    @Test
    void shouldDeleteUserById() throws Exception {
        final Long userId = 1L;

        // Configuration du mock pour renvoyer l'utilisateur
        given(userService.findById(userId)).willReturn(user);

        // Exécution d'une requête DELETE sur l'API d'utilisateur avec le token et l'ID de l'utilisateur et vérification que le statut est OK
        this.mockMvc.perform(delete("/api/user/{id}", userId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    // Test pour vérifier que l'API renvoie une exception BadRequest pour un nom d'utilisateur en double
    @Test
    void shouldThrowBadRequestExceptionForDuplicateUsername() throws Exception {
        String username = "test@example.com";
        String password = "password";

        // Exécution d'une requête POST sur l'API d'authentification avec le token et un nom d'utilisateur en double et vérification que le statut est BadRequest
        this.mockMvc.perform(post("/api/auth/register")
            .header("Authorization", "Bearer " + token)
            .param("username", username)
            .param("password", password))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}