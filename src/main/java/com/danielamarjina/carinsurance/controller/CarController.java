package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.CarRequest;
import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.enums.CarCategory;
import com.danielamarjina.carinsurance.service.CarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public Page<CarResponse> getAllCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) CarCategory category,
            @RequestParam(required = false) UUID ownerId,
            @ParameterObject @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "make",
                    direction = Sort.Direction.ASC
            ) Pageable pageable  /// implicit page=0, size=20
    ){
        return carService.getAllCars(make, model, category, ownerId, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public CarResponse createCar(
            @Valid @RequestBody CarRequest request){
        return carService.createCar(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public CarResponse getCar(@PathVariable UUID id)
    {
        return carService.getCarById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id){
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }


}
