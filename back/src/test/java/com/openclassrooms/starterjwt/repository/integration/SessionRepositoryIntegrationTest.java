// Ce package contient les tests d'intégration pour le repository de session
package com.openclassrooms.starterjwt.repository.integration;

// Importations nécessaires
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;

// Annotation pour indiquer qu'il s'agit d'un test de données JPA
@DataJpaTest
public class SessionRepositoryIntegrationTest {

    // Injection du gestionnaire d'entités de test
    @Autowired
    private TestEntityManager entityManager;

    // Injection du repository de session
    @Autowired
    private SessionRepository sessionRepository;

    // Test pour vérifier que la méthode findById retourne la bonne session
    @Test
    public void whenFindById_thenReturnSession() {
        // given
        // Création d'une session
        Session session = new Session();
        session.setDate(new Date());
        session.setName("Test Session");
        session.setDescription("Test Session");
        // Persistance de la session dans la base de données
        entityManager.persist(session);
        entityManager.flush();

        // when
        // Recherche de la session par son ID
        Session found = sessionRepository.findById(session.getId()).orElse(null);

        // then
        // Vérification que la description de la session trouvée est la même que celle de la session initiale
        assertThat(found.getDescription()).isEqualTo(session.getDescription());
    }
}