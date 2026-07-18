package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.QuickReservationRequest;
import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.exception.*;
import com.gabsdev.findaseat.mapper.ReservationMapper;
import com.gabsdev.findaseat.model.entity.Employee;
import com.gabsdev.findaseat.model.entity.ReservationPeriod;
import com.gabsdev.findaseat.model.entity.Reservation;
import com.gabsdev.findaseat.model.entity.Seat;
import com.gabsdev.findaseat.model.enums.Type;
import com.gabsdev.findaseat.repository.EmployeeRepository;
import com.gabsdev.findaseat.repository.ReservationRepository;
import com.gabsdev.findaseat.repository.SeatRepository;
import com.gabsdev.findaseat.service.ReservationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ReservationResponse createReservation(ReservationRequest reservation, LocalTime start, LocalTime end) {
        verifyEmployeeAbleToReserve(reservation);
        Type type = verifySeatType(reservation.seatId());
        ReservationPeriod reservationPeriod = defineDate(type, reservation.date(), start, end);
        verifyReservationDate(reservation, reservationPeriod);
        Reservation reservationToSave = getReservation(reservation, reservationPeriod);
        Reservation saved = repository.save(reservationToSave);
        return mapper.toReservationResponse(saved);

    }

    @Override
    public ReservationResponse CreateQuickReservation(QuickReservationRequest reservation, LocalTime startTime, LocalTime endTime) {
        verifyEmployeeAbleToReserve(reservation.employeId(), reservation.type());
        ReservationPeriod reservationPeriod = defineDate(reservation.type(), reservation.date(), startTime, endTime);
        
        List<Seat> seatList = seatRepository.findByType(reservation.type());
        List<Seat> seats = seatList.stream()
                .filter(seat ->
                        !repository.existsBySeat_IdAndReservationPeriod_reservationDay(
                                seat.getId(), reservationPeriod.getReservationDay())
                ).toList();

        Seat seat = seats.get(0);
        Employee employee = employeeRepository.findById(reservation.employeId()).get();

        Reservation quickReservation = Reservation.builder()
                .employees(employee)
                .reservationPeriod(reservationPeriod)
                .seat(seat)
                .build();

        Reservation saved = repository.save(quickReservation);
        return mapper.toReservationResponse(saved);
    }

    private ReservationPeriod defineDate(Type type, LocalDate date, LocalTime start, LocalTime end) {
        if (type.name().equalsIgnoreCase("desk")){
            start = LocalTime.parse("08:00");
            end = LocalTime.parse("18:00");
        }
        return new ReservationPeriod(date,start,end);
    }

    private Type verifySeatType(UUID uuid) {
        if (seatRepository.existsById(uuid)){
            return  seatRepository.findById(uuid).get().getType();
        }
        throw new SeatNotFoundException("Seat not found");

    }

    private void   verifyEmployeeAbleToReserve(ReservationRequest reservation) {
        Type type = seatRepository.findById(reservation.seatId()).get().getType();
        verifyEmployeeAbleToReserve(reservation.employeId(), type);
    }

    private void verifyEmployeeAbleToReserve(Long employeId , Type type) {
        if (repository.existsByEmployees_idAndActiveTrue(employeId)){
          List<Reservation> reservationList =  repository.findByEmployees_idAndActiveTrue(employeId);
            if (reservationList.stream().anyMatch(reservation1 -> reservation1.getSeat().getType() == type)) {
                throw new ReservationConflictException("Já possui uma reserva de "
                        + type.name() +
                        " Ativa, finalize ela para poder realizar uma nova reserva");
            }
        }
    }


    @Override
    public List<ReservationResponse> getReservation(UUID reservationId, String employeeName, LocalDate date) {
        List<ReservationResponse> responseList = new ArrayList<>();
        if (reservationId != null){
           responseList.add(findById(reservationId)) ;
           return responseList;
        }
        if (date== null){
            date = LocalDate.now();
        }
       List<Reservation> reservations =  repository.findByEmployee_EmployeeNameAndReservationPeriod_ReservationDay("%"+employeeName+"%", date);
        responseList = reservations.stream().map(mapper::toReservationResponse).toList();
        return responseList;
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

    @Override
    public List<ReservationResponse> getBySeatAndData(UUID seatId, LocalDate date) {
        if (date!= null){
            List<Reservation> bySeatIdAndDateReservationDay = repository.findBySeat_IdAndReservationPeriod_reservationDay(seatId, date);
            return bySeatIdAndDateReservationDay.stream().map(mapper::toReservationResponse).toList();
        }
       List<Reservation> reservation = repository.findBySeat_Id(seatId);
        return reservation.stream().map(mapper::toReservationResponse).toList();
    }

    @Override
    public List<ReservationResponse> getByDay(LocalDate localDate) {
        if (localDate == null){
            localDate = LocalDate.now();
        }
        List<Reservation> byDateReservationDay = repository.findByReservationPeriod_reservationDay(localDate);
        return  byDateReservationDay.stream().map(mapper::toReservationResponse).toList();
    }

    @Override
    public ReservationResponse close(UUID uuid) {
        Reservation reservation = repository.findById(uuid).get();
        reservation.getReservationPeriod().setEndTimeLocation(LocalTime.now());
        return mapper.toReservationResponse(repository.save(reservation));
    }

    private ReservationResponse findById(UUID reservationId) {
        Reservation reservation = repository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException("Reserva não localizada"));
        return mapper.toReservationResponse(reservation);
    }

    private void verifyReservationDate(ReservationRequest reservation, ReservationPeriod reservationPeriod) {
        if(repository.existsBySeat_IdAndReservationPeriod_reservationDay(reservation.seatId(),
                reservation.date())){
            List<Reservation> reservationList = repository.findBySeat_IdAndReservationPeriod_reservationDay(reservation.seatId(),
                    reservation.date());
            reservationList.forEach(r -> verifyDate(r, reservation, reservationPeriod));
        }
    }

    private void verifyDate(Reservation r, ReservationRequest reservation, ReservationPeriod reservationPeriod) {
        if (!
                reservationPeriod.getStartTimeLocation().isAfter(r.getReservationPeriod().getEndTimeLocation()) ||
                reservationPeriod.getEndTimeLocation().isBefore(r.getReservationPeriod().getStartTimeLocation())
        ){
            throw new ConflictReservationException("Há um conflito de horário entre as reservas");
        }
    }

    private Reservation getReservation(ReservationRequest reservation, ReservationPeriod reservationPeriod) {
        Reservation reservationToSave = new Reservation();
        verifySeat(reservation);
        reservationToSave.setSeat(seatRepository.findById(reservation.seatId()).get());
        verifyEmployee(reservation);
        reservationToSave.setEmployees(employeeRepository.getReferenceById(reservation.employeId()));
        reservationToSave.setReservationPeriod(reservationPeriod);
        return reservationToSave;
    }

    private void verifyEmployee(ReservationRequest reservation) {
        if (!employeeRepository.existsById(reservation.employeId())){
            throw new EmployeeNotFoundException("Funcionario não localizado!");
        }
    }

    private void verifySeat(ReservationRequest reservation) {
        if (!seatRepository.existsById(reservation.seatId())){
            throw  new SeatNotFoundException("Não foi localizado um seat de ID: " + reservation.seatId());
        }
    }
}