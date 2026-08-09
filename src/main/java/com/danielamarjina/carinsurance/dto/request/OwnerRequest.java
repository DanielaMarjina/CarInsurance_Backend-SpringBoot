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

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Pattern(
            regexp = "^[\\p{L}]+([ '-][\\p{L}]+)*$",
            message = "Name can contain only letters, spaces, hyphens and apostrophes"
    )
    private String name;

    @NotNull(message = "Birthdate is required")
    @Past
    private LocalDate birthdate;

    @NotNull(message = "Year of driver license is required")
    @Min(value = 1900, message = "Year of driver license must be after 1900")
    private Integer yearOfDriverLicense;

    private DriverLicenseCategory driverLicenseCategory;

    @Email(message = "Please enter a valid email")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

}
