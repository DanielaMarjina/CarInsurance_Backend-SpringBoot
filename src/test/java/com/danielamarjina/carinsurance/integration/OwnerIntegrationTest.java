package com.danielamarjina.carinsurance.integration;

import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import com.danielamarjina.carinsurance.repository.OwnerRepository;
import com.danielamarjina.carinsurance.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class OwnerIntegrationTest {
    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void createOwner_shouldSaveOwnerToDB(){
        OwnerRequest ownerRequest = OwnerRequest.builder()
                .name("John Doe")
                .birthdate(LocalDate.of(1995, 5, 10))
                .yearOfDriverLicense(2015)
                .driverLicenseCategory(DriverLicenseCategory.B)
                .email("john.integration@test.com")
                .build();

        OwnerResponse ownerResponse=ownerService.createOwner(ownerRequest);

        assertNotNull(ownerResponse);
        assertNotNull(ownerResponse.getId());

        Optional<Owner> savedOwner=ownerRepository.findById(ownerResponse.getId());

        assertTrue(savedOwner.isPresent());
        assertEquals("John Doe", savedOwner.get().getName());

    }

    @Test
    void findOwnerByEmail_shouldReturnSavedOwner() {
        Owner owner = Owner.builder()
                .name("Maria Popescu")
                .birthdate(LocalDate.of(1995, 5, 10))
                .yearOfDriverLicense(2015)
                .driverLicenseCategory(DriverLicenseCategory.B)
                .email("maria.integration@test.com")
                .build();

        ownerRepository.save(owner);

        OwnerResponse response =
                ownerService.findOwnerByEmail("maria.integration@test.com");

        assertNotNull(response);
        assertEquals("Maria Popescu", response.getName());
        assertEquals("maria.integration@test.com", response.getEmail());
    }
}
