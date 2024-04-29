package com.openclassrooms.starterjwt.security.services.integration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserDetailsServiceImplIntegrationTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

     @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        userRepository.deleteAll();
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    @Test
    void shouldLoadUserByUsername() {
        String username = "test@example.com";

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

        assertEquals(username, userDetails.getUsername());
        assertEquals("john", userDetailsImpl.getFirstName());
        assertEquals("doe", userDetailsImpl.getLastName());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionForInvalidUsername() {
        String username = "invalidUser";

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}
