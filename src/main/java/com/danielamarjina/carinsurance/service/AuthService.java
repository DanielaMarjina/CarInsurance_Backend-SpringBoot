package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.LoginRequest;
import com.danielamarjina.carinsurance.dto.request.RegisterRequest;
import com.danielamarjina.carinsurance.dto.response.LoginResponse;
import com.danielamarjina.carinsurance.entity.User;
import com.danielamarjina.carinsurance.exception.UserAlreadyExistsException;
import com.danielamarjina.carinsurance.mapper.UserMapper;
import com.danielamarjina.carinsurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private static final Logger log= LoggerFactory.getLogger(AuthService.class);

    public void register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException(request.getUsername());
        if(userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException(request.getEmail());
        User user=userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        log.info("New user registered successfully: {}", user.getEmail());
    }

    public LoginResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String token=jwtService.generateToken(request.getEmail());
        log.info("User logged in successfully: {}", request.getEmail());
        return new LoginResponse(token);
    }
}
