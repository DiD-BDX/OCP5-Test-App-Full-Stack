package com.openclassrooms.starterjwt.security.jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@SpringBootTest
@TestPropertySource(locations="classpath:application.properties")
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Test
    void shouldGenerateJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
            1L,  // id
            "testUser",  // username
            "Test",  // firstName
            "User",  // lastName
            false,  // admin
            "password"  // password
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("testUser", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    void shouldNotValidateInvalidToken() {
        String token = "invalidToken";

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void shouldValidateValidToken() {
        String token = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))  // 1 hour expiration
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void shouldNotValidateTokenWithInvalidSignature() {
        String token = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))  // 1 hour expiration
            .signWith(SignatureAlgorithm.HS512, "wrongSecret")
            .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void shouldNotValidateExpiredToken() {
        String token = Jwts.builder()
            .setSubject("testUser")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() - 60 * 60 * 1000))  // 1 hour ago
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }
}
