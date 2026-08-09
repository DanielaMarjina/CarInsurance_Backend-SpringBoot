package com.danielamarjina.carinsurance.dto.request;

import java.time.LocalDate;

public interface OwnerValidationData {
    LocalDate getBirthdate();
    Integer getYearOfDriverLicense();
}
