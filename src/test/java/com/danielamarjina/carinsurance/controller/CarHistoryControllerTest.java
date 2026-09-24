package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.response.CarHistoryResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.enums.CarHistoryType;
import com.danielamarjina.carinsurance.service.CarHistoryService;
import com.danielamarjina.carinsurance.service.CustomUserDetailsService;
import com.danielamarjina.carinsurance.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarHistoryController.class)
@Import(CarHistoryControllerTest.MethodSecurityTestConfig.class)
class CarHistoryControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarHistoryService carHistoryService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getCarHistory_shouldReturnAllHistory() throws Exception {

        UUID carId = UUID.randomUUID();

        List<CarHistoryResponse> history = List.of(
               new CarHistoryResponse(),
                new CarHistoryResponse()
        );

        when(carHistoryService.getCarHistory(carId, null))
                .thenReturn(history);

        mockMvc.perform(get("/cars/{carId}/history", carId))
                .andExpect(status().isOk());

        verify(carHistoryService)
                .getCarHistory(carId, null);
    }


    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getCarHistory_shouldReturnPolicyHistory() throws Exception {

        UUID carId = UUID.randomUUID();
        CarHistoryType type=CarHistoryType.POLICY;

        List<CarHistoryResponse> history = List.of(
                new CarHistoryResponse(),
                new CarHistoryResponse()
        );

        when(carHistoryService.getCarHistory(carId, type))
                .thenReturn(history);

        mockMvc.perform(get("/cars/{carId}/history", carId)
                        .param("type", type.name()))
                .andExpect(status().isOk());

        verify(carHistoryService)
                .getCarHistory(carId, type);
    }


    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getCarHistory_shouldReturnClaimHistory() throws Exception {

        UUID carId = UUID.randomUUID();
        CarHistoryType type=CarHistoryType.CLAIM;

        List<CarHistoryResponse> history = List.of(
                new CarHistoryResponse(),
                new CarHistoryResponse()
        );

        when(carHistoryService.getCarHistory(carId, type))
                .thenReturn(history);

        mockMvc.perform(get("/cars/{carId}/history", carId)
                        .param("type", type.name()))
                .andExpect(status().isOk());

        verify(carHistoryService)
                .getCarHistory(carId, type);
    }
}