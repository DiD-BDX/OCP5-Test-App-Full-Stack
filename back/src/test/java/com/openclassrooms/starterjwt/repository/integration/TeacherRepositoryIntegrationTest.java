package com.openclassrooms.starterjwt.repository.integration;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TeacherRepositoryIntegrationTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void whenSave_thenFindsById() {
        // given
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);

        // when
        Optional<Teacher> found = teacherRepository.findById(teacher.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getLastName()).isEqualTo(teacher.getLastName());
        assertThat(found.get().getFirstName()).isEqualTo(teacher.getFirstName());
    }
}
