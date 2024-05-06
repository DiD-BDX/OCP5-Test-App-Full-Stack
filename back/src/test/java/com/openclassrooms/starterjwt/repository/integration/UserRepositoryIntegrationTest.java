// Ce package contient les tests d'intégration pour le repository d'utilisateur
package com.openclassrooms.starterjwt.repository.integration;

// Importations nécessaires
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

// Annotation pour indiquer qu'il s'agit d'un test de données JPA
@DataJpaTest
public class UserRepositoryIntegrationTest {

    // Injection du repository d'utilisateur
    @Autowired
    private UserRepository userRepository;

    // Test pour vérifier que la méthode findByEmail retourne le bon utilisateur
    @Test
    public void whenFindByEmail_thenReturnUser() {
        // given
        // Création d'un utilisateur
        User user = new User();
        user.setEmail("test@example.com");
        // Enregistrement de l'utilisateur dans la base de données
        user = userRepository.save(user);

        // when
        // Recherche de l'utilisateur par son email
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // then
        // Vérification que l'utilisateur a été trouvé et que son email est correct
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    // Test pour vérifier que la méthode existsByEmail retourne true si l'email existe
    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        // given
        // Création d'un utilisateur
        User user = new User();
        user.setEmail("test@example.com");
        // Enregistrement de l'utilisateur dans la base de données
        user = userRepository.save(user);

        // when
        // Vérification de l'existence de l'email
        Boolean exists = userRepository.existsByEmail(user.getEmail());

        // then
        // Vérification que l'email existe
        assertThat(exists).isTrue();
    }
}