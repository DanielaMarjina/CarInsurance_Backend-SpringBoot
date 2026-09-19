package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.CarRequest;
import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.enums.CarCategory;
import com.danielamarjina.carinsurance.service.CarService;
import com.danielamarjina.carinsurance.service.CustomUserDetailsService;
import com.danielamarjina.carinsurance.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.*;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.List;
import java.util.UUID;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
@Import(CarControllerTest.MethodSecurityTestConfig.class)
class CarControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllCars_shouldReturnAllCars() throws Exception {
        List<CarResponse> carResponses = List.of(
                new CarResponse(),
                new CarResponse()
        );
        when(carService.getAllCars(null, null, null, null))
                .thenReturn(carResponses);
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk());
        verify(carService).getAllCars(null, null, null, null);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllCars_shouldReturnFilteredCars() throws Exception {
        List<CarResponse> cars = List.of(
                new CarResponse(),
                new CarResponse()
        );

        String make = "Volkswagen";
        String model = "Golf";
        CarCategory category = CarCategory.EURO6;
        UUID ownerId = UUID.randomUUID();

        when(carService.getAllCars(make, model, category, ownerId))
                .thenReturn(cars);

        mockMvc.perform(get("/cars")
                        .param("make", make)
                        .param("model", model)
                        .param("category", category.name())
                        .param("ownerId", ownerId.toString()))
                .andExpect(status().isOk());

        verify(carService).getAllCars(make, model, category, ownerId);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createCar_shouldCreateCar() throws Exception {
        CarResponse carResponse = new CarResponse();
        CarRequest carRequest = new CarRequest();
        carRequest.setVin("WVWZZZ1JZXW000001");
        carRequest.setMake("Volkswagen");
        carRequest.setModel("Golf");
        carRequest.setYearOfManufacture(2020);
        carRequest.setCategory(CarCategory.EURO6);
        carRequest.setCc(1598);
        carRequest.setPower(110);
        carRequest.setOwnerId(UUID.randomUUID());
        when(carService.createCar(any(CarRequest.class)))
                .thenReturn(carResponse);
        mockMvc.perform(post("/cars")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isOk());
        verify(carService).createCar(any(CarRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createCar_shouldNotCreateCar() throws Exception {
        CarRequest carRequest = new CarRequest();
        carRequest.setVin("WVWZZZ1JZXW000001");
        carRequest.setMake("Volkswagen");
        carRequest.setModel("Golf");
        carRequest.setYearOfManufacture(1800);
        carRequest.setCategory(CarCategory.EURO6);
        carRequest.setCc(1598);
        carRequest.setPower(110);
        carRequest.setOwnerId(UUID.randomUUID());
        mockMvc.perform(post("/cars")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isBadRequest());
        verify(carService, never()).createCar(any(CarRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getCar_shouldGetCar() throws Exception{
        UUID id=UUID.randomUUID();
        CarResponse carResponse=new CarResponse();
        when(carService.getCarById(id))
                .thenReturn(carResponse);
        mockMvc.perform(get("/cars/{id}",id))
                .andExpect(status().isOk());
        verify(carService).getCarById(id);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCar_shouldDeleteCar() throws Exception{
        UUID id=UUID.randomUUID();
        mockMvc.perform(delete("/cars/{id}",id)
                .with(csrf()))
                .andExpect(status().isNoContent());
        verify(carService).deleteCar(id);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void deleteCar_shouldNotDeleteCar() throws Exception{
        UUID id=UUID.randomUUID();
        mockMvc.perform(delete("/cars/{id}",id)
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(carService,never()).deleteCar(id);
    }

}