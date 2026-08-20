package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.QuickReservationRequest;
import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.exception.*;
import com.gabsdev.findaseat.mapper.ReservationMapper;
import com.gabsdev.findaseat.model.entity.*;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import com.gabsdev.findaseat.model.enums.Type;
import com.gabsdev.findaseat.repository.*;
import com.gabsdev.findaseat.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    private final WaitlistRepository waitlistRepository;

    public ReservationServiceImpl(ReservationRepository repository, SeatRepository seatRepository, EmployeeRepository employeeRepository, ReservationMapper mapper, UserRepository userRepository, WaitlistRepository waitlistRepository) {
        this.repository = repository;
        this.seatRepository = seatRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.waitlistRepository = waitlistRepository;
    }


    @Transactional
    @Override
    public ReservationResponse createReservation(ReservationRequest reservation, LocalTime start, LocalTime end) {
        Type type = verifySeatType(reservation.seatId());
        verifyEmployee(reservation);
        UUID employeeBusinessUuid = employeeRepository.findBusinessUuid(reservation.employeId());
        UUID seatBusinessUuid = seatRepository.getBusinessUuid(reservation.seatId());
        if (!employeeBusinessUuid.equals(seatBusinessUuid)) {
            throw new SeatNotFoundException("Seat not found");
        }
        if (reservation.date().isBefore(LocalDate.now())) {
            throw new ReservationPeriodDayException("Data de reserva não pode ser anterior a data atual " + LocalDate.now());
        }
        verifyEmployeeAbleToReserve(reservation.employeId(), type);
        ReservationPeriod reservationPeriod = defineDate(type, reservation.date(), start, end);
        verifyReservationDate(reservation, reservationPeriod);
        Reservation reservationToSave = getReservation(reservation, reservationPeriod);
        reservationToSave.setActive(true);
        reservationToSave.setReservationStatus(ReservationStatus.PENDING);
        Reservation saved = repository.save(reservationToSave);
        return mapper.toReservationResponse(saved);
    }

    @Override
    public ReservationResponse createQuickReservation(QuickReservationRequest reservation,
                                                      LocalTime startTime, LocalTime endTime) {
        UUID employeeBusinessUuid = employeeRepository.findBusinessUuid(reservation.employeId());
        List<Seat> seatList = seatRepository
                .findByTypeAndFloor_Business_Uuid(reservation.type(), employeeBusinessUuid);
        List<Seat> seats = seatList.stream()
                .filter(seat ->
                        !repository.existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(
                                seat.getId(), reservation.date())
                ).toList();
        if (seats.isEmpty() && reservation.type().name().equalsIgnoreCase("table")) {
            Waitlist waitlist = new Waitlist(reservation.employeId(), reservation.date(), Duration.between(startTime, endTime), ReservationStatus.PENDING, employeeBusinessUuid);
            Waitlist saved = waitlistRepository.save(waitlist);
            throw new WaitlistException("Reserva adicionada a fila de espera ID: " + saved.getId(), saved.getId());

        }
        UUID id = seats.getFirst().getId();
        ReservationResponse response = createReservation(new ReservationRequest(reservation.date(), id, reservation.employeId()), startTime, endTime);
        return response;
    }

    @Override
    public ReservationResponse updateReservation(UUID uuid, ReservationStatus reservationStatus) {
        Reservation reservation = repository.findById(uuid).orElseThrow(() -> new ReservationNotFoundException("Reservation Not Found"));
        if (!reservation.isActive()){
            throw  new ReservationDesativatedException("Não é possivel confirmar reserva com status: " + reservation.getReservationStatus().name());
        }
        if (reservation.getReservationStatus().name().equalsIgnoreCase(reservationStatus.name())) {
            throw new ConflictReservationException("Reserva já esta " + reservationStatus.name());
        }
        if(reservationStatus.name().equalsIgnoreCase("cancelled") ||reservationStatus.name().equalsIgnoreCase("finished")){
            reservation.setActive(false);
        }
        reservation.setReservationStatus(reservationStatus);
        return mapper.toReservationResponse(repository.save(reservation));
    }

    @Override
    public ReservationPeriod defineDate(Type type, LocalDate date, LocalTime start, LocalTime end) {
        if (type.name().equalsIgnoreCase("desk")) {
            start = LocalTime.parse("08:00");
            end = LocalTime.parse("18:00");
        }
        if (date == null) {
            date = LocalDate.now();
        }
        if (start == null || end == null) {
            throw new ReservationConflictException("Horario de inicio/fim deve ser informado");
        }
        return new ReservationPeriod(date, start, end);
    }

    @Override
    public Type verifySeatType(UUID uuid) {
        if (!seatRepository.existsById(uuid)) {
            throw new SeatNotFoundException("Seat not found");
        }
        return seatRepository.findTypeById(uuid);

    }


    @Override
    public void verifyEmployeeAbleToReserve(Long employeId, Type type) {
        if (repository.existsByEmployees_idAndActiveTrue(employeId)) {
            List<Reservation> reservationList = repository.findByEmployees_idAndActiveTrue(employeId);
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
        if (reservationId != null) {
            responseList.add(findById(reservationId));
            return responseList;
        }
        if (date == null) {
            date = LocalDate.now();
        }
        List<Reservation> reservations = repository.findByEmployee_EmployeeNameAndReservationPeriod_ReservationDay("%" + employeeName + "%", date);
        responseList = reservations.stream().map(mapper::toReservationResponse).toList();
        return responseList;
    }


    @Override
    public void deleteById(UUID uuid) {
        if (!repository.existsById(uuid)) {
            throw new ReservationNotFoundException("Reservation not found");
        }
        repository.deleteById(uuid);
    }

    @Override
    public List<ReservationResponse> getBySeatAndData(UUID seatId, LocalDate date) {
        if (date != null) {
            List<Reservation> bySeatIdAndDateReservationDay = repository.findBySeat_IdAndReservationPeriod_reservationDay(seatId, date);
            return bySeatIdAndDateReservationDay.stream().map(mapper::toReservationResponse).toList();
        }
        List<Reservation> reservation = repository.findBySeat_Id(seatId);
        return reservation.stream().map(mapper::toReservationResponse).toList();
    }

    @Override
    public List<ReservationResponse> getByDay(LocalDate localDate) {
        if (localDate == null) {
            localDate = LocalDate.now();
        }
        List<Reservation> byDateReservationDay = repository.findByReservationPeriod_reservationDay(localDate);
        return byDateReservationDay.stream().map(mapper::toReservationResponse).toList();
    }

    @Override
    public ReservationResponse close(UUID uuid) {
        Reservation reservation = repository.findById(uuid).orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        if (!reservation.getReservationStatus().name().equalsIgnoreCase("confirmed")){
            throw  new ReservationConflictException("Não é possivel fechar reserverva para status " + reservation.getReservationStatus().name());
        }
        reservation.getReservationPeriod().setEndTimeLocation(LocalTime.now());
        reservation.setActive(false);
        reservation.setReservationStatus(ReservationStatus.FINISHED);
        return mapper.toReservationResponse(repository.save(reservation));
    }

    @Override
    public List<Reservation> verifyUnconfirmedReservations() {
        return repository.findByActiveTrueAndReservationStatus(ReservationStatus.PENDING);
    }

    @Override
    public void sendNotificationReservation(Reservation reservation) {
        User byEmployeeId = userRepository
                .findByEmployees_Id(reservation.getEmployees().getId())
                .orElse(new User(
                        "email@teste.com", "12345", List.of("USER"), reservation.getEmployees()
                ));
        if (reservation.getReservationPeriod().getStartTimeLocation().isBefore(LocalTime.now())) {
            sendCancellEmail(byEmployeeId.getEmail(), reservation.getEmployees().getEmployeeName(),
                    reservation.getSeat().getNick(), reservation.getReservationPeriod().getReservationDay());
        } else {
            sendEmailToConfirm(byEmployeeId.getEmail(), reservation.getEmployees().getEmployeeName(),
                    reservation.getSeat().getNick(), reservation.getReservationPeriod().getReservationDay());
        }
    }

    @Override
    public void sendCancellEmail(String email, String employeeName, String nick, LocalDate reservationDay) {
         /*
        Metodo ficticio para chamar serviço de envio de e-mail para informar que a reserva foi cancelada
         */
        log.info("""
                To: %s
                
                Hello, %s
                This e-mail has send because you are not confirmed, then your reservation has cancelled
                Seat: %s
                Date: %s
                """.formatted(email, employeeName, nick, reservationDay));

    }

    @Override
    public void verifyInactivedReservations() {
        List<Reservation> reservationList = repository.findByActiveTrueAndReservationStatus(ReservationStatus.PENDING);

        reservationList.forEach(r -> {
            if (r.getReservationPeriod().getStartTimeLocation().isBefore(LocalTime.now().truncatedTo(ChronoUnit.MINUTES))) {
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
        if (!reservation.isActive()) {
            throw new ReservationDesativatedException("Reserva não pode ser confirmada pois passou do prazo para confirmação");
        }
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        Reservation saved = repository.save(reservation);
        return mapper.toReservationResponse(saved);
    }

    @Override
    public void sendEmailToConfirm(String email, String name, String seatName, LocalDate date) {
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

    @Override
    public ReservationResponse findById(UUID reservationId) {
        Reservation reservation = repository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException("Reserva não localizada"));
        return mapper.toReservationResponse(reservation);
    }

    @Override
    public void verifyReservationDate(ReservationRequest reservation, ReservationPeriod reservationPeriod) {
        if (repository.
                existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrueAndReservationPeriod_StartTimeLocationLessThanEqualAndReservationPeriod_EndTimeLocationGreaterThanEqual(
                        reservation.seatId(), reservationPeriod.getReservationDay(), reservationPeriod.getEndTimeLocation(),
                        reservationPeriod.getStartTimeLocation())) {
            throw new ConflictReservationException("Há um conflito de horário entre as reservas");
        }

    }

    @Override
    public Reservation getReservation(ReservationRequest reservation, ReservationPeriod reservationPeriod) {
        Reservation reservationToSave = new Reservation();
        reservationToSave.setSeat(seatRepository.findById(reservation.seatId()).get());
        reservationToSave.setEmployees(employeeRepository.findById(reservation.employeId()).get());
        reservationToSave.setReservationPeriod(reservationPeriod);
        return reservationToSave;
    }

    @Override
    public void verifyEmployee(ReservationRequest reservation) {
        if (!employeeRepository.existsById(reservation.employeId())) {
            throw new EmployeeNotFoundException("Funcionario não localizado!");
        }
    }

}