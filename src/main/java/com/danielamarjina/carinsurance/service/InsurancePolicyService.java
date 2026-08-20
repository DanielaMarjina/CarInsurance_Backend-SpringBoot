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
import com.danielamarjina.carinsurance.mapper.InsurancePolicyMapper;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.repository.InsurancePolicyRepository;
import lombok.RequiredArgsConstructor;
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

    public List<InsurancePolicyResponse> getAllPolicies(){
        return insurancePolicyRepository.findAll()
                .stream()
                .map(insurancePolicyMapper::toResponse)
                .toList();
    }

    public InsurancePolicyResponse createPolicy(UUID carId, InsurancePolicyRequest request){
        Car car=carRepository.findById(carId)
                .orElseThrow(()->new CarNotFoundException(carId));
        InsurancePolicy insurancePolicy=insurancePolicyMapper.toEntity(request);
        insurancePolicy.setCar(car);
        if(request.getEndDate().isAfter(LocalDate.now())){
            insurancePolicy.setStatus(InsurancePolicyStatus.ACTIVE);
        }
        else{
            insurancePolicy.setStatus(InsurancePolicyStatus.EXPIRED);
        }
        InsurancePolicy savedPolicy=insurancePolicyRepository.save(insurancePolicy);
        return insurancePolicyMapper.toResponse(savedPolicy);
    }

    public InsurancePolicyIsValidResponse getValidityPolicy(UUID carId, LocalDate date){
        Car car=carRepository.findById(carId)
                .orElseThrow(()->new CarNotFoundException(carId));
        InsurancePolicy insurancePolicy=insurancePolicyRepository.findByCar(car)
                .orElseThrow(()->new InsurancePolicyNotFoundException(carId));
        boolean valid =
                !date.isBefore(insurancePolicy.getStartDate())
                        && !date.isAfter(insurancePolicy.getEndDate());

        return InsurancePolicyIsValidResponse.builder()
                .carId(carId)
                .date(date)
                .valid(valid)
                .build();
    }

    public InsurancePolicyResponse getActivePolicy(UUID carId){
        Car car=carRepository.findById(carId)
                .orElseThrow(()->new CarNotFoundException(carId));
        InsurancePolicy activePolicy=insurancePolicyRepository.findByCarAndStatus(car,InsurancePolicyStatus.ACTIVE)
                .orElseThrow(()->new ActivePolicyNotFoundException(carId));
        return insurancePolicyMapper.toResponse(activePolicy);
    }
}
