package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.ClaimRequest;
import com.danielamarjina.carinsurance.dto.response.ClaimResponse;
import com.danielamarjina.carinsurance.service.ClaimService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("")
public class ClaimController {
    private final ClaimService claimService;

    @GetMapping("/claims")
    public List<ClaimResponse> getAllClaims(){
        return claimService.getAllClaims();
    }

    @PostMapping("/cars/{carId}/claims")
    public ClaimResponse createClaim(@PathVariable UUID carId,
                                     @Valid @RequestBody ClaimRequest claimRequest){
        return claimService.createClaim(carId,claimRequest);
    }
}
