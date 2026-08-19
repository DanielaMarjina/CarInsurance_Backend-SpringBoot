package com.danielamarjina.carinsurance.dto.request;

import com.danielamarjina.carinsurance.validation.ValidInsurancePolicy;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ValidInsurancePolicy
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequest {

    @NotBlank(message = "Provider is required")
    @Size(max = 100, message = "Provider cannot exceed 100 characters")
    private String provider;

    @PastOrPresent(message = "Start date cannot be in the future")
    @NotNull(message = "Start Date is required")
    private LocalDate startDate;

    @NotNull(message = "End Date is required")
    private LocalDate endDate;

    @NotNull(message = "Paid Amount is required")
    @DecimalMin(value = "0.01", message = "Paid Amount cannot be 0 or lower")
    private BigDecimal paidAmount;


}
