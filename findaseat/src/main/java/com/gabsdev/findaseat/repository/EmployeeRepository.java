package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
