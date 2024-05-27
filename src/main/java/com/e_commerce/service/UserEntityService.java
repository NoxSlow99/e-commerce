package com.e_commerce.service;

import com.e_commerce.api.dto.AuthCreateUserRequest;
import com.e_commerce.api.dto.AuthResponse;
import com.e_commerce.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserEntityService {
    AuthResponse createUser(AuthCreateUserRequest createUserRequest);
    Optional<UserEntity> findById(int id);
    List<UserEntity> findAll();
}
