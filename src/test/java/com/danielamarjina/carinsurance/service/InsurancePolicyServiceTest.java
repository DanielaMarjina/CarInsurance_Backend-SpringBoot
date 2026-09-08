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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyServiceTest {
    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @Mock
    private InsurancePolicyMapper insurancePolicyMapper;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private InsurancePolicyService insurancePolicyService;

    @Test
    void createPolicy_shouldCreatePolicy_whenCarExistsAndEndDateIsInTheFuture(){
        UUID carId= UUID.randomUUID();
        InsurancePolicyRequest insurancePolicyRequest=new InsurancePolicyRequest();
        insurancePolicyRequest.setEndDate(LocalDate.now().plusDays(30));
        Car car=new Car();
        InsurancePolicy insurancePolicy=new InsurancePolicy();
        InsurancePolicyResponse insurancePolicyResponse=new InsurancePolicyResponse();
        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));
        when(insurancePolicyMapper.toEntity(insurancePolicyRequest))
                .thenReturn(insurancePolicy);
        insurancePolicy.setCar(car);
        when(insurancePolicyRepository.save(insurancePolicy))
                .thenReturn(insurancePolicy);
        when(insurancePolicyMapper.toResponse(insurancePolicy))
                .thenReturn(insurancePolicyResponse);

        InsurancePolicyResponse result=insurancePolicyService.createPolicy(carId,insurancePolicyRequest);

        assertEquals(insurancePolicyResponse,result);
        assertEquals(InsurancePolicyStatus.ACTIVE,insurancePolicy.getStatus());

        verify(carRepository).findById(carId);
        verify(insurancePolicyMapper).toEntity(insurancePolicyRequest);
        verify(insurancePolicyRepository).save(insurancePolicy);
        verify(insurancePolicyMapper).toResponse(insurancePolicy);
    }

    @Test
    void createPolicy_shouldCreatePolicy_whenCarExistsAndEndDateIsInThePast(){
        UUID carId= UUID.randomUUID();
        InsurancePolicyRequest insurancePolicyRequest=new InsurancePolicyRequest();
        insurancePolicyRequest.setEndDate(LocalDate.now().minusDays(1));
        Car car=new Car();
        InsurancePolicy insurancePolicy=new InsurancePolicy();
        InsurancePolicyResponse insurancePolicyResponse=new InsurancePolicyResponse();
        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));
        when(insurancePolicyMapper.toEntity(insurancePolicyRequest))
                .thenReturn(insurancePolicy);
        insurancePolicy.setCar(car);
        when(insurancePolicyRepository.save(insurancePolicy))
                .thenReturn(insurancePolicy);
        when(insurancePolicyMapper.toResponse(insurancePolicy))
                .thenReturn(insurancePolicyResponse);

        InsurancePolicyResponse result=insurancePolicyService.createPolicy(carId,insurancePolicyRequest);

        assertEquals(insurancePolicyResponse,result);
        assertEquals(InsurancePolicyStatus.EXPIRED,insurancePolicy.getStatus());

        verify(carRepository).findById(carId);
        verify(insurancePolicyMapper).toEntity(insurancePolicyRequest);
        verify(insurancePolicyRepository).save(insurancePolicy);
        verify(insurancePolicyMapper).toResponse(insurancePolicy);
    }

    @Test
    void createPolicy_shouldThrowException_whenCarDoesNotExist(){
        UUID carId= UUID.randomUUID();
        InsurancePolicyRequest insurancePolicyRequest=new InsurancePolicyRequest();
        when(carRepository.findById(carId))
                .thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class,
                ()->insurancePolicyService.createPolicy(carId,insurancePolicyRequest));
        verify(carRepository).findById(carId);
    }

    @Test
    void getAllPolicies_shouldReturnAllPolicies_whenNoFiltersAreProvided(){
        InsurancePolicy insurancePolicy1=new InsurancePolicy();
        InsurancePolicy insurancePolicy2=new InsurancePolicy();
        List<InsurancePolicy> policies= List.of(
                insurancePolicy1,
                insurancePolicy2
        );
        InsurancePolicyResponse insurancePolicyResponse1=new InsurancePolicyResponse();
        InsurancePolicyResponse insurancePolicyResponse2=new InsurancePolicyResponse();

        when(insurancePolicyRepository.findAll(any(Specification.class)))
                .thenReturn(policies);
        when(insurancePolicyMapper.toResponse(insurancePolicy1))
                .thenReturn(insurancePolicyResponse1);
        when(insurancePolicyMapper.toResponse(insurancePolicy2))
                .thenReturn(insurancePolicyResponse2);

        List<InsurancePolicyResponse> result=insurancePolicyService.getAllPolicies(null,null);
        assertEquals(List.of(insurancePolicyResponse1,insurancePolicyResponse2),result);
        verify(insurancePolicyRepository).findAll(any(Specification.class));
        verify(insurancePolicyMapper).toResponse(insurancePolicy1);
        verify(insurancePolicyMapper).toResponse(insurancePolicy2);
    }

    @Test
    void getAllPolicies_shouldReturnFilteredPolicies_whenProviderIsProvided(){
        String provider="Generali Group";
        InsurancePolicy insurancePolicy1=new InsurancePolicy();
        InsurancePolicy insurancePolicy2=new InsurancePolicy();
        List<InsurancePolicy> policies= List.of(
                insurancePolicy1,
                insurancePolicy2
        );
        InsurancePolicyResponse insurancePolicyResponse1=new InsurancePolicyResponse();
        InsurancePolicyResponse insurancePolicyResponse2=new InsurancePolicyResponse();

        when(insurancePolicyRepository.findAll(any(Specification.class)))
                .thenReturn(policies);
        when(insurancePolicyMapper.toResponse(insurancePolicy1))
                .thenReturn(insurancePolicyResponse1);
        when(insurancePolicyMapper.toResponse(insurancePolicy2))
                .thenReturn(insurancePolicyResponse2);

        List<InsurancePolicyResponse> result=insurancePolicyService.getAllPolicies(provider,null);
        assertEquals(List.of(insurancePolicyResponse1,insurancePolicyResponse2),result);
        verify(insurancePolicyRepository).findAll(any(Specification.class));
        verify(insurancePolicyMapper).toResponse(insurancePolicy1);
        verify(insurancePolicyMapper).toResponse(insurancePolicy2);
    }

    @Test
    void getAllPolicies_shouldReturnFilteredPolicies_whenStatusIsProvided(){
        InsurancePolicyStatus insurancePolicyStatus=InsurancePolicyStatus.ACTIVE;
        InsurancePolicy insurancePolicy1=new InsurancePolicy();
        InsurancePolicy insurancePolicy2=new InsurancePolicy();
        List<InsurancePolicy> policies= List.of(
                insurancePolicy1,
                insurancePolicy2
        );
        InsurancePolicyResponse insurancePolicyResponse1=new InsurancePolicyResponse();
        InsurancePolicyResponse insurancePolicyResponse2=new InsurancePolicyResponse();

        when(insurancePolicyRepository.findAll(any(Specification.class)))
                .thenReturn(policies);
        when(insurancePolicyMapper.toResponse(insurancePolicy1))
                .thenReturn(insurancePolicyResponse1);
        when(insurancePolicyMapper.toResponse(insurancePolicy2))
                .thenReturn(insurancePolicyResponse2);

        List<InsurancePolicyResponse> result=insurancePolicyService.getAllPolicies(null,insurancePolicyStatus);
        assertEquals(List.of(insurancePolicyResponse1,insurancePolicyResponse2),result);
        verify(insurancePolicyRepository).findAll(any(Specification.class));
        verify(insurancePolicyMapper).toResponse(insurancePolicy1);
        verify(insurancePolicyMapper).toResponse(insurancePolicy2);
    }

    @Test
    void getAllPolicies_shouldReturnFilteredPolicies_whenAllFiltersAreProvided(){
        String provider="Generali Group";
        InsurancePolicyStatus insurancePolicyStatus=InsurancePolicyStatus.ACTIVE;
        InsurancePolicy insurancePolicy1=new InsurancePolicy();
        InsurancePolicy insurancePolicy2=new InsurancePolicy();
        List<InsurancePolicy> policies= List.of(
                insurancePolicy1,
                insurancePolicy2
        );
        InsurancePolicyResponse insurancePolicyResponse1=new InsurancePolicyResponse();
        InsurancePolicyResponse insurancePolicyResponse2=new InsurancePolicyResponse();

        when(insurancePolicyRepository.findAll(any(Specification.class)))
                .thenReturn(policies);
        when(insurancePolicyMapper.toResponse(insurancePolicy1))
                .thenReturn(insurancePolicyResponse1);
        when(insurancePolicyMapper.toResponse(insurancePolicy2))
                .thenReturn(insurancePolicyResponse2);

        List<InsurancePolicyResponse> result=insurancePolicyService.getAllPolicies(provider,insurancePolicyStatus);
        assertEquals(List.of(insurancePolicyResponse1,insurancePolicyResponse2),result);
        verify(insurancePolicyRepository).findAll(any(Specification.class));
        verify(insurancePolicyMapper).toResponse(insurancePolicy1);
        verify(insurancePolicyMapper).toResponse(insurancePolicy2);
    }

    @Test
    void getValidityPolicy_shouldReturnValidTrue_whenDateIsWithinPolicyPeriodAndCarAndPolicyExist() {
        UUID carId = UUID.randomUUID();

        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);
        LocalDate date = LocalDate.of(2026, 6, 15);

        Car car = new Car();
        InsurancePolicy policy = new InsurancePolicy();

        policy.setStartDate(startDate);
        policy.setEndDate(endDate);

        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));

        when(insurancePolicyRepository.findByCar(car))
                .thenReturn(Optional.of(policy));

        InsurancePolicyIsValidResponse result =
                insurancePolicyService.getValidityPolicy(carId, date);

        assertEquals(carId, result.getCarId());
        assertEquals(date, result.getDate());
        assertTrue(result.getValid());

        verify(carRepository).findById(carId);
        verify(insurancePolicyRepository).findByCar(car);
    }

    @Test
    void getValidityPolicy_shouldReturnValidFalse_whenDateIsOutsidePolicyPeriodAndCarAndPolicyExist() {
        UUID carId = UUID.randomUUID();

        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);
        LocalDate date = LocalDate.of(2025, 12, 15);

        Car car = new Car();
        InsurancePolicy policy = new InsurancePolicy();

        policy.setStartDate(startDate);
        policy.setEndDate(endDate);

        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));

        when(insurancePolicyRepository.findByCar(car))
                .thenReturn(Optional.of(policy));

        InsurancePolicyIsValidResponse result =
                insurancePolicyService.getValidityPolicy(carId, date);

        assertEquals(carId, result.getCarId());
        assertEquals(date, result.getDate());
        assertFalse(result.getValid());

        verify(carRepository).findById(carId);
        verify(insurancePolicyRepository).findByCar(car);
    }

    @Test
    void getValidityPolicy_shouldThrowException_whenCarDoesNotExist() {
        UUID carId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 6, 15);

        when(carRepository.findById(carId))
                .thenReturn(Optional.empty());

        assertThrows(
                CarNotFoundException.class,
                () -> insurancePolicyService.getValidityPolicy(carId, date)
        );

        verify(carRepository).findById(carId);
    }

    @Test
    void getValidityPolicy_shouldThrowException_whenPolicyDoesNotExist() {
        UUID carId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 6, 15);

        Car car = new Car();

        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));

        when(insurancePolicyRepository.findByCar(car))
                .thenReturn(Optional.empty());

        assertThrows(
                InsurancePolicyNotFoundException.class,
                () -> insurancePolicyService.getValidityPolicy(carId, date)
        );

        verify(carRepository).findById(carId);
        verify(insurancePolicyRepository).findByCar(car);
    }

    @Test
    void getActivePolicy_shouldReturnActivePolicy_whenActivePolicyExists() {
        UUID carId = UUID.randomUUID();

        Car car = new Car();
        InsurancePolicy activePolicy = new InsurancePolicy();
        InsurancePolicyResponse policyResponse = new InsurancePolicyResponse();

        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));

        when(insurancePolicyRepository.findByCarAndStatus(
                car,
                InsurancePolicyStatus.ACTIVE))
                .thenReturn(Optional.of(activePolicy));

        when(insurancePolicyMapper.toResponse(activePolicy))
                .thenReturn(policyResponse);

        InsurancePolicyResponse result =
                insurancePolicyService.getActivePolicy(carId);

        assertEquals(policyResponse, result);

        verify(carRepository).findById(carId);
        verify(insurancePolicyRepository)
                .findByCarAndStatus(car, InsurancePolicyStatus.ACTIVE);
        verify(insurancePolicyMapper).toResponse(activePolicy);
    }

    @Test
    void getActivePolicy_shouldThrowException_whenCarDoesNotExist() {
        UUID carId = UUID.randomUUID();

        when(carRepository.findById(carId))
                .thenReturn(Optional.empty());

        assertThrows(
                CarNotFoundException.class,
                () -> insurancePolicyService.getActivePolicy(carId)
        );

        verify(carRepository).findById(carId);
    }

    @Test
    void getActivePolicy_shouldThrowException_whenActivePolicyDoesNotExist() {
        UUID carId = UUID.randomUUID();

        Car car = new Car();

        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));

        when(insurancePolicyRepository.findByCarAndStatus(
                car,
                InsurancePolicyStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(
                ActivePolicyNotFoundException.class,
                () -> insurancePolicyService.getActivePolicy(carId)
        );

        verify(carRepository).findById(carId);
        verify(insurancePolicyRepository)
                .findByCarAndStatus(car, InsurancePolicyStatus.ACTIVE);
    }
}