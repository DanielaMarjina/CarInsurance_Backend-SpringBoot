package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.response.CarHistoryResponse;
import com.danielamarjina.carinsurance.enums.CarHistoryType;
import com.danielamarjina.carinsurance.service.CarHistoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("")
@RequiredArgsConstructor
public class CarHistoryController {
    private final CarHistoryService service;

    @GetMapping("/cars/{carId}/history")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public List<CarHistoryResponse> getCarHistory(@PathVariable UUID carId,
                                                  @RequestParam(required = false)CarHistoryType type){
        return service.getCarHistory(carId,type);
    }
}
