package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.OwnerPatchRequest;
import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import com.danielamarjina.carinsurance.exception.OwnerNotFoundException;
import com.danielamarjina.carinsurance.mapper.OwnerMapper;
import com.danielamarjina.carinsurance.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Test
    void getAllOwners_shouldReturnAllOwners_whenCategoryIsNull(){
        List<Owner> owners= List.of(
                new Owner(),
                new Owner()
        );

        when(ownerRepository.findAll())
                .thenReturn(owners);

        List<Owner> result=ownerService.getAllOwners(null);

        assertEquals(owners,result);

        verify(ownerRepository).findAll();
    }

    @Test
    void getAllOwners_shouldReturnOwnersFilteredByCategory_whenCategoryIsNotNull(){
        DriverLicenseCategory category=DriverLicenseCategory.A;
        List<Owner> owners=List.of(
                new Owner(),
                new Owner()
        );

        when(ownerRepository.findByDriverLicenseCategory(category))
                .thenReturn(owners);

        List<Owner> result=ownerService.getAllOwners(category);

        assertEquals(owners,result);

        verify(ownerRepository).findByDriverLicenseCategory(category);
    }

    @Test
    void findOwnerById_shouldReturnOwner_whenOwnerExists(){
        UUID id = UUID.randomUUID();
        Owner owner=new Owner();
        OwnerResponse ownerResponse=new OwnerResponse();

        when(ownerRepository.findById(id))
                .thenReturn(Optional.of(owner));

        when(ownerMapper.toResponse(owner))
                .thenReturn(ownerResponse);

        OwnerResponse result=ownerService.findOwnerById(id);

        assertEquals(ownerResponse,result);

        verify(ownerRepository).findById(id);
        verify(ownerMapper).toResponse(owner);

    }

    @Test
    void findOwnerById_shouldThrowException_whenOwnerDoesNotExist(){
        UUID id = UUID.randomUUID();
        when(ownerRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class,
                ()->ownerService.findOwnerById(id));
        verify(ownerRepository).findById(id);
    }

    @Test
    void updateOwner_shouldUpdateOwner_whenOwnerExists(){
        UUID id = UUID.randomUUID();
        OwnerRequest ownerRequest=new OwnerRequest();
        Owner owner=new Owner();
        OwnerResponse ownerResponse=new OwnerResponse();

        when(ownerRepository.findById(id))
                .thenReturn(Optional.of(owner));

        when(ownerRepository.save(owner))
                .thenReturn(owner);

        when(ownerMapper.toResponse(owner))
                .thenReturn(ownerResponse);

        OwnerResponse result=ownerService.updateOwner(id,ownerRequest);

        assertEquals(ownerResponse,result);

        verify(ownerRepository).findById(id);
        verify(ownerMapper).updateEntity(ownerRequest,owner);
        verify(ownerRepository).save(owner);
        verify(ownerMapper).toResponse(owner);

    }

    @Test
    void updateOwner_shouldThrowException_whenOwnerDoesNotExist(){
        UUID id = UUID.randomUUID();
        OwnerRequest ownerRequest=new OwnerRequest();
        when(ownerRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class,
                ()->ownerService.updateOwner(id,ownerRequest));
        verify(ownerRepository).findById(id);
    }

    @Test
    void patchOwner_shouldPatchOwner_whenOwnerExists(){
        UUID id = UUID.randomUUID();
        OwnerPatchRequest ownerPatchRequest=new OwnerPatchRequest();
        Owner owner=new Owner();
        OwnerResponse ownerResponse=new OwnerResponse();

        when(ownerRepository.findById(id))
                .thenReturn(Optional.of(owner));

        when(ownerRepository.save(owner))
                .thenReturn(owner);

        when(ownerMapper.toResponse(owner))
                .thenReturn(ownerResponse);

        OwnerResponse result=ownerService.patchOwner(id,ownerPatchRequest);

        assertEquals(ownerResponse,result);

        verify(ownerRepository).findById(id);
        verify(ownerMapper).patchEntity(ownerPatchRequest,owner);
        verify(ownerRepository).save(owner);
        verify(ownerMapper).toResponse(owner);
    }

    @Test
    void patchOwner_shouldThrowException_whenOwnerDoesNotExist(){
        UUID id = UUID.randomUUID();
        OwnerPatchRequest ownerPatchRequest=new OwnerPatchRequest();
        when(ownerRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class,
                ()->ownerService.patchOwner(id,ownerPatchRequest));
        verify(ownerRepository).findById(id);
    }

    @Test
    void deleteOwner_shouldDeleteOwner_whenOwnerExists(){
        UUID id = UUID.randomUUID();
        Owner owner=new Owner();
        when(ownerRepository.findById(id))
                .thenReturn(Optional.of(owner));
        ownerService.deleteOwner(id);

        verify(ownerRepository).findById(id);
        verify(ownerRepository).delete(owner);

    }

    @Test
    void deleteOwner_shouldThrowException_whenOwnerDoesNotExist(){
        UUID id = UUID.randomUUID();
        when(ownerRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class,
                ()->ownerService.deleteOwner(id));
        verify(ownerRepository).findById(id);
    }
}