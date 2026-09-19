package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.OwnerPatchRequest;
import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import com.danielamarjina.carinsurance.service.CustomUserDetailsService;
import com.danielamarjina.carinsurance.service.JWTService;
import com.danielamarjina.carinsurance.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OwnerService ownerService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllOwners_shouldReturnAllOwners() throws Exception {
        List<Owner> owners = List.of(
                new Owner(),
                new Owner()
        );
        when(ownerService.getAllOwners(null))
                .thenReturn(owners);
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk());
        verify(ownerService).getAllOwners(null);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllOwners_shouldFilterOwnersByCategory() throws Exception {
        List<Owner> owners = List.of(
                new Owner(),
                new Owner()
        );
        DriverLicenseCategory category = DriverLicenseCategory.A;
        when(ownerService.getAllOwners(category))
                .thenReturn(owners);
        mockMvc.perform(get("/owners")
                        .param("driverLicenseCategory", category.name()))
                .andExpect(status().isOk());
        verify(ownerService).getAllOwners(category);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createOwner_shouldReturnNewOwner() throws Exception {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setName("John Doe");
        ownerRequest.setBirthdate(LocalDate.of(1995, 5, 10));
        ownerRequest.setYearOfDriverLicense(2018);
        ownerRequest.setDriverLicenseCategory(DriverLicenseCategory.B);
        ownerRequest.setEmail("john.doe@test.com");
        OwnerResponse ownerResponse = new OwnerResponse();
        when(ownerService.createOwner(any(OwnerRequest.class)))
                .thenReturn(ownerResponse);
        mockMvc.perform(post("/owners")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerRequest)))
                .andExpect(status().isOk());
        verify(ownerService).createOwner(any(OwnerRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createOwner_shouldNotReturnNewOwner() throws Exception {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setName("John Doe");
        ownerRequest.setBirthdate(LocalDate.of(2027, 5, 10));
        ownerRequest.setYearOfDriverLicense(2018);
        ownerRequest.setDriverLicenseCategory(DriverLicenseCategory.B);
        ownerRequest.setEmail("john.doe@test.com");
        mockMvc.perform(post("/owners")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerRequest)))
                .andExpect(status().isBadRequest());
        verify(ownerService, never()).createOwner(any(OwnerRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void findOwnerByEmail_shouldFindOwner() throws Exception {
        OwnerResponse ownerResponse = new OwnerResponse();
        String email = "test@gmail.com";
        when(ownerService.findOwnerByEmail(email))
                .thenReturn(ownerResponse);
        mockMvc.perform(get("/owners/search")
                        .param("email", email))
                .andExpect(status().isOk());
        verify(ownerService).findOwnerByEmail(email);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void findOwnerById_shouldFindOwner() throws Exception {
        OwnerResponse ownerResponse = new OwnerResponse();
        UUID id = UUID.randomUUID();
        when(ownerService.findOwnerById(id))
                .thenReturn(ownerResponse);
        mockMvc.perform(get("/owners/{id}", id))
                .andExpect(status().isOk());
        verify(ownerService).findOwnerById(id);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updateOwner_shouldReturnUpdatedOwner() throws Exception {
        OwnerResponse ownerResponse = new OwnerResponse();
        UUID id = UUID.randomUUID();
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setName("John Doe");
        ownerRequest.setBirthdate(LocalDate.of(1995, 5, 10));
        ownerRequest.setYearOfDriverLicense(2018);
        ownerRequest.setDriverLicenseCategory(DriverLicenseCategory.B);
        ownerRequest.setEmail("john.doe@test.com");
        when(ownerService.updateOwner(eq(id), any(OwnerRequest.class)))
                .thenReturn(ownerResponse);
        mockMvc.perform(put("/owners/update")
                        .with(csrf())
                        .param("id", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerRequest)))
                .andExpect(status().isOk());
        verify(ownerService).updateOwner(eq(id),any(OwnerRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void patchOwner_shouldReturnPatchedOwner() throws Exception {
        OwnerResponse ownerResponse = new OwnerResponse();
        UUID id = UUID.randomUUID();
        OwnerPatchRequest ownerPatchRequest=new OwnerPatchRequest();
        ownerPatchRequest.setEmail("john.doe@test.com");
        when(ownerService.patchOwner(eq(id), any(OwnerPatchRequest.class)))
                .thenReturn(ownerResponse);
        mockMvc.perform(patch("/owners/id")
                        .with(csrf())
                        .param("id", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerPatchRequest)))
                .andExpect(status().isOk());
        verify(ownerService).patchOwner(eq(id),any(OwnerPatchRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteOwner_shouldDeleteOwner() throws Exception{
        UUID id=UUID.randomUUID();
        mockMvc.perform(delete("/owners/{id}",id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(ownerService).deleteOwner(id);
    }
}