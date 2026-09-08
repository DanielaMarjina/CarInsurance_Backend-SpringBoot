package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.response.CarHistoryResponse;
import com.danielamarjina.carinsurance.entity.Car;
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

        when(carRepository.existsById(carId))
                .thenReturn(false);

        assertThrows(
                CarNotFoundException.class,
                () -> carHistoryService.getCarHistory(carId, null)
        );

        verify(carRepository).existsById(carId);
    }

    @Test
    void getCarHistory_shouldReturnOnlyPolicies_whenTypeIsPolicy() {
        UUID carId = UUID.randomUUID();

        InsurancePolicy policy1 = new InsurancePolicy();
        policy1.setId(UUID.randomUUID());
        policy1.setStartDate(LocalDate.of(2026, 1, 1));
        policy1.setEndDate(LocalDate.of(2026, 12, 31));
        policy1.setProvider("Generali Group");
        policy1.setPaidAmount(BigDecimal.valueOf(500));
        policy1.setStatus(InsurancePolicyStatus.ACTIVE);

        InsurancePolicy policy2 = new InsurancePolicy();
        policy2.setId(UUID.randomUUID());
        policy2.setStartDate(LocalDate.of(2025, 1, 1));
        policy2.setEndDate(LocalDate.of(2025, 12, 31));
        policy2.setProvider("Allianz");
        policy2.setPaidAmount(BigDecimal.valueOf(400));
        policy2.setStatus(InsurancePolicyStatus.EXPIRED);

        when(carRepository.existsById(carId))
                .thenReturn(true);

        when(insurancePolicyRepository.findByCarId(carId))
                .thenReturn(List.of(policy1, policy2));

        List<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, CarHistoryType.POLICY);

        assertEquals(2, result.size());

        assertEquals(CarHistoryType.POLICY, result.get(0).getType());
        assertEquals(policy1.getId(), result.get(0).getPolicyId());

        assertEquals(CarHistoryType.POLICY, result.get(1).getType());
        assertEquals(policy2.getId(), result.get(1).getPolicyId());

        verify(carRepository).existsById(carId);
        verify(insurancePolicyRepository).findByCarId(carId);
    }

    @Test
    void getCarHistory_shouldReturnOnlyClaims_whenTypeIsClaim() {
        UUID carId = UUID.randomUUID();

        Claim claim1 = new Claim();
        claim1.setId(UUID.randomUUID());
        claim1.setClaimDate(LocalDate.of(2026, 6, 15));
        claim1.setAmount(BigDecimal.valueOf(1000));
        claim1.setDescription("Accident");

        Claim claim2 = new Claim();
        claim2.setId(UUID.randomUUID());
        claim2.setClaimDate(LocalDate.of(2025, 5, 10));
        claim2.setAmount(BigDecimal.valueOf(700));
        claim2.setDescription("Broken windshield");

        when(carRepository.existsById(carId))
                .thenReturn(true);

        when(claimRepository.findByCarId(carId))
                .thenReturn(List.of(claim1, claim2));

        List<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, CarHistoryType.CLAIM);

        assertEquals(2, result.size());

        assertEquals(CarHistoryType.CLAIM, result.get(0).getType());
        assertEquals(claim1.getId(), result.get(0).getClaimId());

        assertEquals(CarHistoryType.CLAIM, result.get(1).getType());
        assertEquals(claim2.getId(), result.get(1).getClaimId());

        verify(carRepository).existsById(carId);
        verify(claimRepository).findByCarId(carId);
    }

    @Test
    void getCarHistory_shouldReturnPoliciesAndClaims_whenTypeIsNull() {
        UUID carId = UUID.randomUUID();

        InsurancePolicy policy = new InsurancePolicy();
        policy.setId(UUID.randomUUID());
        policy.setStartDate(LocalDate.of(2026, 1, 1));
        policy.setEndDate(LocalDate.of(2026, 12, 31));
        policy.setProvider("Generali Group");
        policy.setPaidAmount(BigDecimal.valueOf(500));
        policy.setStatus(InsurancePolicyStatus.ACTIVE);

        Claim claim = new Claim();
        claim.setId(UUID.randomUUID());
        claim.setClaimDate(LocalDate.of(2026, 6, 15));
        claim.setAmount(BigDecimal.valueOf(1000));
        claim.setDescription("Accident");

        when(carRepository.existsById(carId))
                .thenReturn(true);

        when(insurancePolicyRepository.findByCarId(carId))
                .thenReturn(List.of(policy));

        when(claimRepository.findByCarId(carId))
                .thenReturn(List.of(claim));

        List<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, null);

        assertEquals(2, result.size());

        assertEquals(CarHistoryType.CLAIM, result.get(0).getType());
        assertEquals(claim.getId(), result.get(0).getClaimId());

        assertEquals(CarHistoryType.POLICY, result.get(1).getType());
        assertEquals(policy.getId(), result.get(1).getPolicyId());

        verify(carRepository).existsById(carId);
        verify(insurancePolicyRepository).findByCarId(carId);
        verify(claimRepository).findByCarId(carId);
    }

    @Test
    void getCarHistory_shouldSortHistoryByDateDescending() {
        UUID carId = UUID.randomUUID();

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

        List<CarHistoryResponse> result =
                carHistoryService.getCarHistory(carId, null);

        assertEquals(3, result.size());

        assertEquals(
                recentClaim.getClaimDate(),
                result.get(0).getDate()
        );

        assertEquals(
                recentPolicy.getStartDate(),
                result.get(1).getDate()
        );

        assertEquals(
                oldPolicy.getStartDate(),
                result.get(2).getDate()
        );
    }


}