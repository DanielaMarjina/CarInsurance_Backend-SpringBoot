package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.InsurancePolicyRequest;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyIsValidResponse;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.entity.InsurancePolicy;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import com.danielamarjina.carinsurance.exception.ActivePolicyNotFoundException;
import com.danielamarjina.carinsurance.exception.CarNotFoundException;
import com.danielamarjina.carinsurance.exception.InsurancePolicyNotFoundException;
import com.danielamarjina.carinsurance.exception.OverlappingInsurancePolicyException;
import com.danielamarjina.carinsurance.mapper.InsurancePolicyMapper;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.repository.InsurancePolicyRepository;
import com.danielamarjina.carinsurance.specification.InsurancePolicySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsurancePolicyService {
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final InsurancePolicyMapper insurancePolicyMapper;
    private final CarRepository carRepository;

    public List<InsurancePolicyResponse> getAllPolicies(String provider, InsurancePolicyStatus status) {
        Specification<InsurancePolicy> specification = (root, query, criteriaBuilder) -> null;

        if (provider != null)
            specification = specification.and(InsurancePolicySpecification.hasProvider(provider));

        if (status != null)
            specification = specification.and(InsurancePolicySpecification.hasStatus(status));

        return insurancePolicyRepository.findAll(specification)
                .stream()
                .map(insurancePolicyMapper::toResponse)
                .toList();
    }

    public InsurancePolicyResponse createPolicy(UUID carId, InsurancePolicyRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
        if (insurancePolicyRepository
                .existsByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        car,
                        request.getEndDate(),
                        request.getStartDate())) {
            throw new OverlappingInsurancePolicyException(carId);
        }
        InsurancePolicy insurancePolicy = insurancePolicyMapper.toEntity(request);
        insurancePolicy.setCar(car);

        LocalDate today = LocalDate.now();
        if (request.getEndDate().isBefore(today)) {
            insurancePolicy.setStatus(InsurancePolicyStatus.EXPIRED);
        } else if (request.getStartDate().isAfter(today)) {
            insurancePolicy.setStatus(InsurancePolicyStatus.UPCOMING);
        } else {
            insurancePolicy.setStatus(InsurancePolicyStatus.ACTIVE);
        }
        InsurancePolicy savedPolicy = insurancePolicyRepository.save(insurancePolicy);
        return insurancePolicyMapper.toResponse(savedPolicy);
    }

    public InsurancePolicyIsValidResponse getValidityPolicy(UUID carId, LocalDate date) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
        InsurancePolicy insurancePolicy = insurancePolicyRepository
                .findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(car,date,date)
                .orElseThrow(() -> new InsurancePolicyNotFoundException(carId));
        boolean valid =
                !date.isBefore(insurancePolicy.getStartDate())
                        && !date.isAfter(insurancePolicy.getEndDate());

        return InsurancePolicyIsValidResponse.builder()
                .carId(carId)
                .date(date)
                .valid(valid)
                .build();
    }

    public InsurancePolicyResponse getActivePolicy(UUID carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
        InsurancePolicy activePolicy = insurancePolicyRepository.findByCarAndStatus(car, InsurancePolicyStatus.ACTIVE)
                .orElseThrow(() -> new ActivePolicyNotFoundException(carId));
        return insurancePolicyMapper.toResponse(activePolicy);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updatePolicyStatus() {
        LocalDate today = LocalDate.now();
        List<InsurancePolicy> expiredPolicies = insurancePolicyRepository.
                findByStatusAndEndDateBefore(InsurancePolicyStatus.ACTIVE, today);
        System.out.println("Policies expired: "+ expiredPolicies.size());
        for(InsurancePolicy policy : expiredPolicies){
            policy.setStatus(InsurancePolicyStatus.EXPIRED);
        }
        List<InsurancePolicy> nowActivePolicies=insurancePolicyRepository.
                findByStatusAndStartDate(InsurancePolicyStatus.UPCOMING,today);
        System.out.println("Policies now active: "+ nowActivePolicies.size());
        for(InsurancePolicy policy : nowActivePolicies){
            policy.setStatus(InsurancePolicyStatus.ACTIVE);
        }
    }
}
