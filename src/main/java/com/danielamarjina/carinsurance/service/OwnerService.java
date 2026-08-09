package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.OwnerPatchRequest;
import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.enums.DriverLicenseCategory;
import com.danielamarjina.carinsurance.exception.OwnerNotFoundException;
import com.danielamarjina.carinsurance.mapper.OwnerMapper;
import com.danielamarjina.carinsurance.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerResponse createOwner(OwnerRequest ownerRequest){
        Owner owner=ownerMapper.toEntity(ownerRequest);
        Owner savedOwner=ownerRepository.save(owner);
        return ownerMapper.toResponse(savedOwner);
    }

    public OwnerResponse findOwnerByEmail(String email){
        Owner owner=ownerRepository.findByEmail(email)
                .orElseThrow(()->new OwnerNotFoundException(email));
        return ownerMapper.toResponse(owner);
    }

    public List<Owner> getAllOwners(DriverLicenseCategory category){
        if(category==null)
            return ownerRepository.findAll();
        return ownerRepository.findByDriverLicenseCategory(category);
    }

    public OwnerResponse findOwnerById(UUID id){
        Owner owner=ownerRepository.findById(id)
                .orElseThrow(()->new OwnerNotFoundException(id));
        return ownerMapper.toResponse(owner);
    }

    public OwnerResponse updateOwner(UUID id, OwnerRequest ownerRequest){
        Owner owner=ownerRepository.findById(id)
                .orElseThrow(()->new OwnerNotFoundException(id));
        ownerMapper.updateEntity(ownerRequest,owner);
        return ownerMapper.toResponse(ownerRepository.save(owner));
    }

    public OwnerResponse patchOwner(UUID id, OwnerPatchRequest ownerPatchRequest){
        Owner owner=ownerRepository.findById(id)
                .orElseThrow(()->new OwnerNotFoundException(id));
        ownerMapper.patchEntity(ownerPatchRequest,owner);
        return ownerMapper.toResponse(ownerRepository.save(owner));
    }

    public void deleteOwner(UUID id){
        Owner owner=ownerRepository.findById(id)
                .orElseThrow(()->new OwnerNotFoundException(id));
        ownerRepository.delete(owner);
    }
}
