package com.danielamarjina.carinsurance.dto.response;

import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerResponse {

    private UUID id;

    private String name;

    private LocalDate birthdate;

    private Integer yearOfDriverLicense;

    private DriverLicenseCategory driverLicenseCategory;

    private String email;

}

