package com.danielamarjina.carinsurance.validation;

import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.request.OwnerValidationData;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class OwnerValidator implements ConstraintValidator<ValidOwner, OwnerValidationData> {

    @Override
    public boolean isValid(OwnerValidationData ownerValidationData, ConstraintValidatorContext constraintValidatorContext) {
        if (ownerValidationData==null)
            return true;
        if(!isBirthdateValid(ownerValidationData.getBirthdate()))
            return false;
        if(!isLicenseYearValid(ownerValidationData.getYearOfDriverLicense()))
            return false;
        if(!isLicenseAgeValid(ownerValidationData.getBirthdate(), ownerValidationData.getYearOfDriverLicense())){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Owner must be at least 18 years old when obtaining the driver's license.")
                    .addPropertyNode("yearOfDriverLicense")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    public boolean isBirthdateValid(LocalDate birthdate){
        if(birthdate==null)
            return true;

        LocalDate minDate=LocalDate.of(1900,1,1);
        LocalDate today=LocalDate.now();

        return !birthdate.isBefore(minDate) && !birthdate.isAfter(today);
    }

    public boolean isLicenseYearValid(Integer licenseYear){
        if(licenseYear==null)
            return true;
        int currentYear=LocalDate.now().getYear();
        return licenseYear>=1900 && licenseYear<currentYear;
    }

    public boolean isLicenseAgeValid(LocalDate birthdate, Integer licenseYear){
        if(birthdate==null || licenseYear==null)
            return true;
        int minLicenseYear=birthdate.getYear()+18;
        return licenseYear>=minLicenseYear;
    }
}
