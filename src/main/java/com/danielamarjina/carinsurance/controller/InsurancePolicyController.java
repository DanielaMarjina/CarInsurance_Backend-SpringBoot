package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.InsurancePolicyRequest;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyIsValidResponse;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyResponse;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import com.danielamarjina.carinsurance.service.InsurancePolicyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<InsurancePolicyResponse> getAllPolicies(
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) InsurancePolicyStatus status
            ){
        return service.getAllPolicies(provider,status);
    }

    @PostMapping("/cars/{carId}/policies")
    public InsurancePolicyResponse createPolicy(@PathVariable UUID carId,
                                                @Valid @RequestBody InsurancePolicyRequest request){
        return service.createPolicy(carId,request);
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public InsurancePolicyIsValidResponse getValidityPolicy(@PathVariable UUID carId,
                                                            @Valid LocalDate date){
        return service.getValidityPolicy(carId,date);
    }

    @GetMapping("/policies/active-policy")
    public InsurancePolicyResponse getActivePolicy(@Valid UUID carId){
        return service.getActivePolicy(carId);
    }
}
