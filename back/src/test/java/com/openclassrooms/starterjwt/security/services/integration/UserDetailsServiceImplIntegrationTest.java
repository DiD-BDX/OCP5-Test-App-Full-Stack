// Ce package contient les tests d'intégration pour la classe UserDetailsServiceImpl
package com.openclassrooms.starterjwt.security.services.integration;

// Importations nécessaires
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

// Annotations pour indiquer qu'il s'agit d'un test Spring Boot
@SpringBootTest
public class UserDetailsServiceImplIntegrationTest {

    // Injection des dépendances nécessaires
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    // Méthode exécutée avant chaque test
    @BeforeEach
    void setUp() {
        // Nettoyage du contexte de sécurité et de la base de données
        SecurityContextHolder.clearContext();
        userRepository.deleteAll();
        // Création et enregistrement d'un utilisateur fictif
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    // Test pour vérifier que la méthode loadUserByUsername() retourne les détails de l'utilisateur corrects
    @Test
    void shouldLoadUserByUsername() {
        String username = "test@example.com";

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

        assertEquals(username, userDetails.getUsername());
        assertEquals("john", userDetailsImpl.getFirstName());
        assertEquals("doe", userDetailsImpl.getLastName());
    }

    // Test pour vérifier que la méthode loadUserByUsername() lance une exception lorsque l'utilisateur n'est pas trouvé
    @Test
    void shouldThrowUsernameNotFoundExceptionForInvalidUsername() {
        String username = "invalidUser";

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}