package com.danielamarjina.carinsurance.dto.response;

import com.danielamarjina.carinsurance.enums.CarCategory;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponse {

    private UUID id;

    private String vin;

    private String make;

    private String model;

    private Integer yearOfManufacture;

    private CarCategory category;

    private Integer cc;

    private Integer power;

    private OwnerSummaryResponse owner;

}


