package com.danielamarjina.carinsurance.repository;

import com.danielamarjina.carinsurance.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, UUID> {
    @Override
    @EntityGraph(attributePaths = "car")
    List<InsurancePolicy> findAll();
}
