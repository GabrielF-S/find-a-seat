package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.EmployeeRequest;
import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.model.entity.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);

    List<EmployeeResponse> getEmployees(String name);

    EmployeeResponse getEmployeeyId(UUID businessUuid, Long employeeId);

    Employee updateEmployee(Employee employee);

    void deleteById(Long id);
}
