package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.ClaimRequest;
import com.danielamarjina.carinsurance.dto.response.ClaimResponse;
import com.danielamarjina.carinsurance.service.ClaimService;
import com.danielamarjina.carinsurance.service.CustomUserDetailsService;
import com.danielamarjina.carinsurance.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClaimController.class)
@Import(ClaimControllerTest.MethodSecurityTestConfig.class)
class ClaimControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClaimService claimService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllClaims_shouldReturnAllClaims() throws Exception {

        List<ClaimResponse> claims = List.of(
                new ClaimResponse(),
                new ClaimResponse()
        );

        when(claimService.getAllClaims())
                .thenReturn(claims);

        mockMvc.perform(get("/claims"))
                .andExpect(status().isOk());

        verify(claimService).getAllClaims();
    }


    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createClaim_shouldCreateClaim() throws Exception {

        UUID carId = UUID.randomUUID();

        ClaimRequest claimRequest = new ClaimRequest();
        claimRequest.setClaimDate(LocalDate.of(2026, 9, 20));
        claimRequest.setDescription("Minor accident involving another vehicle");
        claimRequest.setAmount(new BigDecimal("1500.00"));

        ClaimResponse claimResponse = new ClaimResponse();

        when(claimService.createClaim(
                eq(carId),
                any(ClaimRequest.class)))
                .thenReturn(claimResponse);

        mockMvc.perform(post("/cars/{carId}/claims", carId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claimRequest)))
                .andExpect(status().isOk());

        verify(claimService).createClaim(
                eq(carId),
                any(ClaimRequest.class));
    }


    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createClaim_shouldNotCreateClaim() throws Exception {

        UUID carId = UUID.randomUUID();

        ClaimRequest claimRequest = new ClaimRequest();
        claimRequest.setClaimDate(LocalDate.of(2026, 9, 20));
        claimRequest.setDescription("");
        claimRequest.setAmount(new BigDecimal("1500.00"));

        mockMvc.perform(post("/cars/{carId}/claims", carId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claimRequest)))
                .andExpect(status().isBadRequest());

        verify(claimService, never())
                .createClaim(eq(carId), any(ClaimRequest.class));
    }
}