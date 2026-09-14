package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.entity.User;
import com.danielamarjina.carinsurance.enums.Role;
import com.danielamarjina.carinsurance.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;


    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        String email = "daniela@gmail.com";
        String password = "encodedPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.EMPLOYEE);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        UserDetails result =
                customUserDetailsService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals(password, result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_EMPLOYEE")));

        verify(userRepository).findByEmail(email);
    }


    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        String email = "notfound@gmail.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(email)
        );

        verify(userRepository).findByEmail(email);
    }
}