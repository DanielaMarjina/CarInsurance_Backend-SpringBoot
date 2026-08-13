package com.danielamarjina.carinsurance.entity;

import com.danielamarjina.carinsurance.enums.CarCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 17, unique = true)
    private String vin;

    @Column(nullable = false, length = 100)
    private String make;

    @Column(nullable = false, length = 150)
    private String model;

    @Column(name = "year_of_manufacture", nullable = false)
    private Integer yearOfManufacture;

    @Enumerated(EnumType.STRING)
    @Column()
    private CarCategory category;

    @Column()
    private Integer cc;

    @Column()
    private Integer power;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

}
