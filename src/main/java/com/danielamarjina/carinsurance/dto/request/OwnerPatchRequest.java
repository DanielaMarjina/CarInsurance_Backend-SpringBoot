package com.danielamarjina.carinsurance.dto.request;

import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerPatchRequest {

    @Size(max = 100)
    private String name;

    @Past
    private LocalDate birthdate;

    @Min(1900)
    private Integer yearOfDriverLicense;

    private DriverLicenseCategory driverLicenseCategory;

    @Email
    @Size(max = 150)
    private String email;

}
