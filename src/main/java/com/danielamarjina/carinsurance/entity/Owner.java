package com.danielamarjina.carinsurance.entity;

import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(name="year_of_driver_license",nullable = false)
    private Integer yearOfDriverLicense;

    @Enumerated(EnumType.STRING)
    @Column(name = "driver_license_cat")
    private DriverLicenseCategory driverLicenseCategory;

    @Column(unique = true, length = 150)
    private String email;

}
