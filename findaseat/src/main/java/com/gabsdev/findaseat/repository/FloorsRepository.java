package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Floor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FloorsRepository extends JpaRepository<Floor, UUID> {

    Page<Floor> findByBusinessUuid(UUID businessUuid, PageRequest pageRequest);

    boolean existsByfloorNameAndBusinessUuidAndTowerName(String floorName, UUID uuid, String string);

    boolean existsBytowerName(String s);
}
