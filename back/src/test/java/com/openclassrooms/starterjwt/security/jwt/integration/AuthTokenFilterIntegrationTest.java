// Ce package contient les tests d'intégration pour le filtre de token d'authentification
package com.openclassrooms.starterjwt.security.jwt.integration;

// Importations nécessaires
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

// Annotations pour indiquer qu'il s'agit d'un test Spring Boot avec configuration automatique de MockMvc
@SpringBootTest
@AutoConfigureMockMvc
public class AuthTokenFilterIntegrationTest {

    // Injection des dépendances nécessaires
    @Autowired
    private AuthTokenFilter authTokenFilter;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Méthode exécutée avant chaque test
    @BeforeEach
    void setUp() {
        // Nettoyage du contexte de sécurité et de la base de données
        SecurityContextHolder.clearContext();
        userRepository.deleteAll();
        // Création et enregistrement d'un utilisateur fictif
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    // Test pour vérifier que le filtre de token d'authentification définit correctement l'authentification de l'utilisateur
    @Test
    void shouldSetUserAuthentication() throws Exception {
        // Chargement des détails de l'utilisateur et génération d'un token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = jwtUtils.generateJwtToken(authentication);

        // Création d'une requête, d'une réponse et d'une chaîne de filtres fictives
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // Appel de la méthode à tester
        authTokenFilter.doFilter(request, response, filterChain);

        // Vérification que l'authentification de l'utilisateur a été définie correctement
        assertEquals(userDetails.getUsername(), ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    // Test pour vérifier que le filtre de token d'authentification ne définit pas l'authentification de l'utilisateur pour un token invalide
    @Test
    void shouldNotSetUserAuthenticationForInvalidToken() throws Exception {
        // Création d'une requête, d'une réponse et d'une chaîne de filtres fictives
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // Appel de la méthode à tester
        authTokenFilter.doFilter(request, response, filterChain);

        // Vérification que l'authentification de l'utilisateur n'a pas été définie
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}