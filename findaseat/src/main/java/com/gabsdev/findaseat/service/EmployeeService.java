package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.EmployeeRequest;
import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.model.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest, UUID businessUuid);

    Page<EmployeeResponse> getEmployees(UUID businessUuid, String name, Integer page, Integer size);

    EmployeeResponse getEmployeeyId(UUID businessUuid, Long employeeId);

    Employee updateEmployee(Employee employee);

    void deleteById(Long id);

    Page<EmployeeResponse> getAllEmployees(UUID businessId, Integer page, Integer size);
}
