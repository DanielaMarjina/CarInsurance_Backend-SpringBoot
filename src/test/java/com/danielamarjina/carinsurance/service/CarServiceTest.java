package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.CarRequest;
import com.danielamarjina.carinsurance.dto.response.CarResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.exception.OwnerNotFoundException;
import com.danielamarjina.carinsurance.mapper.CarMapper;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void createCar_shouldCreateCar_whenOwnerExists(){
        CarRequest carRequest=new CarRequest();
        UUID ownerId= UUID.randomUUID();
        carRequest.setOwnerId(ownerId);
        Owner owner=new Owner();
        Car car=new Car();
        CarResponse carResponse=new CarResponse();

        when(ownerRepository.findById(carRequest.getOwnerId()))
                .thenReturn(Optional.of(owner));
        when(carMapper.toEntity(carRequest))
                .thenReturn(car);
        when(carRepository.save(car))
                .thenReturn(car);
        when(carMapper.toResponse(car))
                .thenReturn(carResponse);
        CarResponse result=carService.createCar(carRequest);
        assertEquals(carResponse,result);

        verify(ownerRepository).findById(carRequest.getOwnerId());
        verify(carMapper).toEntity(carRequest);
        verify(carRepository).save(car);
        verify(carMapper).toResponse(car);
    }

    @Test
    void createCar_shouldThrowException_whenOwnerDoesNotExist(){
        UUID ownerId=UUID.randomUUID();
        CarRequest carRequest=new CarRequest();
        carRequest.setOwnerId(ownerId);
        when(ownerRepository.findById(ownerId))
                .thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class,
                ()->carService.createCar(carRequest));
        verify(ownerRepository).findById(ownerId);
    }
}