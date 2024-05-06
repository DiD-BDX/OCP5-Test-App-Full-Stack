// Ce package contient les tests pour le mapper de session
package com.openclassrooms.starterjwt.mapper;

// Importations nécessaires
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import org.mockito.Mockito;
import java.util.Arrays;

public class SessionMapperTest {

    // Injection des mocks
    @InjectMocks
    private SessionMapper sessionMapper = new SessionMapperImpl();

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    // Méthode exécutée avant chaque test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test pour vérifier que le mapper transforme correctement un DTO de session en entité de session
    @Test
    void shouldMapDtoToEntity() {
        // Création d'un DTO de session
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        // Création d'utilisateurs et d'un professeur
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Configuration des mocks pour renvoyer les utilisateurs et le professeur
        Mockito.when(userService.findById(1L)).thenReturn(user1);
        Mockito.when(userService.findById(2L)).thenReturn(user2);
        Mockito.when(teacherService.findById(1L)).thenReturn(teacher); 

        // Transformation du DTO de session en entité de session
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérification que les données de l'entité de session sont correctes
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
    }

    // Test pour vérifier que le mapper transforme correctement une entité de session en DTO de session
    @Test
    void shouldMapEntityToDto() {
        // Création d'une entité de session
        Session session = new Session();
        session.setDescription("Test Session");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(new User(), new User()));

        // Transformation de l'entité de session en DTO de session
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Vérification que les données du DTO de session sont correctes
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
    }
}