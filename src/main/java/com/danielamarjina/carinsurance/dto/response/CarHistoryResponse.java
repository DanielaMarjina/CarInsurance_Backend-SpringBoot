package com.danielamarjina.carinsurance.dto.response;

import com.danielamarjina.carinsurance.enums.CarHistoryType;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CarHistoryResponse {

    private CarHistoryType type;

    private UUID policyId;

    private LocalDate startDate;
    private LocalDate endDate;

    private String provider;

    private BigDecimal paidAmount;

    private InsurancePolicyStatus status;

    private UUID claimId;

    private LocalDate claimDate;

    private BigDecimal amount;

    private String description;

    public LocalDate getDate() {
        return type == CarHistoryType.POLICY ? startDate : claimDate;
    }
}
