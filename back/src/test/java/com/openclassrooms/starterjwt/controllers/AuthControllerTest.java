package com.openclassrooms.starterjwt.controllers;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testAuthenticateUser() {
        // Créer un objet LoginRequest
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");

        // Créer un objet Authentication
        Authentication authentication = mock(Authentication.class);

        // Créer un objet UserDetailsImpl
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(1L)
            .username("test@test.com")
            .password("password")
            .firstName(null)
            .lastName(null)
            .admin(false)
            .build();

        // Configurer le mock pour renvoyer ces objets
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");

        // Appeler la méthode à tester
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Vérifier que la méthode authenticate a été appelée
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof JwtResponse);
    }

    @Test
    public void testRegisterUser() {
        // Créer un objet SignupRequest
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("firstName");
        signupRequest.setLastName("lastName");

        // Configurer le mock pour renvoyer ces objets
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Appeler la méthode à tester
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Vérifier que la méthode save a été appelée
        verify(userRepository, times(1)).save(any(User.class));

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
    }
}