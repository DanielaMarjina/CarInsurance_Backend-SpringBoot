package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.ClaimRequest;
import com.danielamarjina.carinsurance.dto.response.ClaimResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.entity.Claim;
import com.danielamarjina.carinsurance.exception.CarNotFoundException;
import com.danielamarjina.carinsurance.mapper.ClaimMapper;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClaimService {
    private final ClaimMapper claimMapper;
    private final ClaimRepository claimRepository;
    private final CarRepository carRepository;

    public List<ClaimResponse> getAllClaims(){
        return claimRepository.findAll()
                .stream()
                .map(claimMapper::toResponse)
                .toList();
    }

    public ClaimResponse createClaim(UUID carId, ClaimRequest request){
        Car car=carRepository.findById(carId)
                .orElseThrow(()->new CarNotFoundException(carId));
        Claim claim=claimMapper.toEntity(request);
        claim.setCar(car);
        Claim savedClaim=claimRepository.save(claim);
        return claimMapper.toResponse(savedClaim);
    }
}
