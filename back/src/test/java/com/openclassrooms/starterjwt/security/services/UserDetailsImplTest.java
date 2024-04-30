package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserDetailsImplTest {
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = UserDetailsImpl.builder()
            .id(1L)
            .username("username")
            .firstName("firstName")
            .lastName("lastName")
            .admin(true)
            .password("password")
            .build();
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities.isEmpty());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testEquals() {
        UserDetailsImpl sameUserDetails = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl differentUserDetails = UserDetailsImpl.builder().id(2L).build();

        assertTrue(userDetails.equals(sameUserDetails));
        assertTrue(!userDetails.equals(differentUserDetails));
    }
   
}