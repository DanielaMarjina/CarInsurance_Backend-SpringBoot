package com.danielamarjina.carinsurance.dto.request;

import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import com.danielamarjina.carinsurance.validation.ValidOwner;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@ValidOwner
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerRequest implements OwnerValidationData{

    @NotBlank
    @Size(max = 100)
    @Pattern(
            regexp = "^[\\p{L}]+([ '-][\\p{L}]+)*$",
            message = "Name can contain only letters, spaces, hyphens and apostrophes."
    )
    private String name;

    @NotNull
    @Past
    private LocalDate birthdate;

    @NotNull
    @Min(1900)
    private Integer yearOfDriverLicense;

    private DriverLicenseCategory driverLicenseCategory;

    @Email
    @Size(max = 150)
    private String email;

}
