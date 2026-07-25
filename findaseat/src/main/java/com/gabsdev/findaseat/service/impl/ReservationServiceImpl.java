package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.QuickReservationRequest;
import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.exception.*;
import com.gabsdev.findaseat.mapper.ReservationMapper;
import com.gabsdev.findaseat.model.entity.*;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import com.gabsdev.findaseat.model.enums.Type;
import com.gabsdev.findaseat.repository.EmployeeRepository;
import com.gabsdev.findaseat.repository.ReservationRepository;
import com.gabsdev.findaseat.repository.SeatRepository;
import com.gabsdev.findaseat.repository.UserRepository;
import com.gabsdev.findaseat.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {


    private final ReservationRepository repository;
    private final SeatRepository seatRepository;
    private final EmployeeRepository employeeRepository;
    private final ReservationMapper mapper;
    private final UserRepository userRepository;

    public ReservationServiceImpl(ReservationRepository repository, SeatRepository seatRepository, EmployeeRepository employeeRepository, ReservationMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.seatRepository = seatRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }


    @Override
    public ReservationResponse createReservation(ReservationRequest reservation, LocalTime start, LocalTime end) {
        verifyEmployeeAbleToReserve(reservation);
        Type type = verifySeatType(reservation.seatId());
        ReservationPeriod reservationPeriod = defineDate(type, reservation.date(), start, end);
        verifyReservationDate(reservation, reservationPeriod);
        Reservation reservationToSave = getReservation(reservation, reservationPeriod);
        reservationToSave.setActive(true);
        reservationToSave.setReservationStatus(ReservationStatus.PENDING);
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

        Seat seat = seats.getFirst();
        Employee employee = employeeRepository.findById(reservation.employeId()).get();
        Reservation quickReservation = Reservation.builder()
                .employees(employee)
                .reservationPeriod(reservationPeriod)
                .seat(seat)
                .active(true)
                .reservationStatus(ReservationStatus.PENDING)
                .build();
        Reservation saved = repository.save(quickReservation);
        return mapper.toReservationResponse(saved);
    }

    @Override
    public ReservationResponse updateReservation(UUID uuid, ReservationStatus reservationStatus) {
        Reservation reservation = repository.findById(uuid).orElseThrow(() -> new ReservationNotFoundException("Reservation Not found"));
        if (reservation.getReservationStatus().name().equalsIgnoreCase(reservationStatus.name())){
            throw new ConflictReservationException("Reserva já esta " + reservationStatus.name());
        }
        reservation.setReservationStatus(reservationStatus);
        reservation.setActive(true);
        return mapper.toReservationResponse(repository.save(reservation));
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
    public ReservationResponse close(UUID uuid, boolean isCancelled) {
        Reservation reservation = repository.findById(uuid).get();
        reservation.getReservationPeriod().setEndTimeLocation(LocalTime.now());
        reservation.setActive(false);
        if (isCancelled){
            reservation.setReservationStatus(ReservationStatus.CANCELLED);
        }
        return mapper.toReservationResponse(repository.save(reservation));
    }

    @Override
    public List<Reservation> verifyUnconfirmedReservations(){
        return repository.findByActiveTrueAndReservationStatus(ReservationStatus.PENDING);
    }

    @Override
    public void sendNotificationReservation(Reservation reservation) {
        User byEmployeeId = userRepository
                .findByEmployees_Id(reservation.getEmployees().getId())
                .orElse(new User(
                        "email@teste.com", "12345", List.of("USER"), reservation.getEmployees()
                ));
        if (reservation.getReservationPeriod().getStartTimeLocation().isBefore(LocalTime.now())){
            sendCancellEmail(byEmployeeId.getEmail(), reservation.getEmployees().getEmployeeName(), reservation.getSeat().getNick(), reservation.getReservationPeriod().getReservationDay());
        }else {
        sendEmailToConfirm(byEmployeeId.getEmail(), reservation.getEmployees().getEmployeeName(), reservation.getSeat().getNick(), reservation.getReservationPeriod().getReservationDay());
        }
    }

    private void sendCancellEmail(String email, String employeeName, String nick, LocalDate reservationDay) {
         /*
        Metodo ficticio para chamar serviço de envio de e-mail para informar que a reserva foi cancelada
         */
        log.info("""
                To: %s
                
                Hello, %s
                This e-mail has send because you are  not confirmed, then your reservation has cancelled
                Seat: %s
                Date: %s
                """.formatted(email, employeeName, nick, reservationDay));

    }

    @Override
    public void verifyInactivedReservations() {
        List<Reservation> reservationList = repository.findByActiveTrueAndReservationStatus(ReservationStatus.PENDING);
        reservationList.forEach(r ->{
            if (r.getReservationPeriod().getStartTimeLocation().isBefore(LocalTime.now())){
                r.setActive(false);
                r.setReservationStatus(ReservationStatus.NOT_CONFIRMED);
                repository.save(r);
            }
        });
    }

    @Override
    public ReservationResponse confirmReservation(UUID uuid) {
        Reservation reservation = repository.findById(uuid).orElseThrow(
                () -> new ReservationNotFoundException("Reservation Not Found"));
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        Reservation saved = repository.save(reservation);
        return mapper.toReservationResponse(saved);
    }

    private void sendEmailToConfirm(String email, String name, String seatName, LocalDate date) {
        /*
        Metodo ficticio para chamar serviço de envio de e-mail para  usuario que ainda não confirmou a reserva
         */
        log.info("""
                To: %s
                
                Hello, %s
                This e-mail has send because you are a reservation still not confirmed, please, check the reservation for not have issues
                Seat: %s
                Date: %s
                """.formatted(email, name, seatName, date));

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