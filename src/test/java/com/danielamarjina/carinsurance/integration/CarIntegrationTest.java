package com.danielamarjina.carinsurance.integration;

import com.danielamarjina.carinsurance.dto.request.CarRequest;
import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.enums.CarCategory;
import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.repository.OwnerRepository;
import com.danielamarjina.carinsurance.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CarIntegrationTest {
    @Autowired
    private OwnerRepository ownerRepository;
    
    @Autowired 
    private CarService carService;
    
    @Autowired
    private CarRepository carRepository;
    
    @Test
    void createCar_shouldSaveCarAndItsOwnerToDB(){
        Owner owner = Owner.builder()
                .name("Maria Popescu")
                .birthdate(LocalDate.of(1995, 5, 10))
                .yearOfDriverLicense(2015)
                .driverLicenseCategory(DriverLicenseCategory.B)
                .email("maria.car.integration@test.com")
                .build();

        Owner savedOwner = ownerRepository.save(owner);

        CarRequest request = CarRequest.builder()
                .vin("WVWZZZ1JZXW000001")
                .make("Volkswagen")
                .model("Golf")
                .yearOfManufacture(2020)
                .category(CarCategory.EURO6)
                .cc(1600)
                .power(110)
                .ownerId(savedOwner.getId())
                .build();

        CarResponse response = carService.createCar(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Volkswagen", response.getMake());
        assertNotNull(response.getOwner());
        assertEquals(savedOwner.getId(), response.getOwner().getId());

        Optional<Car> savedCar=carRepository.findById(response.getId());
        assertTrue(savedCar.isPresent());
        assertEquals("WVWZZZ1JZXW000001", savedCar.get().getVin());
        assertEquals(savedOwner.getId(), savedCar.get().getOwner().getId());

    }
}
