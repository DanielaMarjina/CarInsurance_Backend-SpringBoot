package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.InsurancePolicyRequest;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyIsValidResponse;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyResponse;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import com.danielamarjina.carinsurance.service.InsurancePolicyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("")
@RequiredArgsConstructor
public class InsurancePolicyController {
    private final InsurancePolicyService service;

    @GetMapping("/policies")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public Page<InsurancePolicyResponse> getAllPolicies(
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) InsurancePolicyStatus status,
            @ParameterObject @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "status",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
            ){
        return service.getAllPolicies(provider,status,pageable);
    }

    @PostMapping("/cars/{carId}/policies")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<InsurancePolicyResponse> createPolicy(@PathVariable UUID carId,
                                                               @Valid @RequestBody InsurancePolicyRequest request){
        InsurancePolicyResponse response=service.createPolicy(carId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public InsurancePolicyIsValidResponse getValidityPolicy(@PathVariable UUID carId,
                                                            @RequestParam LocalDate date){
        return service.getValidityPolicy(carId,date);
    }

    @GetMapping("/policies/active-policy")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public InsurancePolicyResponse getActivePolicy(@RequestParam UUID carId){
        return service.getActivePolicy(carId);
    }
}
