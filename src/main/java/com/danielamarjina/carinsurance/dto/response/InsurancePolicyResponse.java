package com.danielamarjina.carinsurance.dto.response;

import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyResponse {
    private UUID id;

    private UUID carId;

    private String provider;

    private LocalDate startDate;

    private LocalDate endDate;

    private InsurancePolicyStatus status;

    private BigDecimal paidAmount;
}
