// Ce package contient les tests d'intégration pour le repository de professeur
package com.openclassrooms.starterjwt.repository.integration;

// Importations nécessaires
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

// Annotation pour indiquer qu'il s'agit d'un test de données JPA
@DataJpaTest
public class TeacherRepositoryIntegrationTest {

    // Injection du repository de professeur
    @Autowired
    private TeacherRepository teacherRepository;

    // Test pour vérifier que la méthode save fonctionne correctement
    @Test
    public void whenSave_thenFindsById() {
        // given
        // Création d'un professeur
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        // Enregistrement du professeur dans la base de données
        teacher = teacherRepository.save(teacher);

        // when
        // Recherche du professeur par son ID
        Optional<Teacher> found = teacherRepository.findById(teacher.getId());

        // then
        // Vérification que le professeur a été trouvé et que ses informations sont correctes
        assertThat(found).isPresent();
        assertThat(found.get().getLastName()).isEqualTo(teacher.getLastName());
        assertThat(found.get().getFirstName()).isEqualTo(teacher.getFirstName());
    }
}