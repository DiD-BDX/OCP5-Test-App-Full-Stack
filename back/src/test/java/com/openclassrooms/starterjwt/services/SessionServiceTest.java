package com.openclassrooms.starterjwt.services;

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

public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);
        assertEquals(session, sessionService.create(session));
    }

    @Test
    public void testDelete() {
        doNothing().when(sessionRepository).deleteById(1L);
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAll() {
        Session session = new Session();
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session));
        assertEquals(1, sessionService.findAll().size());
    }

    @Test
    public void testGetById() {
        Session session = new Session();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        assertEquals(session, sessionService.getById(1L));
    }

    @Test
    public void testUpdate() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);
        assertEquals(session, sessionService.update(1L, session));
    }

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
        System.out.println(session.getUsers());

        // Vérifier que la méthode save a été appelée
        verify(sessionRepository, times(1)).save(session);
    }

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