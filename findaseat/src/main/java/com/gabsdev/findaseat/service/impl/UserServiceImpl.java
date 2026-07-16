package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.RegisterUserRequest;
import com.gabsdev.findaseat.dto.response.RegisterUserResponse;
import com.gabsdev.findaseat.exception.EmployeeNotFoundException;
import com.gabsdev.findaseat.mapper.UserMapper;
import com.gabsdev.findaseat.model.entity.Employee;
import com.gabsdev.findaseat.model.entity.User;
import com.gabsdev.findaseat.repository.EmployeeRepository;
import com.gabsdev.findaseat.repository.UserRepository;
import com.gabsdev.findaseat.service.UserService;

public class UserServiceImpl implements UserService {
    private  final UserRepository repository;
    private final EmployeeRepository employeeRepository;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository, EmployeeRepository employeeRepository, UserMapper mapper) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    @Override
    public RegisterUserResponse createUser(RegisterUserRequest userToRegister) {
        User user = mapper.toUser(userToRegister);
              Employee employee = employeeRepository.findById(userToRegister.employeeID()).orElseThrow(
                () ->  new EmployeeNotFoundException("Não foi possivel localizar um funcionario com este id"));
        user.setEmployees(employee);
        User saved = repository.save(user);

        return mapper.toUserResponse(saved);
    }
}
