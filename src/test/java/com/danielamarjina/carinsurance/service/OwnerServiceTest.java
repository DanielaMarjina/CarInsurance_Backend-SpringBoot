package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.exception.OwnerNotFoundException;
import com.danielamarjina.carinsurance.mapper.OwnerMapper;
import com.danielamarjina.carinsurance.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private OwnerMapper ownerMapper;

    @InjectMocks
    private OwnerService ownerService;

    @Test
    void createOwner_shouldCreateOwner() {
        //ARRANGE
        OwnerRequest ownerRequest = new OwnerRequest();
        Owner owner = new Owner();
        OwnerResponse ownerResponse = new OwnerResponse();

        when(ownerMapper.toEntity(ownerRequest))
                .thenReturn(owner);

        when(ownerRepository.save(owner))
                .thenReturn(owner);

        when(ownerMapper.toResponse(owner))
                .thenReturn(ownerResponse);

        //ACT
        OwnerResponse result = ownerService.createOwner(ownerRequest);

        //ASSERT
        assertEquals(ownerResponse, result);

        //VERIFY
        verify(ownerMapper).toEntity(ownerRequest);
        verify(ownerRepository).save(owner);
        verify(ownerMapper).toResponse(owner);
    }

    @Test
    void findOwnerByEmail_shouldReturnOwner_whenOwnerExists(){
        String email="test@gmail.com";
        Owner owner=new Owner();
        OwnerResponse ownerResponse=new OwnerResponse();

        when(ownerRepository.findByEmail(email))
                .thenReturn(Optional.of(owner));

        when(ownerMapper.toResponse(owner))
                .thenReturn(ownerResponse);

        OwnerResponse result=ownerService.findOwnerByEmail(email);

        assertEquals(ownerResponse,result);

        verify(ownerRepository).findByEmail(email);
        verify(ownerMapper).toResponse(owner);
    }

    @Test
    void findOwnerByEmail_shouldThrowException_whenOwnerDoesNotExist(){
        String email="test@gmail.com";

        when(ownerRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(OwnerNotFoundException.class,
                ()->ownerService.findOwnerByEmail(email));

        verify(ownerRepository).findByEmail(email);
    }
}