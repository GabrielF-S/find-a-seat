package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.exception.FindASetException;
import com.gabsdev.findaseat.mapper.impl.ReservationMapperImpl;
import com.gabsdev.findaseat.model.entity.*;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import com.gabsdev.findaseat.model.enums.Status;
import com.gabsdev.findaseat.model.enums.Type;
import com.gabsdev.findaseat.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {


    @Mock
    private ReservationRepository repository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WaitlistRepository waitlistRepository;

    @Mock
    private ReservationMapperImpl mapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private ReservationRequest request;

    private Reservation reservation;

    private List<Reservation> reservationList;

    private Seat deaskSeat;

    private Employee employee;


    @BeforeEach
    void setUp() {

        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        deaskSeat = new Seat(UUID.randomUUID(), "Desk 01", "Desk_01", "desk_01", Status.AVALIABLE,
                false, LocalDateTime.now(), LocalDateTime.now(), new Floor(), Type.DESK, 1);


        employee = Employee.builder().id(1L).employeeName("Jose").department("TI").document("1231455").build();
        reservation = new Reservation(
                UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790"),
                new ReservationPeriod(LocalDate.now(),
                        LocalTime.now(),
                        LocalTime.now().plusMinutes(15L)),
                new Seat(),
                new Employee(),
                true,
                ReservationStatus.PENDING
        );
        reservationList = List.of(new Reservation(
                        UUID.randomUUID(),
                        new ReservationPeriod(
                                LocalDate.now(),
                                LocalTime.now(),
                                LocalTime.now().plusMinutes(15)
                        ),
                        new Seat(),
                        new Employee(),
                        true,
                        ReservationStatus.PENDING
                ),

                new Reservation(
                        UUID.randomUUID(),
                        new ReservationPeriod(
                                LocalDate.now(),
                                LocalTime.now().plusMinutes(30),
                                LocalTime.now().plusMinutes(45)
                        ),
                        new Seat(),
                        new Employee(),
                        true,
                        ReservationStatus.PENDING
                )
        );

        reservationList.getFirst().getSeat().setType(Type.DESK);


    }

    @Test
    void shoudTrhowsExceptionWhenSetBusinessUuidAndEmployeeBusinessUuidAreEqulsButSeatNotFound() {
        //cenario

        //acao
        FindASetException exception = assertThrows(FindASetException.class,
                () -> reservationService.createReservation(request, LocalTime.now(),
                        LocalTime.now().plusMinutes(15L)));

        //verificação
        assertEquals("Seat not found", exception.toProblemDetail().getDetail());
    }

    @Test
    void shoudTrhowsExceptionWhenEmployeeBusinessUuidAndSeatBusinessUuidIsNotEqual() {
        // cenario
        UUID employeeBusinessUuid = UUID.randomUUID();
        UUID seatBusinessUuid = UUID.randomUUID();
        when(seatRepository.getBusinessUuid(any())).thenReturn(seatBusinessUuid);
        when(employeeRepository.findBusinessUuid(any())).thenReturn(employeeBusinessUuid);
        when(seatRepository.existsById(any())).thenReturn(true);
        when(employeeRepository.existsById(any())).thenReturn(true);

        //acao
        FindASetException exception = assertThrows(FindASetException.class,
                () -> reservationService.createReservation(request, LocalTime.now(),
                        LocalTime.now().plusMinutes(15L)));

        //verificação
        assertEquals("Seat not found", exception.toProblemDetail().getDetail());


    }

    @Test
    void shoudTrhowsExceptionWhenReservationPeriodBeforeThatToday() {
        // cenario
        UUID employeeBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        UUID seatBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        when(seatRepository.existsById(any())).thenReturn(true);
        when(seatRepository.getBusinessUuid(any())).thenReturn(seatBusinessUuid);
        when(employeeRepository.findBusinessUuid(any())).thenReturn(employeeBusinessUuid);
        when(employeeRepository.existsById(any())).thenReturn(true);
        request = new ReservationRequest(LocalDate.parse("2026-08-08"), UUID.randomUUID(), 1L);
        //acao
        FindASetException exception = assertThrows(FindASetException.class,
                () -> reservationService.createReservation(request, LocalTime.now(),
                        LocalTime.now().plusMinutes(15L)));

        //verificação
        assertEquals("Data de reserva não pode ser anterior a data atual " + LocalDate.now(),
                exception.toProblemDetail().getDetail());

    }

    @Test
    void shoulThrowsExceptionWhenEmployeeHaveThanMoreOneReservationForTheSameType() {
        UUID employeeBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        UUID seatBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        when(seatRepository.existsById(any())).thenReturn(true);
        when(seatRepository.getBusinessUuid(any())).thenReturn(seatBusinessUuid);
        when(employeeRepository.findBusinessUuid(any())).thenReturn(employeeBusinessUuid);
        when(employeeRepository.existsById(any()))
                .thenReturn(true);
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        when(repository.existsByEmployees_idAndActiveTrue(any())).thenReturn(true);
        when(repository.findByEmployees_idAndActiveTrue(any())).thenReturn(reservationList);
        when(seatRepository.findTypeById(any())).thenReturn(Type.DESK);

        //acao
        FindASetException exception = assertThrows(FindASetException.class,
                () -> reservationService.createReservation(request, LocalTime.now(),
                        LocalTime.now().plusMinutes(15L)));

        //verificação
        assertEquals("Já possui uma reserva de "
                        + Type.DESK.name() +
                        " Ativa, finalize ela para poder realizar uma nova reserva",
                exception.toProblemDetail().getDetail());
    }

    @Test
    void shoulDefineStartTimeReservationAt8AMWhenTypeLikeDesk() {
        //cenario
        UUID employeeBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        UUID seatBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        ReservationPeriod period = ReservationPeriod.builder().reservationDay(LocalDate.parse("2026-08-13"))
                .startTimeLocation(LocalTime.parse("15:00"))
                .endTimeLocation(LocalTime.parse("16:00"))
                .build();

        //acao
        ReservationPeriod reservationPeriod = reservationService.defineDate(deaskSeat.getType(), period.getReservationDay(),
                period.getStartTimeLocation(), period.getEndTimeLocation());
        //verificação
        assertEquals(LocalTime.parse("08:00"), reservationPeriod.getStartTimeLocation());

    }

    @Test
    void shoulDefineTimeEndReservationAt18PMWhenTypeLikeDesk() {
        //cenario
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        ReservationPeriod period = ReservationPeriod.builder().reservationDay(LocalDate.parse("2026-08-13"))
                .startTimeLocation(LocalTime.parse("15:00"))
                .endTimeLocation(LocalTime.parse("16:00"))
                .build();

        //acao
        ReservationPeriod reservationPeriod = reservationService.defineDate(deaskSeat.getType(), period.getReservationDay(),
                period.getStartTimeLocation(), period.getEndTimeLocation());
        //verificação
        assertEquals(LocalTime.parse("18:00"), reservationPeriod.getEndTimeLocation());

    }

    @Test
    void shouldDefineReservationDayForTodayWhenReservationDayIsNull() {
        //cenario
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        ReservationPeriod period = ReservationPeriod.builder()
                .startTimeLocation(LocalTime.parse("15:00"))
                .endTimeLocation(LocalTime.parse("16:00"))
                .build();

        //acao
        ReservationPeriod reservationPeriod = reservationService.defineDate(deaskSeat.getType(), period.getReservationDay(),
                period.getStartTimeLocation(), period.getEndTimeLocation());
        //verificação
        assertEquals(LocalDate.now(), reservationPeriod.getReservationDay());

    }

    @Test
    void shouldNotDefineReservationDayForTodayWhenReservationDayIsNull() {
        //cenario
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        ReservationPeriod period = ReservationPeriod.builder().reservationDay(LocalDate.parse("2026-08-13"))
                .startTimeLocation(LocalTime.parse("15:00"))
                .endTimeLocation(LocalTime.parse("16:00"))
                .build();

        //acao
        ReservationPeriod reservationPeriod = reservationService.defineDate(deaskSeat.getType(), period.getReservationDay(),
                period.getStartTimeLocation(), period.getEndTimeLocation());
        //verificação
        assertEquals(LocalDate.parse("2026-08-13"), reservationPeriod.getReservationDay());
    }

    @Test
    void shouldTrhowsExceptionWhenStartTimeIsNullAndTypeNotDesk() {
        //cenario
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        ReservationPeriod period = ReservationPeriod.builder().reservationDay(LocalDate.parse("2026-08-13"))
                .endTimeLocation(LocalTime.parse("16:00"))
                .build();

        //acao
        FindASetException findASetException = assertThrows(FindASetException.class, () -> reservationService.defineDate(Type.TABLE, period.getReservationDay(),
                period.getStartTimeLocation(), period.getEndTimeLocation()));
        //verificação
        assertEquals("Horario de inicio/fim deve ser informado", findASetException.toProblemDetail().getDetail());
    }
    @Test
    void shouldTrhowsExceptionWhenEndTimeIsNullAndTypeNotDesk() {
        //cenario
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        ReservationPeriod period = ReservationPeriod.builder().reservationDay(LocalDate.parse("2026-08-13"))
                .startTimeLocation(LocalTime.parse("16:00"))
                .build();

        //acao
        FindASetException findASetException = assertThrows(FindASetException.class, () -> reservationService.defineDate(Type.TABLE, period.getReservationDay(),
                period.getStartTimeLocation(), period.getEndTimeLocation()));
        //verificação
        assertEquals("Horario de inicio/fim deve ser informado", findASetException.toProblemDetail().getDetail());
    }

    @Test
    void shoulTrowsExceptionWhenAlreadyHaveReservationInPeriod(){
        when(repository
                        .existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrueAndReservationPeriod_StartTimeLocationLessThanEqualAndReservationPeriod_EndTimeLocationGreaterThanEqual(any(), any(), any(), any()))
                .thenReturn(true);
        ReservationPeriod period = ReservationPeriod.builder()
                .reservationDay(LocalDate.now())
                .startTimeLocation(LocalTime.now())
                .endTimeLocation(LocalTime.now()).build();

        FindASetException exception = assertThrows(FindASetException.class, () -> reservationService.verifyReservationDate(request, period));

        assertEquals("Há um conflito de horário entre as reservas", exception.toProblemDetail().getDetail());
    }


    @Test
    void shouldVerifyIfSaveMetodoIsInvokedWhenNotHaveExceptions(){
        //cenario
        UUID employeeBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        UUID seatBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        when(seatRepository.existsById(any())).thenReturn(true);
        when(seatRepository.getBusinessUuid(any())).thenReturn(seatBusinessUuid);
        when(employeeRepository.findBusinessUuid(any())).thenReturn(employeeBusinessUuid);
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        when(repository.existsByEmployees_idAndActiveTrue(any())).thenReturn(false);
        when(seatRepository.findTypeById(any())).thenReturn(Type.DESK);
        when(repository
                .existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrueAndReservationPeriod_StartTimeLocationLessThanEqualAndReservationPeriod_EndTimeLocationGreaterThanEqual(any(),any(),any(),any()))
                .thenReturn(false);
        when(seatRepository.findById(any())).thenReturn(Optional.of(deaskSeat));
        when(employeeRepository.existsById(any())).thenReturn(true);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(repository.save((any()))).thenReturn(reservation);


        //ação

        reservationService.createReservation(request,LocalTime.now(), LocalTime.now().plusMinutes(15));

        //verifição
        verify(repository, times(1)).save(any());

    }

    @Test
    void shoulThrosExceptionWhenEmployeeNotFound(){
        //cenario
        when(employeeRepository.existsById(any())).thenReturn(false);
        when(seatRepository.existsById(any())).thenReturn(true);

        //ação

        FindASetException exception = assertThrows(FindASetException.class, ()-> reservationService.createReservation(request, LocalTime.now(), LocalTime.now().plusMinutes(60)));
        //verificação
        assertEquals("Funcionario não localizado!", exception.toProblemDetail().getDetail());
    }




}