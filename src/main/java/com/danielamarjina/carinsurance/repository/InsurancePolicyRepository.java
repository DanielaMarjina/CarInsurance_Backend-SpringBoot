package com.danielamarjina.carinsurance.repository;

import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.entity.InsurancePolicy;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, UUID> {
    @Override
    @EntityGraph(attributePaths = "car")
    List<InsurancePolicy> findAll();

    Optional<InsurancePolicy> findByCar(Car car);

    Optional<InsurancePolicy> findByCarAndStatus(Car car,InsurancePolicyStatus status);
}
