package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.mapper.OwnerMapper;
import com.danielamarjina.carinsurance.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
