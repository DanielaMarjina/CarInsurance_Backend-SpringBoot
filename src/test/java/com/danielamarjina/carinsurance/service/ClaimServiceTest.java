package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.ClaimRequest;
import com.danielamarjina.carinsurance.dto.response.ClaimResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.entity.Claim;
import com.danielamarjina.carinsurance.exception.CarNotFoundException;
import com.danielamarjina.carinsurance.mapper.ClaimMapper;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.repository.ClaimRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimMapper claimMapper;

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private ClaimService claimService;

    @Test
    void getAllClaims_shouldReturnAllClaims() {
        Claim claim1 = new Claim();
        Claim claim2 = new Claim();

        List<Claim> claims= List.of(
                claim1,
                claim2
        );

        ClaimResponse claimResponse1 = new ClaimResponse();
        ClaimResponse claimResponse2 = new ClaimResponse();

        when(claimRepository.findAll())
                .thenReturn(claims);

        when(claimMapper.toResponse(claim1))
                .thenReturn(claimResponse1);

        when(claimMapper.toResponse(claim2))
                .thenReturn(claimResponse2);

        List<ClaimResponse> result = claimService.getAllClaims();

        assertEquals(
                List.of(claimResponse1, claimResponse2),
                result
        );

        verify(claimRepository).findAll();
        verify(claimMapper).toResponse(claim1);
        verify(claimMapper).toResponse(claim2);
    }


    @Test
    void createClaim_shouldCreateClaim_whenCarExists() {
        UUID carId = UUID.randomUUID();

        ClaimRequest request = new ClaimRequest();
        Car car = new Car();
        Claim claim = new Claim();
        ClaimResponse claimResponse = new ClaimResponse();

        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));

        when(claimMapper.toEntity(request))
                .thenReturn(claim);
        claim.setCar(car);
        when(claimRepository.save(claim))
                .thenReturn(claim);

        when(claimMapper.toResponse(claim))
                .thenReturn(claimResponse);

        ClaimResponse result =
                claimService.createClaim(carId, request);

        assertEquals(claimResponse, result);
        assertEquals(car, claim.getCar());

        verify(carRepository).findById(carId);
        verify(claimMapper).toEntity(request);
        verify(claimRepository).save(claim);
        verify(claimMapper).toResponse(claim);
    }

    @Test
    void createClaim_shouldThrowException_whenCarDoesNotExist() {
        UUID carId = UUID.randomUUID();

        ClaimRequest request = new ClaimRequest();

        when(carRepository.findById(carId))
                .thenReturn(Optional.empty());

        assertThrows(
                CarNotFoundException.class,
                () -> claimService.createClaim(carId, request)
        );

        verify(carRepository).findById(carId);
    }
}