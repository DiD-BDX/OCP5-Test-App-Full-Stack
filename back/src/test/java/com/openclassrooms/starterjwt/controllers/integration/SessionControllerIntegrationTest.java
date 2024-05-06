// Ce package contient les tests d'intégration pour le contrôleur de session
package com.openclassrooms.starterjwt.controllers.integration;

// Importations nécessaires
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.JsonNode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// Annotation pour indiquer qu'il s'agit d'un test Spring Boot
@SpringBootTest
// Annotation pour configurer automatiquement le MockMvc
@AutoConfigureMockMvc
public class SessionControllerIntegrationTest {

    // Injection des dépendances
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Session session;

    // Méthode pour extraire le token de la réponse JSON
    private String parseTokenFromResponse(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("token").asText();
    }

    // Méthode exécutée avant chaque test
    @BeforeEach
    public void setUp() {
        // Suppression de tous les utilisateurs
        userRepository.deleteAll();
        // Création d'un nouvel utilisateur
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);

        // Création d'une nouvelle session
        session = new Session();
        session.setId(1L);
        session.setDescription("Description de la session");
        session.setName("Nom de la session");
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        session.setDate(new Date());

        // Enregistrement de la session
        sessionService.create(session);
    }

    // Test pour vérifier que l'API renvoie 200 lorsque les données d'entrée sont valides
    @Test
    public void whenValidInput_thenReturns200() throws Exception {
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
        String token = parseTokenFromResponse(response);

        // Exécution d'une requête GET sur l'API de session avec le token et vérification que l'ID de session est correct
        mockMvc.perform(get("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(session.getId()));
    }

    // Test pour vérifier que l'API renvoie 200 lors de la récupération de toutes les sessions
    @Test
    public void whenGetAll_thenReturns200() throws Exception {
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
        String token = parseTokenFromResponse(response);
        
        // Exécution d'une requête GET sur l'API de session avec le token et vérification que l'ID de session est correct
        mockMvc.perform(get("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(session.getId()));
    }
}