package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Employee;
import com.gabsdev.findaseat.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmployees_Id(Long id);
}
