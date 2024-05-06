// Ce package contient les tests pour la classe UserDetailsImpl
package com.openclassrooms.starterjwt.security.services;

// Importations nécessaires
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

// Classe de test pour UserDetailsImpl
public class UserDetailsImplTest {
    // Instance de UserDetailsImpl à tester
    private UserDetailsImpl userDetails;

    // Méthode exécutée avant chaque test pour initialiser l'instance de UserDetailsImpl
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

    // Test pour vérifier que la méthode getAuthorities() retourne une collection vide
    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities.isEmpty());
    }

    // Test pour vérifier que la méthode isAccountNonExpired() retourne true
    @Test
    public void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    // Test pour vérifier que la méthode isAccountNonLocked() retourne true
    @Test
    public void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    // Test pour vérifier que la méthode isCredentialsNonExpired() retourne true
    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    // Test pour vérifier que la méthode isEnabled() retourne true
    @Test
    public void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    // Test pour vérifier que la méthode equals() fonctionne correctement
    @Test
    public void testEquals() {
        // Création de deux autres instances de UserDetailsImpl
        UserDetailsImpl sameUserDetails = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl differentUserDetails = UserDetailsImpl.builder().id(2L).build();

        // Vérification que userDetails est égal à sameUserDetails et différent de differentUserDetails
        assertTrue(userDetails.equals(sameUserDetails));
        assertTrue(!userDetails.equals(differentUserDetails));
    }
}