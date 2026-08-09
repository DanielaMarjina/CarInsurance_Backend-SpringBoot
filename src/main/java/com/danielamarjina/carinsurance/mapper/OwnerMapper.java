package com.danielamarjina.carinsurance.mapper;

import com.danielamarjina.carinsurance.dto.request.OwnerPatchRequest;
import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface OwnerMapper {
    Owner toEntity(OwnerRequest ownerRequest);
    OwnerResponse toResponse(Owner owner);
    void updateEntity(OwnerRequest ownerRequest, @MappingTarget Owner owner);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(OwnerPatchRequest ownerPatchRequest, @MappingTarget Owner owner);
}
