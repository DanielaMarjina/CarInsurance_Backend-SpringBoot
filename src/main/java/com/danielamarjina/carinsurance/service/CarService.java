package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.CarRequest;
import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.mapper.CarMapper;
import com.danielamarjina.carinsurance.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarResponse createCar(CarRequest carRequest){
        Car car=carMapper.toEntity(carRequest);
        Car savedCar=carRepository.save(car);
        return carMapper.toResponse(savedCar);
    }

    public List<CarResponse> getAllCars(){
        return carRepository.findAll()
                .stream()
                .map(carMapper::toResponse)
                .toList();
    }


}
