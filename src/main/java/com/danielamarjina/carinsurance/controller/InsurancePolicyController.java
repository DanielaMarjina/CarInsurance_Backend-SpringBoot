package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.response.InsurancePolicyResponse;
import com.danielamarjina.carinsurance.service.InsurancePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class InsurancePolicyController {
    private final InsurancePolicyService service;

    @GetMapping("/policies")
    public List<InsurancePolicyResponse> getAllPolicies(){
        return service.getAllPolicies();
    }
}
