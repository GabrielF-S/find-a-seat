package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.LoginRequest;
import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.LoginResponse;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import com.gabsdev.findaseat.model.entity.Employee;
import com.gabsdev.findaseat.model.entity.User;

public interface AuthService {
    RegisterUserResponse createComumUser(RegisterUserRequest request);

    RegisterUserResponse createSuperUser(RegisterUserRequest request);

    RegisterUserResponse createAdminUser(RegisterUserRequest request);

    User createUser(Employee employee, String email, String password);

    LoginResponse login(LoginRequest request);

}
