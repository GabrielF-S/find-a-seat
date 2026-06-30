package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN Employee e WHERE UPPER(e.employeeName) LIKE UPPER(:name)  " +
            "AND r.date.reservationDay =:date")
    Reservation findByEmployee_EmployeeNameAndDate_ReservationDay(@Param("name") String name,@Param("date") LocalDate date);

    boolean existsBySeat_IdAndDate_reservationDay(UUID seatId, LocalDate localDate);

    List<Reservation> findBySeat_IdAndDate_reservationDay(UUID uuid, LocalDate reservationDay);

    List<Reservation> findBySeat_Id(UUID seatId);

    List<Reservation> findByDate_reservationDay(LocalDate localDate);

    boolean existsByEmployees_idAndActiveTrue(Long aLong);

    List<Reservation> findByEmployees_idAndActiveTrue(Long aLong);
}
