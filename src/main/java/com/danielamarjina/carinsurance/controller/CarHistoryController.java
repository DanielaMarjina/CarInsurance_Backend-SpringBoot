package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.response.CarHistoryResponse;
import com.danielamarjina.carinsurance.enums.CarHistoryType;
import com.danielamarjina.carinsurance.service.CarHistoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("")
@RequiredArgsConstructor
public class CarHistoryController {
    private final CarHistoryService service;

    @GetMapping("/cars/{carId}/history")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public Page<CarHistoryResponse> getCarHistory(@PathVariable UUID carId,
                                                  @RequestParam(required = false) CarHistoryType type,
                                                  @ParameterObject @PageableDefault(
                                                          page = 0,
                                                          size = 10
                                                  ) Pageable pageable) {
        return service.getCarHistory(carId, type,pageable);
    }
}
