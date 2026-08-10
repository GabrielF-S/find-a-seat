package com.gabsdev.findaseat.repository;

import com.gabsdev.findaseat.model.entity.Waitlist;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {


    List<Waitlist> findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(ReservationStatus reservationStatus, LocalDate now);
}
