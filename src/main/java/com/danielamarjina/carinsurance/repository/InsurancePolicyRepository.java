package com.danielamarjina.carinsurance.repository;

import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.entity.InsurancePolicy;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, UUID>, JpaSpecificationExecutor<InsurancePolicy> {
    @Override
    @EntityGraph(attributePaths = "car")
    List<InsurancePolicy> findAll();

    Optional<InsurancePolicy> findByCar(Car car);

    List<InsurancePolicy> findByCarId(UUID carId);

    Optional<InsurancePolicy> findByCarAndStatus(Car car,InsurancePolicyStatus status);
}
