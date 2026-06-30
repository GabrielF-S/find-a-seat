package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;

public interface UserService {


    RegisterUserResponse createUser(RegisterUserRequest userToRegister);
}
