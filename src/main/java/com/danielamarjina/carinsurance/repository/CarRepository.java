package com.danielamarjina.carinsurance.repository;

import com.danielamarjina.carinsurance.entity.Car;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
    @Override
    @EntityGraph(attributePaths = "owner")
    List<Car> findAll();
}
