package com.openclassrooms.starterjwt.controllers;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TeacherControllerTest {

    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @Test
    public void testFindById() {
        // Créer un objet Teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Créer un objet TeacherDto
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);

        // Configurer le mock pour renvoyer ces objets
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Appeler la méthode à tester
        ResponseEntity<?> response = teacherController.findById("1");

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof TeacherDto);
        assertEquals(1L, ((TeacherDto) response.getBody()).getId());
    }

    @Test
    public void testFindAll() {
        // Créer une liste de teachers
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());

        // Configurer le mock pour renvoyer cette liste
        when(teacherService.findAll()).thenReturn(teachers);

        // Appeler la méthode à tester
        ResponseEntity<?> response = teacherController.findAll();

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof List);
    }
}