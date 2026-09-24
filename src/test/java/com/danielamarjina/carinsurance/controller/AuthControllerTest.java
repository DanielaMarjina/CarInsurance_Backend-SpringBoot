package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.config.SecurityConfig;
import com.danielamarjina.carinsurance.dto.request.LoginRequest;
import com.danielamarjina.carinsurance.dto.request.RegisterRequest;
import com.danielamarjina.carinsurance.dto.response.LoginResponse;
import com.danielamarjina.carinsurance.service.AuthService;
import com.danielamarjina.carinsurance.service.CustomUserDetailsService;
import com.danielamarjina.carinsurance.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void register_shouldCreateUser() throws Exception {
        RegisterRequest registerRequest=new RegisterRequest();
        registerRequest.setUsername("john123");
        registerRequest.setEmail("john@test.com");
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());
        verify(authService).register(any(RegisterRequest.class));

    }

    @Test
    void register_shouldNotCreateUser() throws Exception {
        RegisterRequest registerRequest=new RegisterRequest();
        registerRequest.setUsername("john");
        registerRequest.setEmail("john@test.com");
        registerRequest.setPassword("123");

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
        verify(authService,never()).register(any(RegisterRequest.class));

    }

    @Test
    void login_shouldReturnLoginResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@test.com");
        loginRequest.setPassword("password123");

        LoginResponse loginResponse = new LoginResponse();

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(loginResponse);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void login_shouldNotLoginWithInvalidRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("");

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

}