package com.danielamarjina.carinsurance.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimRequest {
    @NotNull(message = "Claim Date is required")
    @PastOrPresent(message = "Claim Date cannot be in the future")
    private LocalDate claimDate;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01",message = "Amount cannot be 0 or lower")
    private BigDecimal amount;
}
