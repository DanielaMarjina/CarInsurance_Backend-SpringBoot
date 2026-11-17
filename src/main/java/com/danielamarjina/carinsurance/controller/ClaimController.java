package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.ClaimRequest;
import com.danielamarjina.carinsurance.dto.response.ClaimResponse;
import com.danielamarjina.carinsurance.service.ClaimService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public Page<ClaimResponse> getAllClaims(@ParameterObject @PageableDefault(
            page = 0,
            size = 10,
            sort = "claimDate",
            direction = Sort.Direction.ASC
    ) Pageable pageable ){
        return claimService.getAllClaims(pageable);
    }

    @PostMapping("/cars/{carId}/claims")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<ClaimResponse> createClaim(@PathVariable UUID carId,
                                                    @Valid @RequestBody ClaimRequest claimRequest){
        ClaimResponse response=claimService.createClaim(carId,claimRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
