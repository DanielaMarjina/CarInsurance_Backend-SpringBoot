package com.danielamarjina.carinsurance.mapper;

import com.danielamarjina.carinsurance.dto.request.CarRequest;
import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.entity.Car;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = OwnerMapper.class)
public interface CarMapper {
    Car toEntity(CarRequest carRequest);
    CarResponse toResponse(Car car);
}
