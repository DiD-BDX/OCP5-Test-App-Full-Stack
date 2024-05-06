// Ce package contient les tests pour la classe UserDetailsServiceImpl
package com.openclassrooms.starterjwt.security.services;

// Importations nécessaires
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

// Classe de test pour UserDetailsServiceImpl
public class UserDetailsServiceImplTest {

    // Injection des mocks
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserRepository userRepository;

    // Méthode exécutée avant chaque test pour initialiser les mocks
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test pour vérifier que la méthode loadUserByUsername() retourne les détails de l'utilisateur corrects
    @Test
    public void testLoadUserByUsername() {
        // Création d'un utilisateur fictif
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");

        // Configuration du mock pour retourner l'utilisateur fictif lors de l'appel à userRepository.findByEmail()
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        // Appel de la méthode à tester
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@test.com");

        // Vérification que les détails de l'utilisateur retournés sont corrects
        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getFirstName(), userDetails.getFirstName());
        assertEquals(user.getLastName(), userDetails.getLastName());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    // Test pour vérifier que la méthode loadUserByUsername() lance une exception lorsque l'utilisateur n'est pas trouvé
    @Test
    public void testLoadUserByUsernameNotFound() {
        // Configuration du mock pour retourner un Optional vide lors de l'appel à userRepository.findByEmail()
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        // Vérification que la méthode loadUserByUsername() lance une UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("test@test.com");
        });
    }
}