package com.gabsdev.findaseat.controller.impl;

import com.gabsdev.findaseat.controller.EmployeeController;
import com.gabsdev.findaseat.dto.request.EmployeeRequest;
import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.model.entity.Employee;
import com.gabsdev.findaseat.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("api/employees")
@CrossOrigin("*")
public class EmployeeControllerImpl implements EmployeeController {

    private final EmployeeService service;

    public EmployeeControllerImpl(EmployeeService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<EmployeeResponse> registerEmployee( EmployeeRequest employeeRequest, UUID businessuuid) {
        EmployeeResponse response = service.createEmployee(employeeRequest, businessuuid);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<List<EmployeeResponse>> getEmployesByName(String name) {
       List<EmployeeResponse> responseList = service.getEmployees(name);
        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<EmployeeResponse> getEmployeeyId(UUID businessUuid, Long employeeId) {
        return ResponseEntity.ok(service.getEmployeeyId(businessUuid, employeeId));
    }

    @Override
    public ResponseEntity<List<EmployeeResponse>> getAllEmployess(UUID businessId) {
        return null;
    }

    @Override
    public ResponseEntity<Employee> updateEmployee(Employee employee) {

        return ResponseEntity.ok(service.updateEmployee(employee));
    }

    @Override
    public ResponseEntity<Void> deleteEmployeeById(Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();

    }

}
