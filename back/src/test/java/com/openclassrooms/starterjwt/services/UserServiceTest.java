package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testDelete() {
        // Appeler la méthode à tester
        userService.delete(1L);

        // Vérifier que la méthode deleteById a été appelée
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindById() {
        // Créer un objet User
        User user = new User();
        user.setId(1L);

        // Configurer le mock pour renvoyer cet objet
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Appeler la méthode à tester
        User foundUser = userService.findById(1L);

        // Vérifier que la méthode findById a été appelée
        verify(userRepository, times(1)).findById(1L);

        // Vérifier que le résultat est correct
        assertEquals(user, foundUser);
    }
}
