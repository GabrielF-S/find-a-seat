package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.EmployeeRequest;
import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.model.entity.Employee;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface EmployeeController {

    @PostMapping(value = "/{businessUuid}")
    ResponseEntity<EmployeeResponse> registerEmployee(@RequestBody EmployeeRequest employeeRequest,
                                                      @PathVariable(value = "businessUuid") UUID businessUuid);

    @GetMapping(value = "/getEmployeeByName")
    ResponseEntity<List<EmployeeResponse>> getEmployesByName(
            @RequestParam(name = "name", required = true, defaultValue = "") String name);

    @GetMapping(value = "/{businessUuid}/{employeeId}")
    ResponseEntity<EmployeeResponse> getEmployeeyId(
            @PathVariable(value = "businessUuid")UUID businessUuid,
            @PathVariable(value = "employeeId") Long employeeId
            );


    @GetMapping
    ResponseEntity<List<EmployeeResponse>> getAllEmployess(@PathVariable UUID businessId);

    @PutMapping
    ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee);

    @DeleteMapping(value="/{id}")
    ResponseEntity<Void> deleteEmployeeById(Long id);
}
