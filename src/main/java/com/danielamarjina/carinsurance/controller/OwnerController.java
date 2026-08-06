package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.entity.Owner;
import com.danielamarjina.carinsurance.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping
    public List<Owner> getAllOwners(){
        return ownerService.getAllOwners();
    }

    @PostMapping
    public OwnerResponse createOwner(
            @Valid @RequestBody OwnerRequest ownerRequest) {
        return ownerService.createOwner(ownerRequest);
    }

    @GetMapping("/search")
    public OwnerResponse findOwnerByEmail(@RequestParam String email) {
        return ownerService.findOwnerByEmail(email);
    }

    @GetMapping("/{id}")
    public OwnerResponse findOwnerById(@PathVariable UUID id){
        return ownerService.findOwnerById(id);
    }

    @PutMapping("/update")
    public OwnerResponse updateOwner(@RequestParam UUID id, @Valid @RequestBody OwnerRequest ownerRequest){
        return ownerService.updateOwner(id,ownerRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable UUID id){
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }



}
