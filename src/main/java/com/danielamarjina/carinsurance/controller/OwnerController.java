package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.OwnerRequest;
import com.danielamarjina.carinsurance.dto.response.OwnerResponse;
import com.danielamarjina.carinsurance.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping
    public OwnerResponse createOwner(
            @Valid @RequestBody OwnerRequest ownerRequest) {
        return ownerService.createOwner(ownerRequest);
    }

    @GetMapping(params = "email")
    public OwnerResponse findOwnerByEmail(@RequestParam String email) {
        return ownerService.findOwnerByEmail(email);
    }

}
