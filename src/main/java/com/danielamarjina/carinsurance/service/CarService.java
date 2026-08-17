package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.CarRequest;
import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.enums.CarCategory;
import com.danielamarjina.carinsurance.mapper.CarMapper;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.specification.CarSpecification;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarResponse createCar(CarRequest carRequest) {
        Car car = carMapper.toEntity(carRequest);
        Car savedCar = carRepository.save(car);
        return carMapper.toResponse(savedCar);
    }

    public List<CarResponse> getAllCars(String make, String model, CarCategory category, UUID ownerId) {
        Specification<Car> specification = (root, query, criteriaBuilder) -> null;

        if (make != null) {
            specification = specification.and(CarSpecification.hasMake(make));
        }
        if (model != null) {
            specification = specification.and(CarSpecification.hasModel(model));
        }
        if (category != null) {
            specification = specification.and(CarSpecification.hasCategory(category));
        }
        if (ownerId != null) {
            specification = specification.and(CarSpecification.hasOwnerId(ownerId));
        }

        specification = specification.and((root, query, criteriaBuilder) -> {
            root.fetch("owner", JoinType.INNER);
            return null;
        });

        return carRepository.findAll(specification)
                .stream()
                .map(carMapper::toResponse)
                .toList();

    }


}
