package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE UPPER(e.employeeName) LIKE UPPER(:name)")
    Optional<List<Employee>> findByEmployeeName(@Param("name") String name);

    @Query("""
            SELECT e.business.uuid FROM Employee e WHERE e.id = :id
            """)
    UUID findBusinessUuid(@Param("id") Long id);

}
