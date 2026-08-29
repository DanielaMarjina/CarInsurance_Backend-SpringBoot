package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.request.LoginRequest;
import com.danielamarjina.carinsurance.dto.request.RegisterRequest;
import com.danielamarjina.carinsurance.dto.response.LoginResponse;
import com.danielamarjina.carinsurance.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
