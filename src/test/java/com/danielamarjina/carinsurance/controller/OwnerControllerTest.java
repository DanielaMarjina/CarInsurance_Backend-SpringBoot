package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.service.CustomUserDetailsService;
import com.danielamarjina.carinsurance.service.JWTService;
import com.danielamarjina.carinsurance.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerService ownerService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllOwners_shouldReturnAllOwners() throws Exception{
        List<Owner> owners= List.of(
                new Owner(),
                new Owner()
        );
        when(ownerService.getAllOwners(null))
                .thenReturn(owners);
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk());
        verify(ownerService).getAllOwners(null);
    }
}