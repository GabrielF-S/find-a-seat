package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.Date;
import com.gabsdev.findaseat.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    boolean existsByDate(Date data);


    boolean existsByDate_ReservationDay(LocalDate date);

    Date findByDate(LocalDate date);

    @Query("SELECT r FROM Reservation r " +
            "JOIN Employee e WHERE UPPER(e.employeeName) LIKE UPPER(:name)  " +
            "AND r.date.reservationDay =:date")
    Reservation findByEmployee_EmployeeNameAndDate_ReservationDay(@Param("name") String name,@Param("date") LocalDate date);

    boolean existsBySeat_IdAndDate_reservationDay(UUID seatId, LocalDate localDate);
}
