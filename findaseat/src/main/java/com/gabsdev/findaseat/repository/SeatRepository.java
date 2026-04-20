package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, Long> {



    Seat findByIdAndFloor_BusinessUuid(Long id, UUID businessUuid);

    List<Seat> findByFloor_BusinessUuid(UUID businessUuid);

    List<Seat> findByFloorId(UUID floorUuid);

    boolean existsBySeatNameAndFloorId(String s, UUID floorUuid);
}
