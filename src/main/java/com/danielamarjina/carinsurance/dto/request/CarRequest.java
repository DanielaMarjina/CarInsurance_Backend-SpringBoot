package com.danielamarjina.carinsurance.dto.request;

import com.danielamarjina.carinsurance.enums.CarCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequest {

    @NotBlank(message = "VIN is required")
    @Size(min = 17, max = 17, message = "VIN must contain exactly 17 characters")
    @Pattern(
            regexp = "^[A-HJ-NPR-Z0-9]{17}$",
            message = "VIN must contain only uppercase letters and digits, excluding I, O and Q"
    )
    private String vin;

    @NotBlank(message = "Make is required")
    @Size(max = 100, message = "Make cannot exceed 100 characters")
    private String make;

    @NotBlank(message = "Model is required")
    @Size(max = 150, message = "Model cannot exceed 150 characters")
    private String model;

    @NotNull(message = "Year of manufacture is required")
    @Min(value = 1900, message = "Year of manufacture must be after 1900")
    private Integer yearOfManufacture;

    private CarCategory category;

    @Min(value = 1)
    private Integer cc;

    @Min(value = 1)
    private Integer power;

    @NotNull(message = "Owner ID is required")
    private UUID ownerId;

}
