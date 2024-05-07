// Ce package contient les tests pour la classe UserService
package com.openclassrooms.starterjwt.services;

// Importations nécessaires
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import java.util.Optional;

// Classe de test pour UserService
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // Injection des mocks
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    // Test pour vérifier que la méthode delete() fonctionne correctement
    @Test
    public void testDelete() {
        // Appel de la méthode à tester
        userService.delete(1L);

        // Vérification que la méthode deleteById() du repository a été appelée une fois avec l'argument 1L
        verify(userRepository, times(1)).deleteById(1L);
    }

    // Test pour vérifier que la méthode findById() fonctionne correctement
    @Test
    public void testFindById() {
        // Création d'un objet User
        User user = new User();
        user.setId(1L);

        // Configuration du mock pour retourner cet objet lors de l'appel à userRepository.findById()
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Appel de la méthode à tester
        User foundUser = userService.findById(1L);

        // Vérification que la méthode findById() du repository a été appelée une fois avec l'argument 1L
        verify(userRepository, times(1)).findById(1L);

        // Vérification que le résultat est correct
        assertEquals(user, foundUser);
    }
}