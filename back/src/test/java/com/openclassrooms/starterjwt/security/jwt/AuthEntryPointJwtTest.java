// Ce package contient les tests pour le point d'entrée JWT
package com.openclassrooms.starterjwt.security.jwt;

// Importations nécessaires
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import javax.servlet.http.HttpServletResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthEntryPointJwtTest {

    // Déclaration de l'objet à tester
    private AuthEntryPointJwt authEntryPointJwt;

    // Méthode exécutée avant chaque test
    @BeforeEach
    void setUp() {
        // Initialisation de l'objet à tester
        authEntryPointJwt = new AuthEntryPointJwt();
    }

    // Test pour vérifier que le point d'entrée JWT renvoie une réponse non autorisée
    @Test
    void shouldReturnUnauthorizedResponse() throws Exception {
        // Création d'une requête et d'une réponse fictives
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/testPath");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // Création d'une exception d'authentification
        AuthenticationCredentialsNotFoundException authException = new AuthenticationCredentialsNotFoundException("Test Exception");

        // Appel de la méthode à tester
        authEntryPointJwt.commence(request, response, authException);

        // Vérification que le statut de la réponse est non autorisé
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        // Vérification que le contenu de la réponse contient les bonnes informations
        assertTrue(response.getContentAsString().contains("\"status\":401"));
        assertTrue(response.getContentAsString().contains("\"error\":\"Unauthorized\""));
        assertTrue(response.getContentAsString().contains("\"message\":\"Test Exception\""));
        assertTrue(response.getContentAsString().contains("\"path\":\"/testPath\""));
    }
}