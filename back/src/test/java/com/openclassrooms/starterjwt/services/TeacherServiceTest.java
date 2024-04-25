package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @Test
    public void testFindAll() {
        // Créer un objet Teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Configurer le mock pour renvoyer cet objet
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher));

        // Appeler la méthode à tester
        List<Teacher> teachers = teacherService.findAll();

        // Vérifier que la méthode findAll a été appelée
        verify(teacherRepository, times(1)).findAll();

        // Vérifier que le résultat est correct
        assertEquals(1, teachers.size());
        assertEquals(teacher, teachers.get(0));
    }

    @Test
    public void testFindById() {
        // Créer un objet Teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Configurer le mock pour renvoyer cet objet
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Appeler la méthode à tester
        Teacher foundTeacher = teacherService.findById(1L);

        // Vérifier que la méthode findById a été appelée
        verify(teacherRepository, times(1)).findById(1L);

        // Vérifier que le résultat est correct
        assertEquals(teacher, foundTeacher);
    }
}