package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.InsurancePolicyRequest;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyIsValidResponse;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyResponse;
import com.danielamarjina.carinsurance.service.InsurancePolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class InsurancePolicyController {
    private final InsurancePolicyService service;

    @GetMapping("/policies")
    public List<InsurancePolicyResponse> getAllPolicies(){
        return service.getAllPolicies();
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
