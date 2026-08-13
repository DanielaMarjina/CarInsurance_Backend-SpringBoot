package com.danielamarjina.carinsurance.dto.response;

import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerSummaryResponse {

    private UUID id;

    private String name;

    private String email;

}

