// Ce package contient les tests pour les utilitaires JWT
package com.openclassrooms.starterjwt.security.jwt;

// Importations nécessaires
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

// Annotation pour indiquer qu'il s'agit d'un test Spring Boot
@SpringBootTest
public class JwtUtilsTest {

    // Injection des utilitaires JWT
    @Autowired
    private JwtUtils jwtUtils;

    // Injection de la valeur de la clé secrète JWT à partir des propriétés de l'application
    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    // Test pour vérifier que le générateur de token JWT fonctionne correctement
    @Test
    void shouldGenerateJwtToken() {
        // Création d'un utilisateur fictif
        UserDetailsImpl userDetails = new UserDetailsImpl(
            1L,  // id
            "testUser",  // username
            "Test",  // firstName
            "User",  // lastName
            false,  // admin
            "password"  // password
        );
        // Création d'une authentification fictive
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Génération du token JWT
        String token = jwtUtils.generateJwtToken(authentication);

        // Vérification que le token n'est pas null, qu'il est valide et qu'il contient le bon nom d'utilisateur
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("testUser", jwtUtils.getUserNameFromJwtToken(token));
    }

    // Test pour vérifier que la validation d'un token invalide échoue
    @Test
    void shouldNotValidateInvalidToken() {
        String token = "invalidToken";

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    // Test pour vérifier que la validation d'un token valide réussit
    @Test
    void shouldValidateValidToken() {
        // Création d'un token JWT valide
        String token = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))  // 1 hour expiration
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    // Test pour vérifier que la validation d'un token avec une signature invalide échoue
    @Test
    void shouldNotValidateTokenWithInvalidSignature() {
        // Création d'un token JWT avec une signature invalide
        String token = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))  // 1 hour expiration
            .signWith(SignatureAlgorithm.HS512, "wrongSecret")
            .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    // Test pour vérifier que la validation d'un token expiré échoue
    @Test
    void shouldNotValidateExpiredToken() {
        // Création d'un token JWT expiré
        String token = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() - 60 * 60 * 1000))  // 1 hour ago
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }
}