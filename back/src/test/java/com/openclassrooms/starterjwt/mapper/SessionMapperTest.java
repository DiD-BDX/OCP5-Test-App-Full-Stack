package com.openclassrooms.starterjwt.mapper;
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

    @InjectMocks
    private SessionMapper sessionMapper = new SessionMapperImpl();

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldMapDtoToEntity() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Mockito.when(userService.findById(1L)).thenReturn(user1);
        Mockito.when(userService.findById(2L)).thenReturn(user2);
        Mockito.when(teacherService.findById(1L)).thenReturn(teacher); 

        Session session = sessionMapper.toEntity(sessionDto);

        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
    }

    @Test
    void shouldMapEntityToDto() {
        Session session = new Session();
        session.setDescription("Test Session");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(new User(), new User()));

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
    }
}
