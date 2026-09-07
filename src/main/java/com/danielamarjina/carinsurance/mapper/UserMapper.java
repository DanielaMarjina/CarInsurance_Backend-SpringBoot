package com.danielamarjina.carinsurance.mapper;

import com.danielamarjina.carinsurance.dto.request.RegisterRequest;
import com.danielamarjina.carinsurance.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", constant = "EMPLOYEE")
    User toEntity(RegisterRequest request);
}
