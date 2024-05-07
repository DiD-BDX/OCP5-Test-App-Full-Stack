// Ce package contient les tests pour la classe TeacherService
package com.openclassrooms.starterjwt.services;

// Importations nécessaires
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// Classe de test pour TeacherService
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    // Injection des mocks
    @InjectMocks
    private TeacherService teacherService;
    @Mock
    private TeacherRepository teacherRepository;

    // Test pour vérifier que la méthode findAll() fonctionne correctement
    @Test
    public void testFindAll() {
        // Création d'un objet Teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Configuration du mock pour retourner cet objet lors de l'appel à teacherRepository.findAll()
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher));

        // Appel de la méthode à tester
        List<Teacher> teachers = teacherService.findAll();

        // Vérification que la méthode findAll() du repository a été appelée une fois
        verify(teacherRepository, times(1)).findAll();

        // Vérification que le résultat est correct
        assertEquals(1, teachers.size());
        assertEquals(teacher, teachers.get(0));
    }

    // Test pour vérifier que la méthode findById() fonctionne correctement
    @Test
    public void testFindById() {
        // Création d'un objet Teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Configuration du mock pour retourner cet objet lors de l'appel à teacherRepository.findById()
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Appel de la méthode à tester
        Teacher foundTeacher = teacherService.findById(1L);

        // Vérification que la méthode findById() du repository a été appelée une fois avec l'argument 1L
        verify(teacherRepository, times(1)).findById(1L);

        // Vérification que le résultat est correct
        assertEquals(teacher, foundTeacher);
    }
}