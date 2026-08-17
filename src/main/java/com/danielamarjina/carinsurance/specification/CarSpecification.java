package com.danielamarjina.carinsurance.specification;

import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.enums.CarCategory;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CarSpecification {
    public static Specification<Car> hasMake(String make){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("make"),make));
    }

    public static Specification<Car> hasModel(String model){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("model"),model));
    }

    public static Specification<Car> hasCategory(CarCategory category){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"),category));
    }

    public static Specification<Car> hasOwnerId(UUID ownerId){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("owner").get("id"),ownerId));
    }
}
