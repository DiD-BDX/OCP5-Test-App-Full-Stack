package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@test.com");

        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getFirstName(), userDetails.getFirstName());
        assertEquals(user.getLastName(), userDetails.getLastName());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("test@test.com");
        });
    }
}