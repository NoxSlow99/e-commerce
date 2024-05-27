package com.e_commerce.api.dto;

public record AuthCreateUserRequest(
        String username,
        String password,
        String email,
        String firstName,
        String lastName
) {
}
