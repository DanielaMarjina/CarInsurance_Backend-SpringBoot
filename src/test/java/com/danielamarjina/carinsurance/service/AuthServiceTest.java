package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.LoginRequest;
import com.danielamarjina.carinsurance.dto.request.RegisterRequest;
import com.danielamarjina.carinsurance.dto.response.LoginResponse;
import com.danielamarjina.carinsurance.entity.User;
import com.danielamarjina.carinsurance.exception.UserAlreadyExistsException;
import com.danielamarjina.carinsurance.mapper.UserMapper;
import com.danielamarjina.carinsurance.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldRegisterUser_whenUsernameAndEmailAreAvailable() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("daniela");
        request.setEmail("daniela@gmail.com");
        request.setPassword("password123");

        User user = new User();

        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(false);

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        authService.register(request);

        assertEquals("encodedPassword", user.getPassword());

        verify(userRepository).existsByUsername(request.getUsername());
        verify(userRepository).existsByEmail(request.getEmail());
        verify(userMapper).toEntity(request);
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(user);
    }


    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("daniela");
        request.setEmail("daniela@gmail.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(userRepository).existsByUsername(request.getUsername());
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("daniela");
        request.setEmail("daniela@gmail.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(false);

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(userRepository).existsByUsername(request.getUsername());
        verify(userRepository).existsByEmail(request.getEmail());
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("daniela@gmail.com");
        request.setPassword("password123");

        String token = "jwt-token";

        when(jwtService.generateToken(request.getEmail()))
                .thenReturn(token);

        LoginResponse result = authService.login(request);

        assertEquals(token, result.getToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        verify(jwtService).generateToken(request.getEmail());
    }


    @Test
    void login_shouldPropagateException_whenAuthenticationFails() {
        LoginRequest request = new LoginRequest();
        request.setEmail("daniela@gmail.com");
        request.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        verify(authenticationManager).authenticate(any());

    }
}