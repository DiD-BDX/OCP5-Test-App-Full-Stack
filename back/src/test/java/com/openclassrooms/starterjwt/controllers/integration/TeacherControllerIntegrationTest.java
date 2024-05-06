// Ce package contient les tests d'intégration pour le contrôleur de professeur
package com.openclassrooms.starterjwt.controllers.integration;

// Importations nécessaires
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.*;

// Annotation pour indiquer qu'il s'agit d'un test Spring Boot
@SpringBootTest
// Annotation pour configurer automatiquement le MockMvc
@AutoConfigureMockMvc
public class TeacherControllerIntegrationTest {

    // Injection des dépendances
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock du service de professeur
    @MockBean
    private TeacherService teacherService;

    // Liste des professeurs à utiliser dans les tests
    private List<Teacher> teacherList;

    // Token d'authentification à utiliser dans les tests
    private String token;

    // Méthode pour extraire le token de la réponse JSON
    private String parseTokenFromResponse(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("token").asText();
    }

    // Méthode exécutée avant chaque test
    @BeforeEach
    void setUp() throws Exception {
        // Suppression de tous les utilisateurs
        userRepository.deleteAll();
        // Création d'un nouvel utilisateur
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);

        // Création d'une liste de professeurs
        this.teacherList = Arrays.asList(
            new Teacher().setId(1L).setFirstName("John").setLastName("Doe"),
            new Teacher().setId(2L).setFirstName("Jane").setLastName("Doe"));
        
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
    }

    // Test pour vérifier que l'API renvoie tous les professeurs
    @Test
    void shouldFetchAllTeachers() throws Exception {
        // Configuration du mock pour renvoyer la liste des professeurs
        given(teacherService.findAll()).willReturn(this.teacherList);

        // Exécution d'une requête GET sur l'API de professeur avec le token et vérification que la taille de la liste est correcte
        this.mockMvc.perform(get("/api/teacher")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(teacherList.size())));
    }

    // Test pour vérifier que l'API renvoie un professeur par son ID
    @Test
    void shouldFetchOneTeacherById() throws Exception {
        final Long teacherId = 1L;
        final Teacher teacher = new Teacher().setId(teacherId).setFirstName("John").setLastName("Doe");

        // Configuration du mock pour renvoyer le professeur
        given(teacherService.findById(teacherId)).willReturn(teacher);

        // Exécution d'une requête GET sur l'API de professeur avec le token et l'ID du professeur et vérification que les données du professeur sont correctes
        this.mockMvc.perform(get("/api/teacher/{id}", teacherId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(teacher.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(teacher.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teacher.getLastName())));
    }
}