package com.danielamarjina.carinsurance.repository;

import com.danielamarjina.carinsurance.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClaimRepository extends JpaRepository<Claim,UUID> {

}
