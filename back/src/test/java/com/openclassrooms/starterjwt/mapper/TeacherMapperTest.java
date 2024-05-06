// Ce package contient les tests pour le mapper de professeur
package com.openclassrooms.starterjwt.mapper;

// Importations nécessaires
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

// Annotation pour indiquer qu'il s'agit d'un test Spring Boot
@SpringBootTest
public class TeacherMapperTest {

    // Injection du mapper de professeur
    @Autowired
    private TeacherMapper teacherMapper;

    // Test pour vérifier que le mapper transforme correctement un DTO de professeur en entité de professeur
    @Test
    void shouldMapDtoToEntity() {
        // Création d'un DTO de professeur
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");

        // Transformation du DTO de professeur en entité de professeur
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Vérification que les données de l'entité de professeur sont correctes
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
    }

    // Test pour vérifier que le mapper transforme correctement une entité de professeur en DTO de professeur
    @Test
    void shouldMapEntityToDto() {
        // Création d'une entité de professeur
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");

        // Transformation de l'entité de professeur en DTO de professeur
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Vérification que les données du DTO de professeur sont correctes
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
    }

    // Test pour vérifier que le mapper transforme correctement une liste de DTO de professeur en liste d'entités de professeur
    @Test
    void shouldConvertDtoListToEntityList() {
        // Création de deux DTO de professeur
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setLastName("Doe");
        teacherDto1.setFirstName("John");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("White");

        // Création d'une liste de DTO de professeur
        List<TeacherDto> dtoList = Arrays.asList(teacherDto1, teacherDto2);

        // Transformation de la liste de DTO de professeur en liste d'entités de professeur
        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

        // Vérification que la liste d'entités de professeur est correcte
        assertNotNull(entityList);
        assertEquals(2, entityList.size());

        Teacher teacher1 = entityList.get(0);
        assertEquals(1L, teacher1.getId());
        assertEquals("Doe", teacher1.getLastName());
        assertEquals("John", teacher1.getFirstName());

        Teacher teacher2 = entityList.get(1);
        assertEquals(2L, teacher2.getId());
        assertEquals("White", teacher2.getLastName());
        assertEquals("Jane", teacher2.getFirstName());
    }
}