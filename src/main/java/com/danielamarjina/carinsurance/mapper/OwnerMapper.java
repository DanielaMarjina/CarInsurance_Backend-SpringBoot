package com.danielamarjina.carinsurance.mapper;

import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    public Owner toEntity(OwnerRequest ownerRequest){
        return Owner.builder()
                .name(ownerRequest.getName())
                .birthdate(ownerRequest.getBirthdate())
                .yearOfDriverLicense(ownerRequest.getYearOfDriverLicense())
                .driverLicenseCategory(ownerRequest.getDriverLicenseCategory())
                .email(ownerRequest.getEmail())
                .build();

    }


    public OwnerResponse toResponse(Owner owner){
        return OwnerResponse.builder()
                .id(owner.getId())
                .name(owner.getName())
                .birthdate(owner.getBirthdate())
                .yearOfDriverLicense(owner.getYearOfDriverLicense())
                .driverLicenseCategory(owner.getDriverLicenseCategory())
                .email(owner.getEmail())
                .build();
    }
}
