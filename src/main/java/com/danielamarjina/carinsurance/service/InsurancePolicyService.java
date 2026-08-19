package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.response.InsurancePolicyResponse;
import com.danielamarjina.carinsurance.mapper.InsurancePolicyMapper;
import com.danielamarjina.carinsurance.repository.InsurancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsurancePolicyService {
    private final InsurancePolicyRepository repository;
    private final InsurancePolicyMapper mapper;

    public List<InsurancePolicyResponse> getAllPolicies(){
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
