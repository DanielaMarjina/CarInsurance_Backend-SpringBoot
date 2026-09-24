package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.InsurancePolicyRequest;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyIsValidResponse;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyResponse;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import com.danielamarjina.carinsurance.service.CustomUserDetailsService;
import com.danielamarjina.carinsurance.service.InsurancePolicyService;
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
import org.springframework.test.web.servlet.*;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InsurancePolicyController.class)
@Import(InsurancePolicyControllerTest.MethodSecurityTestConfig.class)
class InsurancePolicyControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig{

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InsurancePolicyService insurancePolicyService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllPolicies_shouldReturnAllPolicies() throws Exception {
        List<InsurancePolicyResponse> insurancePolicyList=List.of(
                new InsurancePolicyResponse(),
                new InsurancePolicyResponse()
        );
        when(insurancePolicyService.getAllPolicies(null,null))
                .thenReturn(insurancePolicyList);
        mockMvc.perform(get("/policies")).andExpect(status().isOk());
        verify(insurancePolicyService).getAllPolicies(null,null);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllPolicies_shouldReturnPoliciesFilteredByProvider() throws Exception {
        String provider="Insurance Group";
        List<InsurancePolicyResponse> insurancePolicyList=List.of(
                new InsurancePolicyResponse(),
                new InsurancePolicyResponse()
        );
        when(insurancePolicyService.getAllPolicies(provider,null))
                .thenReturn(insurancePolicyList);
        mockMvc.perform(get("/policies")
                .param("provider",provider))
                .andExpect(status().isOk());
        verify(insurancePolicyService).getAllPolicies(provider,null);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllPolicies_shouldReturnPoliciesFilteredByStatus() throws Exception {
        InsurancePolicyStatus status=InsurancePolicyStatus.ACTIVE;
        List<InsurancePolicyResponse> insurancePolicyList=List.of(
                new InsurancePolicyResponse(),
                new InsurancePolicyResponse()
        );
        when(insurancePolicyService.getAllPolicies(null,status))
                .thenReturn(insurancePolicyList);
        mockMvc.perform(get("/policies")
                .param("status",status.name()))
                .andExpect(status().isOk());
        verify(insurancePolicyService).getAllPolicies(null,status);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createPolicy_shouldCreatePolicy() throws Exception {
        InsurancePolicyResponse insurancePolicyResponse=new InsurancePolicyResponse();
        UUID carId=UUID.randomUUID();
        InsurancePolicyRequest insurancePolicyRequest=new InsurancePolicyRequest();
        insurancePolicyRequest.setProvider("Allianz");
        insurancePolicyRequest.setStartDate(LocalDate.of(2026, 9, 1));
        insurancePolicyRequest.setEndDate(LocalDate.of(2027, 9, 1));
        insurancePolicyRequest.setPaidAmount(new BigDecimal("1250.50"));

        when(insurancePolicyService.createPolicy(eq(carId),any(InsurancePolicyRequest.class)))
                .thenReturn(insurancePolicyResponse);
        mockMvc.perform(post("/cars/{carId}/policies",carId)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(insurancePolicyRequest)))
                .andExpect(status().isOk());
        verify(insurancePolicyService).createPolicy(eq(carId),any(InsurancePolicyRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createPolicy_shouldNotCreatePolicy() throws Exception {
        UUID carId=UUID.randomUUID();
        InsurancePolicyRequest insurancePolicyRequest=new InsurancePolicyRequest();
        insurancePolicyRequest.setProvider("Allianz");
        insurancePolicyRequest.setStartDate(LocalDate.of(2028, 9, 1));
        insurancePolicyRequest.setEndDate(LocalDate.of(2027, 9, 1));
        insurancePolicyRequest.setPaidAmount(new BigDecimal("1250.50"));

        mockMvc.perform(post("/cars/{carId}/policies",carId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insurancePolicyRequest)))
                .andExpect(status().isBadRequest());
        verify(insurancePolicyService,never()).createPolicy(eq(carId),any(InsurancePolicyRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getValidityPolicy_shouldReturnValidity() throws Exception {
        UUID carId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 9, 24);

        InsurancePolicyIsValidResponse response =
                new InsurancePolicyIsValidResponse();

        when(insurancePolicyService.getValidityPolicy(carId, date))
                .thenReturn(response);

        mockMvc.perform(get("/cars/{carId}/insurance-valid", carId)
                        .param("date", date.toString()))
                .andExpect(status().isOk());

        verify(insurancePolicyService)
                .getValidityPolicy(carId, date);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getActivePolicy_shouldReturnActivePolicy() throws Exception {
        UUID carId = UUID.randomUUID();

        InsurancePolicyResponse response =
                new InsurancePolicyResponse();

        when(insurancePolicyService.getActivePolicy(carId))
                .thenReturn(response);

        mockMvc.perform(get("/policies/active-policy")
                        .param("carId", carId.toString()))
                .andExpect(status().isOk());

        verify(insurancePolicyService)
                .getActivePolicy(carId);
    }

}