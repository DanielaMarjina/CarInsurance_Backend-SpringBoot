package com.danielamarjina.carinsurance.specification;

import com.danielamarjina.carinsurance.entity.InsurancePolicy;
import com.danielamarjina.carinsurance.enums.InsurancePolicyStatus;
import org.springframework.data.jpa.domain.Specification;

public class InsurancePolicySpecification {
    public static Specification<InsurancePolicy> hasProvider(String provider){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("provider"),provider));
    }

    public static Specification<InsurancePolicy> hasStatus(InsurancePolicyStatus status){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"),status));
    }
}
