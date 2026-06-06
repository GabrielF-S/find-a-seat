package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.exception.ConflictReservationException;
import com.gabsdev.findaseat.exception.EmployeeNotFoundException;
import com.gabsdev.findaseat.exception.ReservationNotFoundException;
import com.gabsdev.findaseat.exception.SeatNotFoundException;
import com.gabsdev.findaseat.mapper.ReservationMapper;
import com.gabsdev.findaseat.model.Date;
import com.gabsdev.findaseat.model.Reservation;
import com.gabsdev.findaseat.repository.EmployeeRepository;
import com.gabsdev.findaseat.repository.ReservationRepository;
import com.gabsdev.findaseat.repository.SeatRepository;
import com.gabsdev.findaseat.service.ReservationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service


public class ReservationServiceImpl implements ReservationService {


    private final ReservationRepository repository;
    private final SeatRepository seatRepository;
    private final EmployeeRepository employeeRepository;
    private final ReservationMapper mapper;

    public ReservationServiceImpl(ReservationRepository repository, SeatRepository seatRepository, EmployeeRepository employeeRepository, ReservationMapper mapper) {
        this.repository = repository;
        this.seatRepository = seatRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }


    @Override
    public ReservationResponse createReservation(ReservationRequest reservation) {
        verifyReservationDate(reservation);
        Reservation reservationToSave = getReservation(reservation);
        Reservation saved = repository.save(reservationToSave);
        return mapper.toReservationResponse(saved);

    }

    @Override
    public ReservationResponse getReservation(UUID reservationId, String employeeName, LocalDate date) {
        if (reservationId != null){
           return findById(reservationId);
        }
        if (date== null){
            date = LocalDate.now();
        }
       Reservation reservation =  repository.findByEmployee_EmployeeNameAndDate_ReservationDay("%"+employeeName+"%", date);
        return mapper.toReservationResponse(reservation);
    }

    @Override
    public ReservationResponse updateReservation(Reservation reservation) {
        Reservation updated = repository.save(reservation);
        return mapper.toReservationResponse(updated);
    }

    @Override
    public void deleteById(UUID uuid) {
        if (!repository.existsById(uuid)){
            throw new ReservationNotFoundException("Reservation not found");
        }
        repository.deleteById(uuid);
    }

    private ReservationResponse findById(UUID reservationId) {
        Reservation reservation = repository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException("Reserva não localizada"));
        return mapper.toReservationResponse(reservation);
    }

    private void verifyReservationDate(ReservationRequest reservation) {
        if (repository.existsByDate_ReservationDay(reservation.data().getReservationDay())){
            Date date = repository.findByDate(reservation.data().getReservationDay());
            if (reservation.data().getStartTimeLocation().isAfter(date.getEndTimeLocation())){
                throw new ConflictReservationException("Há um conflito de horário entre as reservas");
            }
        }
    }

    private Reservation getReservation(ReservationRequest reservation) {
        Reservation reservationToSave = new Reservation();
        if (!seatRepository.existsById(reservation.seatId())){
            throw  new SeatNotFoundException("Não foi localizado um seat de ID: " + reservation.seatId());
        }
        reservationToSave.setSeat(seatRepository.findById(reservation.seatId()).get());
        if (!employeeRepository.existsById(reservation.employeId())){
            throw new EmployeeNotFoundException("Funcionario não localizado!");
        }
        reservationToSave.setEmployees(employeeRepository.getReferenceById(reservation.employeId()));
        reservationToSave.setDate(reservation.data());
        return reservationToSave;
    }
}
