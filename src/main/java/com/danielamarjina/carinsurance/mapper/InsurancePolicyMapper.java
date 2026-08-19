package com.danielamarjina.carinsurance.mapper;

import com.danielamarjina.carinsurance.dto.request.InsurancePolicyRequest;
import com.danielamarjina.carinsurance.dto.response.InsurancePolicyResponse;
import com.danielamarjina.carinsurance.entity.InsurancePolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = CarMapper.class)
public interface InsurancePolicyMapper {
    InsurancePolicy toEntity(InsurancePolicyRequest request);

    @Mapping(source = "car.id", target = "carId")
    InsurancePolicyResponse toResponse(InsurancePolicy insurancePolicy);
}
