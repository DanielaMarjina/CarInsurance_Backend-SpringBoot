package com.danielamarjina.carinsurance.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimResponse {
    private UUID id;

    private UUID carId;

    private LocalDate claimDate;

    private String description;

    private BigDecimal amount;

}
