package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Seat;
import com.gabsdev.findaseat.model.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {



    Seat findByIdAndFloor_BusinessUuid(UUID id, UUID businessUuid);

    List<Seat> findByFloor_BusinessUuid(UUID businessUuid);

    List<Seat> findByFloorId(UUID floorUuid);

    boolean existsBySeatNameAndFloorId(String s, UUID floorUuid);

    List<Seat> findByType(Type type);

    @Query("""
            SELECT b.uuid FROM Seat s
            INNER JOIN Floor f
            ON s.floor.id = f.id
            INNER JOIN Business b
            ON f.business.uuid = b.uuid
            WHERE s.id = (:uuid)
            """)
    UUID getBusinessUuid(@Param("uuid") UUID uuid);

}
