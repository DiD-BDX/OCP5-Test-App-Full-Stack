package com.openclassrooms.starterjwt.controllers;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void testFindById() {
        // Créer un objet User
        User user = new User();
        user.setId(1L);

        // Créer un objet UserDto
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        // Configurer le mock pour renvoyer ces objets
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Appeler la méthode à tester
        ResponseEntity<?> response = userController.findById("1");

        // Vérifier que le résultat est correct
        assertTrue(response.getBody() instanceof UserDto);
        assertEquals(1L, ((UserDto) response.getBody()).getId());
    }

    @Test
    public void testDeleteUser() {
        // Créer un objet User
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        // Créer un objet UserDetailsImpl
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(1L)
            .username("test@test.com")
            .password("password")
            .build();

        // Créer un objet Authentication
        Authentication authentication = mock(Authentication.class);

        // Créer un objet SecurityContext
        SecurityContext securityContext = mock(SecurityContext.class);

        // Configurer le mock pour renvoyer ces objets
        when(userService.findById(1L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Configurer SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);

        // Appeler la méthode à tester
        ResponseEntity<?> response = userController.save("1");

        // Vérifier que le résultat est correct
        assertEquals(200, response.getStatusCodeValue());
    }
}
