package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.CarRequest;
import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.enums.CarCategory;
import com.danielamarjina.carinsurance.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    @GetMapping
    public List<CarResponse> getAllCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) CarCategory category,
            @RequestParam(required = false) UUID ownerId
    ){
        return carService.getAllCars(make, model, category, ownerId);
    }

    @PostMapping
    public CarResponse createCar(
            @Valid @RequestBody CarRequest request){
        return carService.createCar(request);
    }

    @GetMapping("/{id}")
    public CarResponse getCar(@PathVariable UUID id)
    {
        return carService.getCarById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id){
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }


}
