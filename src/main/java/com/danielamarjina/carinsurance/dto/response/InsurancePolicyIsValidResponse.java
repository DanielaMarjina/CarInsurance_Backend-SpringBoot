package com.danielamarjina.carinsurance.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyIsValidResponse {

    private UUID carId;

    private LocalDate date;

    private Boolean valid;
}
