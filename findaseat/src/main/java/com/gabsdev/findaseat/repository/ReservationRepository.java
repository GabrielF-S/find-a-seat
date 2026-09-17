package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Reservation;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    Page<Reservation> findById(UUID id, Pageable pageable);

    @Query("SELECT r FROM Reservation r " +
            "INNER JOIN Employee e ON r.employees.id = e.id WHERE UPPER(e.employeeName) LIKE UPPER(:name)  " +
            "AND r.reservationPeriod.reservationDay =:date")
    Page<Reservation>findByEmployee_EmployeeNameAndReservationPeriod_ReservationDay(@Param("name") String name, @Param("date") LocalDate date, PageRequest pageRequest);

    boolean existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(UUID seatId, LocalDate localDate);

    Page<Reservation> findBySeat_IdAndReservationPeriod_reservationDay(UUID uuid, LocalDate reservationDay, Pageable pageable);

    List<Reservation> findBySeat_IdAndReservationPeriod_reservationDay(UUID uuid, LocalDate reservationDay);

    Page<Reservation> findBySeat_Id(UUID seatId, Pageable pageable);

    Page<Reservation> findByReservationPeriod_reservationDay(LocalDate localDate, Pageable pageable);

    boolean existsByEmployees_idAndActiveTrue(Long aLong);

    List<Reservation> findByEmployees_idAndActiveTrue(Long aLong);

    List<Reservation> findByActiveTrueAndReservationStatus(ReservationStatus reservationStatus);

    boolean existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrueAndReservationPeriod_StartTimeLocationLessThanEqualAndReservationPeriod_EndTimeLocationGreaterThanEqual(UUID uuid, LocalDate reservationDay, LocalTime endTimeLocation, LocalTime startTimeLocation);

    List<Reservation> findByActiveTrueAndReservationPeriod_ReservationDayLessThan(LocalDate now);
}
