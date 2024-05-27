package com.e_commerce.api.controller;

import com.e_commerce.api.dto.AuthCreateUserRequest;
import com.e_commerce.api.dto.AuthResponse;
import com.e_commerce.service.impl.UserEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserEntityServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrerUser(@RequestBody AuthCreateUserRequest createUserRequest) {
        return new ResponseEntity<>(this.userService.createUser(createUserRequest), HttpStatus.CREATED);
    }
}
