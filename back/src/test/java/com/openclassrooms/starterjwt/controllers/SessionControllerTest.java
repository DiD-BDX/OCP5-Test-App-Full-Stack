package com.openclassrooms.starterjwt.controllers;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @Test
    public void testFindById() {
        // Créer un objet Session
        Session session = new Session();
        session.setId(1L);

        // Créer un objet SessionDto
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        // Configurer le mock pour renvoyer ces objets
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Appeler la méthode à tester
        ResponseEntity<?> response = sessionController.findById("1");

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof SessionDto);
        assertEquals(1L, ((SessionDto) response.getBody()).getId());
    }

    @Test
    public void testFindAll() {
        // Créer une liste de sessions
        List<Session> sessions = Arrays.asList(new Session(), new Session());

        // Configurer le mock pour renvoyer cette liste
        when(sessionService.findAll()).thenReturn(sessions);

        // Appeler la méthode à tester
        ResponseEntity<?> response = sessionController.findAll();

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof List);
    }

    @Test
    public void testCreate() {
        // Créer un objet Session
        Session session = new Session();
        session.setId(1L);

        // Créer un objet SessionDto
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        // Configurer le mock pour renvoyer ces objets
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Appeler la méthode à tester
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof SessionDto);
        assertEquals(1L, ((SessionDto) response.getBody()).getId());
    }

    @Test
    public void testUpdate() {
        // Créer un objet Session
        Session session = new Session();
        session.setId(1L);

        // Créer un objet SessionDto
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        // Configurer le mock pour renvoyer ces objets
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Appeler la méthode à tester
        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof SessionDto);
        assertEquals(1L, ((SessionDto) response.getBody()).getId());
    }

    @Test
    public void testDelete() {
        // Créer un objet Session
        Session session = new Session();
        session.setId(1L);

        // Configurer le mock pour renvoyer cet objet
        when(sessionService.getById(1L)).thenReturn(session);

        // Appeler la méthode à tester
        ResponseEntity<?> response = sessionController.save("1");

        // Vérifier que la méthode delete a été appelée
        verify(sessionService, times(1)).delete(1L);

        // Vérifier que le résultat est correct
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testParticipate() {
        // Appeler la méthode à tester
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // Vérifier que la méthode participate a été appelée
        verify(sessionService, times(1)).participate(1L, 1L);

        // Vérifier que le résultat est correct
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testNoLongerParticipate() {
        // Appeler la méthode à tester
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        // Vérifier que la méthode noLongerParticipate a été appelée
        verify(sessionService, times(1)).noLongerParticipate(1L, 1L);

        // Vérifier que le résultat est correct
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}