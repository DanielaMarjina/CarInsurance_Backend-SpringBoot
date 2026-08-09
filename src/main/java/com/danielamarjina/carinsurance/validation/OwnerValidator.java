package com.danielamarjina.carinsurance.validation;

import com.danielamarjina.carinsurance.dto.request.OwnerValidationData;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class OwnerValidator implements ConstraintValidator<ValidOwner, OwnerValidationData> {

    @Override
    public boolean isValid(OwnerValidationData ownerValidationData, ConstraintValidatorContext constraintValidatorContext) {
        if (ownerValidationData==null)
            return true;
        if(!isBirthdateValid(ownerValidationData.getBirthdate(),constraintValidatorContext))
            return false;
        if(!isLicenseYearValid(ownerValidationData.getYearOfDriverLicense(),constraintValidatorContext))
            return false;
        if(!isLicenseAgeValid(ownerValidationData.getBirthdate(), ownerValidationData.getYearOfDriverLicense(),constraintValidatorContext)){
            return false;
        }
        return true;
    }

    public boolean isBirthdateValid(LocalDate birthdate, ConstraintValidatorContext context){
        if(birthdate==null)
            return true;

        LocalDate minDate=LocalDate.of(1900,1,1);
        LocalDate today=LocalDate.now();

        if (birthdate.isBefore(minDate)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Birthdate cannot be before January 1, 1900.")
                    .addPropertyNode("birthdate")
                    .addConstraintViolation();
            return false;
        }

        if (birthdate.isAfter(today)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Birthdate cannot be after today.")
                    .addPropertyNode("birthdate")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    public boolean isLicenseYearValid(Integer licenseYear, ConstraintValidatorContext context){
        if(licenseYear==null)
            return true;
        int currentYear=LocalDate.now().getYear();
        if (licenseYear < 1900) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Driver's license year cannot be before 1900."
                    )
                    .addPropertyNode("yearOfDriverLicense")
                    .addConstraintViolation();

            return false;
        }

        if (licenseYear >= currentYear) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Driver's license year cannot be in the future."
                    )
                    .addPropertyNode("yearOfDriverLicense")
                    .addConstraintViolation();

            return false;
        }
        return true;
    }

    public boolean isLicenseAgeValid(LocalDate birthdate, Integer licenseYear, ConstraintValidatorContext context){
        if(birthdate==null || licenseYear==null)
            return true;
        int minLicenseYear=birthdate.getYear()+18;
        if (licenseYear<minLicenseYear){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Owner must be at least 18 years old when obtaining the driver's license.")
                    .addPropertyNode("yearOfDriverLicense")
                    .addConstraintViolation();
        }
        return true;
    }
}
