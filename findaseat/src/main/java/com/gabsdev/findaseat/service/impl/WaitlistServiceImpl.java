package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.dto.response.WaitlistResponse;
import com.gabsdev.findaseat.exception.EmployeeNotFoundException;
import com.gabsdev.findaseat.exception.WaitlistNotFoundException;
import com.gabsdev.findaseat.model.entity.Employee;
import com.gabsdev.findaseat.model.entity.Reservation;
import com.gabsdev.findaseat.model.entity.Seat;
import com.gabsdev.findaseat.model.entity.Waitlist;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import com.gabsdev.findaseat.model.enums.Type;
import com.gabsdev.findaseat.repository.EmployeeRepository;
import com.gabsdev.findaseat.repository.ReservationRepository;
import com.gabsdev.findaseat.repository.SeatRepository;
import com.gabsdev.findaseat.repository.WaitlistRepository;
import com.gabsdev.findaseat.service.ReservationService;
import com.gabsdev.findaseat.service.WaitlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WaitlistServiceImpl implements WaitlistService {
    private final WaitlistRepository repository;
    private final EmployeeRepository employeeRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ReservationService reservationService;
    private final Long TOLERANCE_AWAIT_TIME = 5L;

    public WaitlistServiceImpl(WaitlistRepository repository, EmployeeRepository employeeRepository, ReservationRepository reservationRepository, SeatRepository seatRepository, ReservationService reservationService) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
        this.reservationService = reservationService;
    }

    @Override
    public WaitlistResponse updateStatus(Long waitlistId, ReservationStatus status) {
        log.info("Verificando liberação de mesa");
        Waitlist waitlist = repository.findById(waitlistId)
                .orElseThrow(() -> new WaitlistNotFoundException("Not found"));
        waitlist.setReservationStatus(status);
        Waitlist saved = repository.save(waitlist);
        Employee employeeRepositoryById = employeeRepository.findById(saved.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employe not found"));
        return new WaitlistResponse(
                saved.getId(),
                employeeRepositoryById.getEmployeeName(),
                saved.getReservationDay(),
                saved.getDuration(),
                saved.getReservationStatus());
    }

    @Override
    public void verifyStatusWaitlist() {
        log.info("Verificando confirmações de lista de espera");
       List<Waitlist> list = repository.findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(ReservationStatus.PENDING, LocalDate.now());
       list.forEach(waitlist -> {
           if (Duration.between(waitlist.getCreatedAt(), LocalDateTime.now()).toMinutes()>15L){
               waitlist.setReservationStatus(ReservationStatus.NOT_CONFIRMED);
               repository.save(waitlist);
           }
       });
    }

    @Transactional
    @Override
    public void verifySeatsAvaliable(Waitlist waitlist) {
        List<Seat> seatList = seatRepository
                .findByTypeAndFloor_Business_Uuid(Type.TABLE, waitlist.getBusinessUuid());
        List<Seat> seats = seatList.stream()
                .filter(seat ->
                        !reservationRepository.existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(
                                seat.getId(), waitlist.getReservationDay())
                ).toList();
        if (!seats.isEmpty()){
            ReservationResponse response = reservationService.createReservation(
                    new ReservationRequest(LocalDate.now(), seats.getFirst().getId(), waitlist.getEmployeeId()),
                    LocalTime.now()
                            .plusMinutes(TOLERANCE_AWAIT_TIME),
                    LocalTime.now()
                            .plusMinutes(waitlist.getDuration().toMinutes()).plusMinutes(TOLERANCE_AWAIT_TIME));
            waitlist.setReservationStatus(ReservationStatus.FINISHED);
            repository.save(waitlist);
            Reservation byId = reservationRepository.findById(response.id()).get();
            reservationService.sendNotificationReservation(byId);
        }

    }

    @Override
    public List<Waitlist> getWaitlist() {
        return repository.findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(ReservationStatus.CONFIRMED, LocalDate.now());
    }
}
