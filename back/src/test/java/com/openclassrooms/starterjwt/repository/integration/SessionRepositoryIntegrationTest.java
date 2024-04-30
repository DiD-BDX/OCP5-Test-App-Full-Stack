package com.openclassrooms.starterjwt.repository.integration;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

@DataJpaTest
public class SessionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void whenFindById_thenReturnSession() {
        // given
        Session session = new Session();
        session.setDate(new Date());
        session.setName("Test Session");
        session.setDescription("Test Session");
        entityManager.persist(session);
        entityManager.flush();

        // when
        Session found = sessionRepository.findById(session.getId()).orElse(null);

        // then
        assertThat(found.getDescription()).isEqualTo(session.getDescription());
    }

    // Vous pouvez ajouter d'autres méthodes de test pour tester d'autres fonctionnalités
}