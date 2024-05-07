// Ce package contient les tests pour la classe SessionService
package com.openclassrooms.starterjwt.services;

// Importations nécessaires
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Classe de test pour SessionService

public class SessionServiceTest {

    // Injection des mocks
    @InjectMocks
    private SessionService sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    // Méthode exécutée avant chaque test pour initialiser les mocks
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test pour vérifier que la méthode create() fonctionne correctement
    @Test
    public void testCreate() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);
        assertEquals(session, sessionService.create(session));
    }

    // Test pour vérifier que la méthode delete() fonctionne correctement
    @Test
    public void testDelete() {
        doNothing().when(sessionRepository).deleteById(1L);
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    // Test pour vérifier que la méthode findAll() fonctionne correctement
    @Test
    public void testFindAll() {
        Session session = new Session();
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session));
        assertEquals(1, sessionService.findAll().size());
    }

    // Test pour vérifier que la méthode getById() fonctionne correctement
    @Test
    public void testGetById() {
        Session session = new Session();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        assertEquals(session, sessionService.getById(1L));
    }

    // Test pour vérifier que la méthode update() fonctionne correctement
    @Test
    public void testUpdate() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);
        assertEquals(session, sessionService.update(1L, session));
    }

    // Test pour vérifier que la méthode participate() fonctionne correctement
    @Test
    public void testParticipate() {
        // Créer des objets Session et User
        Session session = new Session();
        session.setUsers(new ArrayList<>());
        User user = new User();

        // Configurer les mocks pour renvoyer ces objets
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Appeler la méthode à tester
        sessionService.participate(1L, 1L);

        // Vérifier que la méthode save a été appelée
        verify(sessionRepository, times(1)).save(session);
    }

    // Test pour vérifier que la méthode noLongerParticipate() fonctionne correctement
    @Test
    public void testNoLongerParticipate() {
        Session session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());
        User user = new User();
        user.setId(1L);
        
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        sessionService.noLongerParticipate(1L, 1L);
        verify(sessionRepository, times(1)).save(session);
    }
}