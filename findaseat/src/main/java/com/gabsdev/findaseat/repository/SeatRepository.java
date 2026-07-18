package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Seat;
import com.gabsdev.findaseat.model.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {



    Seat findByIdAndFloor_BusinessUuid(UUID id, UUID businessUuid);

    List<Seat> findByFloor_BusinessUuid(UUID businessUuid);

    List<Seat> findByFloorId(UUID floorUuid);

    boolean existsBySeatNameAndFloorId(String s, UUID floorUuid);

    List<Seat> findByType(Type type);

}
