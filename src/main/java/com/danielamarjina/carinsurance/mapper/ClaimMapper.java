package com.danielamarjina.carinsurance.mapper;

import com.danielamarjina.carinsurance.dto.request.ClaimRequest;
import com.danielamarjina.carinsurance.dto.response.ClaimResponse;
import com.danielamarjina.carinsurance.entity.Claim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = CarMapper.class)

public interface ClaimMapper {
    Claim toEntity(ClaimRequest request);

    @Mapping(source = "car.id",target = "carId")
    ClaimResponse toResponse(Claim claim);
}
