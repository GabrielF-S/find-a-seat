package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = """
            SELECT * 
                FROM tb_employees AS e
                WHERE to_tsvector('portuguese', coalesce(employee_name)) 
                    @@ to_tsquery('portuguese', :name)
                AND e.tb_business_uuid = :businessUuid
            """, nativeQuery = true)
    Page<Employee> findByEmployeeName(@Param("name") String name, @Param("businessUuid") UUID businessUuid, PageRequest pageRequest);

    @Query("""
            SELECT e.business.uuid FROM Employee e WHERE e.id = :id
            """)
    UUID findBusinessUuid(@Param("id") Long id);

    @Query(
            """
                    SELECT e  FROM Employee e WHERE e.business.uuid = :businessUuid
                    """
    )
    Page<Employee> findByBusiness_Uuid(@Param("businessUuid") UUID businessId, PageRequest pageRequest);
}


