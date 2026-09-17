package com.gabsdev.findaseat.controller;

import com.gabsdev.findaseat.dto.request.EmployeeRequest;
import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.model.entity.Employee;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default server URL")})
public interface EmployeeController {

    @PostMapping(value = "/{businessUuid}")
    ResponseEntity<EmployeeResponse> registerEmployee(@RequestBody EmployeeRequest employeeRequest,
                                                      @PathVariable(value = "businessUuid") UUID businessUuid);

    @GetMapping(value = "/{businessUuid}/getEmployeeByName")
    ResponseEntity<Page<EmployeeResponse>> getEmployesByName(
            @PathVariable(value = "businessUuid") UUID businessUuid,
            @RequestParam(name = "name", required = true, defaultValue = "") String name,
            @RequestParam(value="page", defaultValue = "0") Integer page,
            @RequestParam(value="size", defaultValue = "10") Integer size);

    @GetMapping(value = "/{businessUuid}/{employeeId}")
    ResponseEntity<EmployeeResponse> getEmployeeyId(
            @PathVariable(value = "businessUuid")UUID businessUuid,
            @PathVariable(value = "employeeId") Long employeeId
            );


    @GetMapping(value = "/{businessUuid}/getAll")
    ResponseEntity<Page<EmployeeResponse>> getAllEmployess(@PathVariable(value = "businessUuid") UUID businessUuid,
                                                           @RequestParam(value="page", defaultValue = "0") Integer page,
                                                           @RequestParam(value="size", defaultValue = "10") Integer size);

    @PutMapping
    ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee);

    @DeleteMapping(value="/{id}")
    ResponseEntity<Void> deleteEmployeeById(Long id);
}
