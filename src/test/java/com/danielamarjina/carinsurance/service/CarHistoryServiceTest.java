package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.response.CarHistoryResponse;
import com.danielamarjina.carinsurance.entity.Claim;
import com.danielamarjina.carinsurance.entity.InsurancePolicy;
import com.danielamarjina.carinsurance.enums.CarHistoryType;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import com.danielamarjina.carinsurance.exception.CarNotFoundException;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.repository.ClaimRepository;
import com.danielamarjina.carinsurance.repository.InsurancePolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarHistoryServiceTest {

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarHistoryService carHistoryService;

    @Test
    void getCarHistory_shouldThrowException_whenCarDoesNotExist() {
        UUID carId = UUID.randomUUID();
        Pageable pageable= PageRequest.of(0,10);

        when(carRepository.existsById(carId))
                .thenReturn(false);

        assertThrows(
                CarNotFoundException.class,
                () -> carHistoryService.getCarHistory(carId, null,pageable)
        );

        verify(carRepository).existsById(carId);
    }

    @Test
    void getCarHistory_shouldReturnOnlyPolicies_whenTypeIsPolicy() {
        UUID carId = UUID.randomUUID();
        Pageable pageable= PageRequest.of(0,10);
        InsurancePolicy policy1 = createPolicy();
        InsurancePolicy policy2 = createPolicy();
        when(carRepository.existsById(carId))
                .thenReturn(true);

        when(insurancePolicyRepository.findByCarId(carId))
                .thenReturn(List.of(policy1, policy2));

        Page<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, CarHistoryType.POLICY,pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals(CarHistoryType.POLICY, result.getContent().getFirst().getType());
        assertEquals(policy1.getId(), result.getContent().getFirst().getPolicyId());

        assertEquals(CarHistoryType.POLICY, result.getContent().get(1).getType());
        assertEquals(policy2.getId(), result.getContent().get(1).getPolicyId());

        verify(carRepository).existsById(carId);
        verify(insurancePolicyRepository).findByCarId(carId);
    }

    @Test
    void getCarHistory_shouldReturnOnlyClaims_whenTypeIsClaim() {
        UUID carId = UUID.randomUUID();
        Pageable pageable= PageRequest.of(0,10);
        Claim claim1 = createClaim();
        Claim claim2 = createClaim();

        when(carRepository.existsById(carId))
                .thenReturn(true);

        when(claimRepository.findByCarId(carId))
                .thenReturn(List.of(claim1, claim2));

        Page<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, CarHistoryType.CLAIM,pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals(CarHistoryType.CLAIM, result.getContent().getFirst().getType());
        assertEquals(claim1.getId(), result.getContent().getFirst().getClaimId());

        assertEquals(CarHistoryType.CLAIM, result.getContent().get(1).getType());
        assertEquals(claim2.getId(), result.getContent().get(1).getClaimId());

        verify(carRepository).existsById(carId);
        verify(claimRepository).findByCarId(carId);
    }

    @Test
    void getCarHistory_shouldReturnPoliciesAndClaims_whenTypeIsNull() {
        UUID carId = UUID.randomUUID();
        Pageable pageable= PageRequest.of(0,10);
        InsurancePolicy policy = createPolicy();
        Claim claim = createClaim();

        when(carRepository.existsById(carId))
                .thenReturn(true);

        when(insurancePolicyRepository.findByCarId(carId))
                .thenReturn(List.of(policy));

        when(claimRepository.findByCarId(carId))
                .thenReturn(List.of(claim));

        Page<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, null,pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals(CarHistoryType.CLAIM, result.getContent().getFirst().getType());
        assertEquals(claim.getId(), result.getContent().getFirst().getClaimId());

        assertEquals(CarHistoryType.POLICY, result.getContent().get(1).getType());
        assertEquals(policy.getId(), result.getContent().get(1).getPolicyId());

        verify(carRepository).existsById(carId);
        verify(insurancePolicyRepository).findByCarId(carId);
        verify(claimRepository).findByCarId(carId);
    }

    @Test
    void getCarHistory_shouldSortHistoryByDateDescending() {
        UUID carId = UUID.randomUUID();
        Pageable pageable= PageRequest.of(0,10);
        InsurancePolicy oldPolicy = new InsurancePolicy();
        oldPolicy.setId(UUID.randomUUID());
        oldPolicy.setStartDate(LocalDate.of(2025, 1, 1));
        oldPolicy.setEndDate(LocalDate.of(2025, 12, 31));
        oldPolicy.setProvider("Allianz");
        oldPolicy.setPaidAmount(BigDecimal.valueOf(400));
        oldPolicy.setStatus(InsurancePolicyStatus.EXPIRED);

        Claim recentClaim = new Claim();
        recentClaim.setId(UUID.randomUUID());
        recentClaim.setClaimDate(LocalDate.of(2026, 8, 1));
        recentClaim.setAmount(BigDecimal.valueOf(1000));
        recentClaim.setDescription("Accident");

        InsurancePolicy recentPolicy = new InsurancePolicy();
        recentPolicy.setId(UUID.randomUUID());
        recentPolicy.setStartDate(LocalDate.of(2026, 7, 1));
        recentPolicy.setEndDate(LocalDate.of(2027, 6, 30));
        recentPolicy.setProvider("Generali Group");
        recentPolicy.setPaidAmount(BigDecimal.valueOf(500));
        recentPolicy.setStatus(InsurancePolicyStatus.ACTIVE);

        when(carRepository.existsById(carId))
                .thenReturn(true);

        when(insurancePolicyRepository.findByCarId(carId))
                .thenReturn(List.of(oldPolicy, recentPolicy));

        when(claimRepository.findByCarId(carId))
                .thenReturn(List.of(recentClaim));

        Page<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, null,pageable);

        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());

        assertEquals(
                recentClaim.getClaimDate(),
                result.getContent().getFirst().getDate()
        );

        assertEquals(
                recentPolicy.getStartDate(),
                result.getContent().get(1).getDate()
        );

        assertEquals(
                oldPolicy.getStartDate(),
                result.getContent().get(2).getDate()
        );
    }

    @Test
    void getCarHistory_shouldReturnSecondPage() {
        UUID carId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(1, 2);
        InsurancePolicy policy1 = createPolicy(LocalDate.of(2026, 3, 1));
        InsurancePolicy policy2 = createPolicy(LocalDate.of(2026, 2, 1));
        InsurancePolicy policy3 = createPolicy(LocalDate.of(2026, 1, 1));

        when(carRepository.existsById(carId)).thenReturn(true);

        when(insurancePolicyRepository.findByCarId(carId))
                .thenReturn(List.of(policy1, policy2, policy3));

        Page<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, CarHistoryType.POLICY, pageable);

        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getNumber());
        assertEquals(2, result.getTotalPages());
        assertEquals(1, result.getNumberOfElements());
        assertEquals(policy3.getId(), result.getContent().getFirst().getPolicyId());
    }

    private InsurancePolicy createPolicy() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setId(UUID.randomUUID());
        policy.setStartDate(LocalDate.of(2026, 1, 1));
        policy.setEndDate(LocalDate.of(2026, 12, 31));
        policy.setProvider("Test Provider");
        policy.setPaidAmount(BigDecimal.valueOf(500));
        policy.setStatus(InsurancePolicyStatus.ACTIVE);
        return policy;
    }

    private Claim createClaim() {
        Claim claim = new Claim();
        claim.setId(UUID.randomUUID());
        claim.setClaimDate(LocalDate.of(2026, 6, 1));
        claim.setAmount(BigDecimal.valueOf(1000));
        claim.setDescription("Test claim");
        return claim;
    }

    private InsurancePolicy createPolicy(LocalDate startDate) {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setId(UUID.randomUUID());
        policy.setStartDate(startDate);
        policy.setEndDate(startDate.plusYears(1));
        policy.setProvider("Test Provider");
        policy.setPaidAmount(BigDecimal.valueOf(500));
        policy.setStatus(InsurancePolicyStatus.ACTIVE);
        return policy;
    }


}