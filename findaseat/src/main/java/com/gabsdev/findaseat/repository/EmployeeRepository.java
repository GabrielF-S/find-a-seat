package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.dto.response.EmployeeResponse;
import com.gabsdev.findaseat.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE UPPER(e.employeeName) LIKE UPPER(:name)")
    Optional<List<Employee>> findByEmployeeName(@Param("name") String name);

    @Query("""
            SELECT e.business.uuid FROM Employee e WHERE e.id = :id
            """)
    UUID findBusinessUuid(@Param("id") Long id);

    @Query(
            """
                    SELECT e.id, e.employeeName, e.department, e.business.businessName FROM Employee e WHERE e.business.uuid = :businessUuid
                    """
    )
    List<EmployeeResponse> findByBusiness_Uuid(@Param("businessUuid") UUID businessId);
}
