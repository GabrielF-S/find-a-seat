package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FloorsRepository extends JpaRepository<Floor, UUID> {
}
