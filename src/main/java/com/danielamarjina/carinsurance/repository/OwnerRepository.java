package com.danielamarjina.carinsurance.repository;

import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {

    Optional<Owner> findByEmail(String email);


    List<Owner> findByDriverLicenseCategory(DriverLicenseCategory category);
}
