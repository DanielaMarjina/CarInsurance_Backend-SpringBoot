package com.danielamarjina.carinsurance.validation;

import com.danielamarjina.carinsurance.dto.request.InsurancePolicyRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InsurancePolicyValidator implements ConstraintValidator<ValidInsurancePolicy, InsurancePolicyRequest> {

    @Override
    public boolean isValid(InsurancePolicyRequest request, ConstraintValidatorContext context){
        if(request==null)
            return true;
        if(request.getStartDate()==null || request.getEndDate()==null)
            return true;
        if(!request.getStartDate().isBefore(request.getEndDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End Date cannot be before Start Date")
                    .addPropertyNode("endDate")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
