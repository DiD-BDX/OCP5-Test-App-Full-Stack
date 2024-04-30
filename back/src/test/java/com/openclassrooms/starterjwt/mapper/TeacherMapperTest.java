package com.openclassrooms.starterjwt.mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void shouldMapDtoToEntity() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
    }

    @Test
    void shouldMapEntityToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
    }

    @Test
    void shouldConvertDtoListToEntityList() {
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setLastName("Doe");
        teacherDto1.setFirstName("John");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("White");

        List<TeacherDto> dtoList = Arrays.asList(teacherDto1, teacherDto2);

        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

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
