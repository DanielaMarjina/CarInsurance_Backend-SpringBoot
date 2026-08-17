package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.enums.CarCategory;
import com.danielamarjina.carinsurance.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
