package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.request.RegisterRequest;
import com.danielamarjina.carinsurance.entity.User;
import com.danielamarjina.carinsurance.exception.UserAlreadyExistsException;
import com.danielamarjina.carinsurance.mapper.UserMapper;
import com.danielamarjina.carinsurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException(String.format("User with '%s' username already exists",
                    request.getUsername()));
        if(userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException(String.format("User with '%s' email already exists",
                    request.getEmail()));
        User user=userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
