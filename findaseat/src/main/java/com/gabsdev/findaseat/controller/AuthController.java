package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.LoginRequest;
import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.LoginResponse;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request);

    @PostMapping("/register")
    ResponseEntity<RegisterUserResponse> registerUserResponseResponseEntity(@Valid @RequestBody RegisterUserRequest request);

    @PostMapping("adm/register")
    ResponseEntity<RegisterUserResponse> registerAdminUser(@Valid @RequestBody RegisterUserRequest request);

    @PostMapping("super/register")
    ResponseEntity<RegisterUserResponse> registerSuperUser(@Valid @RequestBody RegisterUserRequest request);

}
