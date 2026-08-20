package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.QuickReservationRequest;
import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.exception.*;
import com.gabsdev.findaseat.mapper.impl.ReservationMapperImpl;
import com.gabsdev.findaseat.model.entity.*;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import com.gabsdev.findaseat.model.enums.Status;
import com.gabsdev.findaseat.model.enums.Type;
import com.gabsdev.findaseat.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    @Spy
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
                        LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
                        LocalTime.now().plusMinutes(15L).truncatedTo(ChronoUnit.MINUTES)),
                new Seat(),
                new Employee(),
                true,
                ReservationStatus.PENDING
        );
        reservationList = List.of(new Reservation(
                        UUID.randomUUID(),
                        new ReservationPeriod(
                                LocalDate.now(),
                                LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
                                LocalTime.now().plusMinutes(15).truncatedTo(ChronoUnit.MINUTES)
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
                                LocalTime.now().plusMinutes(30).truncatedTo(ChronoUnit.MINUTES),
                                LocalTime.now().plusMinutes(45).truncatedTo(ChronoUnit.MINUTES)
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
    void shouldThrowsExceptionWhenSetBusinessUuidAndEmployeeBusinessUuidAreEqulsButSeatNotFound() {
        //cenario

        //acao
        FindASetException exception = assertThrows(FindASetException.class,
                () -> reservationService.createReservation(request, LocalTime.now(),
                        LocalTime.now().plusMinutes(15L)));

        //verificação
        assertEquals("Seat not found", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsExceptionWhenEmployeeBusinessUuidAndSeatBusinessUuidIsNotEqual() {
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
    void shouldThrowsExceptionWhenReservationPeriodBeforeThatToday() {
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
    void shouldThrowsExceptionWhenEmployeeHaveThanMoreOneReservationForTheSameType() {
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
    void shouldDefineStartTimeReservationAt8AMWhenTypeLikeDesk() {
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
    void shouldDefineTimeEndReservationAt18PMWhenTypeLikeDesk() {
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
    void shouldThrowsExceptionWhenStartTimeIsNullAndTypeNotDesk() {
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
    void shouldThrowsExceptionWhenEndTimeIsNullAndTypeNotDesk() {
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
    void shouldTrowsExceptionWhenAlreadyHaveReservationInPeriod() {
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
    void shouldVerifyIfSaveMethodeIsInvokedWhenNotHaveExceptions() {
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
                .existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrueAndReservationPeriod_StartTimeLocationLessThanEqualAndReservationPeriod_EndTimeLocationGreaterThanEqual(any(), any(), any(), any()))
                .thenReturn(false);
        when(seatRepository.findById(any())).thenReturn(Optional.of(deaskSeat));
        when(employeeRepository.existsById(any())).thenReturn(true);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(repository.save((any()))).thenReturn(reservation);
        //ação
        reservationService.createReservation(request, LocalTime.now(), LocalTime.now().plusMinutes(15));
        //verifição
        verify(repository, times(1)).save(any());
    }
    @Test
    void shouldThrosExceptionWhenEmployeeNotFound() {
        //cenario
        when(employeeRepository.existsById(any())).thenReturn(false);
        when(seatRepository.existsById(any())).thenReturn(true);

        //ação

        FindASetException exception = assertThrows(FindASetException.class, () -> reservationService.createReservation(request, LocalTime.now(), LocalTime.now().plusMinutes(60)));
        //verificação
        assertEquals("Funcionario não localizado!", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowExceptionWHenNoHaveTableAvaliable() {
        //cenario
        when(employeeRepository.findBusinessUuid(any())).thenReturn(UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790"));
        Waitlist waitlist = Waitlist.builder().id(1L).build();
        Employee employee1 = Employee.builder().id(1L).business(Business.builder().uuid(UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790")).build()).build();
        QuickReservationRequest quickReservationRequest = new QuickReservationRequest(LocalDate.now(), Type.TABLE, 1L);
        when(seatRepository.findByTypeAndFloor_Business_Uuid(Type.TABLE, employee1.getBusiness().getUuid())).thenReturn(List.of(deaskSeat, Seat.builder().build()));
        when(repository.existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(any(), any())).thenReturn(true);
        when(waitlistRepository.save(any())).thenReturn(waitlist);
        //ação
        FindASetException exception = assertThrows(WaitlistException.class, () ->
                reservationService.createQuickReservation(quickReservationRequest, LocalTime.now(), LocalTime.now()));
        //verificação
        assertEquals("Reserva adicionada a fila de espera ID: 1", exception.toProblemDetail().getDetail());

    }
    @Test
    void shouldVerifyIfCreateReservationIsCalled() {
        //cenario
        when(employeeRepository.findBusinessUuid(any())).thenReturn(UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790"));
        Employee employee1 = Employee.builder().id(1L).business(Business.builder().uuid(UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790")).build()).build();
        QuickReservationRequest quickReservationRequest = new QuickReservationRequest(LocalDate.now(), Type.TABLE, 1L);
        when(seatRepository.findByTypeAndFloor_Business_Uuid(Type.TABLE, employee1.getBusiness().getUuid())).thenReturn(List.of(deaskSeat, Seat.builder().build()));
        when(repository.existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(any(), any())).thenReturn(false);
        when(seatRepository.existsById(any())).thenReturn(true);
        when(seatRepository.getBusinessUuid(any())).thenReturn(employee1.getBusiness().getUuid());
        request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L);
        when(repository.existsByEmployees_idAndActiveTrue(any())).thenReturn(false);
        when(seatRepository.findTypeById(any())).thenReturn(Type.DESK);
        when(repository
                .existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrueAndReservationPeriod_StartTimeLocationLessThanEqualAndReservationPeriod_EndTimeLocationGreaterThanEqual(any(), any(), any(), any()))
                .thenReturn(false);
        when(seatRepository.findById(any())).thenReturn(Optional.of(deaskSeat));
        when(employeeRepository.existsById(any())).thenReturn(true);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));


        UUID seatBusinessUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");

        reservationService.createQuickReservation(quickReservationRequest, LocalTime.now(), LocalTime.now());
        //verificação
        verify(repository, times(1)).save(any());

    }
    @Test
    void shouldThrowsExceptionWhenReservationNotFound() {
        //cenario
        UUID reservationUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        when(repository.findById(reservationUuid)).thenThrow(new ReservationNotFoundException("Reservation Not Found"));
        //ação
        FindASetException exception = assertThrows(ReservationNotFoundException.class,
                () -> reservationService.updateReservation(reservationUuid, ReservationStatus.CONFIRMED));

        //verificação
        assertEquals("Reservation Not Found", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsEsceptionWHenReservationActiveIsFalse() {
        //cenario
        UUID reservationUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        Reservation reservation1 = Reservation.builder().id(reservationUuid).active(false)
                .reservationStatus(ReservationStatus.NOT_CONFIRMED).build();
        when(repository.findById(reservationUuid)).thenReturn(Optional.of(reservation1));

        //ação
        FindASetException exception = assertThrows(ReservationDesativatedException.class,
                () -> reservationService.updateReservation(reservationUuid, ReservationStatus.CONFIRMED));
        //verificação
        assertEquals("Não é possivel confirmar reserva com status: NOT_CONFIRMED", exception.toProblemDetail().getDetail());

    }
    @Test
    void shouldThrowsExceptionWhenReservationStatusAlreadySetting() {
        //cenario
        UUID reservationUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        Reservation reservation1 = Reservation.builder().id(reservationUuid).active(true)
                .reservationStatus(ReservationStatus.CONFIRMED).build();
        when(repository.findById(reservationUuid)).thenReturn(Optional.of(reservation1));

        //ação
        FindASetException exception = assertThrows(ConflictReservationException.class,
                () -> reservationService.updateReservation(reservationUuid, ReservationStatus.CONFIRMED));
        //verificação
        assertEquals("Reserva já esta CONFIRMED", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldVerifyIfMapperCalled() {
        //cenario
        UUID reservationUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        Reservation reservation1 = Reservation.builder().id(reservationUuid)
                .reservationStatus(ReservationStatus.PENDING)
                .active(true).build();
        when(repository.findById(reservationUuid)).thenReturn(Optional.of(reservation1));
        when(repository.save(any())).thenReturn(reservation1);
        //ação
        reservationService.updateReservation(reservationUuid, ReservationStatus.CONFIRMED);
        //verificação
        verify(mapper, atLeastOnce()).toReservationResponse(reservation1);

    }
    @Test
    void shouldReturnOnlyOneReservationWhenPassedReservationUuid() {
        //cenario
        UUID reservationUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        when(repository.findById(reservationUuid)).thenReturn(Optional.of(reservation));
        //ação
        List<ReservationResponse> reservation1 = reservationService.getReservation(reservationUuid, "flink", LocalDate.now());
        //verificação
        assertEquals(1, reservation1.size());
    }
    @Test
    void shouldDefineActiveFalseWhenStatusCancelled() {
        //cenario
        UUID reservationUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        Reservation reservation1 = Reservation.builder().id(reservationUuid)
                .reservationStatus(ReservationStatus.PENDING)
                .employees(Employee.builder().employeeName("flink").build())
                .active(true)
                .seat(Seat.builder().build()).build();
        when(repository.findById(reservationUuid)).thenReturn(Optional.of(reservation1));
        when(repository.save(any())).thenReturn(reservation1);
        when(mapper.toReservationResponse(any())).thenCallRealMethod();

        //ação
        ReservationResponse reservationResponse = reservationService.updateReservation(reservationUuid, ReservationStatus.CANCELLED);
        //verificação
        assertFalse(reservationResponse.activate());

    }
    @Test
    void shouldDefineActiveFalseWhenStatusFinished() {
        //cenario
        UUID reservationUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        Reservation reservation1 = Reservation.builder().id(reservationUuid)
                .reservationStatus(ReservationStatus.PENDING)
                .employees(Employee.builder().employeeName("flink").build())
                .active(true)
                .seat(Seat.builder().build()).build();
        when(repository.findById(reservationUuid)).thenReturn(Optional.of(reservation1));
        when(repository.save(any())).thenReturn(reservation1);
        when(mapper.toReservationResponse(any())).thenCallRealMethod();

        //ação
        ReservationResponse reservationResponse = reservationService.updateReservation(reservationUuid, ReservationStatus.FINISHED);
        //verificação
        assertFalse(reservationResponse.activate());

    }
    @Test
    void shouldSettingDateForTodayWhenDateisNull() {
        //cenario
        ArgumentCaptor<LocalDate> argumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        UUID reservationUuid = UUID.fromString("088098f8-27e8-4d2c-a93c-b480c5d20790");
        when(repository.findByEmployee_EmployeeNameAndReservationPeriod_ReservationDay(any(), any())).thenReturn(List.of(reservation));
        when(mapper.toReservationResponse(any())).thenCallRealMethod();
        List<ReservationResponse> reservation1 = reservationService.getReservation(eq(null), eq("flink"), argumentCaptor.capture());
        //verificação
        verify(repository).findByEmployee_EmployeeNameAndReservationPeriod_ReservationDay(any(), argumentCaptor.capture());
        assertEquals(LocalDate.now(), argumentCaptor.getValue());
    }
    @Test
    void shouldMantainDateForTodayWhenDateisNotNull() {
        //cenario
        ArgumentCaptor<LocalDate> argumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        when(repository.findByEmployee_EmployeeNameAndReservationPeriod_ReservationDay(any(), any())).thenReturn(List.of(reservation));
        when(mapper.toReservationResponse(any())).thenCallRealMethod();
        //ação
        List<ReservationResponse> reservation1 = reservationService.getReservation(null, "flink", LocalDate.parse("2026-08-20"));
        //verificação
        verify(repository, atLeastOnce()).findByEmployee_EmployeeNameAndReservationPeriod_ReservationDay(any(), any());

    }
    @Test
    void shouldThrowsExceptionWhenReservationNotFoundWhenTryDelete() {
        //cenario
        UUID reservationUuid = UUID.randomUUID();
        when(repository.existsById(any())).thenReturn(false);
        //ação
        FindASetException exception = assertThrows(ReservationNotFoundException.class,
                () -> reservationService.deleteById(reservationUuid));

        //verificação
        assertEquals("Reservation not found", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldDeleteReservationWhenReservationExists() {
        //cenario
        UUID reservationUuid = UUID.randomUUID();
        when(repository.existsById(reservationUuid)).thenReturn(true);
        //ação
        reservationService.deleteById(reservationUuid);
        //verificação
        verify(repository, atLeastOnce()).deleteById(reservationUuid);

    }
    @Test
    void shouldReturnListWith2ItensWhenDateIsNotNull() {
        UUID seatUuid = UUID.randomUUID();
        var tomorrow = LocalDate.now().plusDays(1L);
        var reservationsWithIdAndPeriodDay = List.of(

                Reservation.builder().id(UUID.randomUUID())
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(LocalDate.now().plusDays(1L))
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .active(true)
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .employees(employee)
                        .build(),
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .employees(Employee.builder()
                                .id(2L)
                                .employeeName("Jonson")
                                .business(Business.builder().build()).build())
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .active(true)
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(LocalDate.now().plusDays(1L))
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .build());

        when(repository.findBySeat_IdAndReservationPeriod_reservationDay(any(), any())).thenReturn(reservationsWithIdAndPeriodDay);
        when(mapper.toReservationResponse(any())).thenCallRealMethod();
        //ação
        List<ReservationResponse> bySeatAndData = reservationService.getBySeatAndData(seatUuid, tomorrow);

        assertEquals(2, bySeatAndData.size());
    }
    @Test
    void shouldReturnListWith3ItensWhenDateIsNotNull() {
        UUID seatUuid = UUID.randomUUID();
        var reservationsWithId = List.of(

                Reservation.builder().id(UUID.randomUUID())
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(LocalDate.now())
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .active(true)
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .employees(employee)
                        .build(),
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .employees(Employee.builder()
                                .id(2L)
                                .employeeName("Jonson")
                                .business(Business.builder().build()).build())
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .active(true)
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(LocalDate.now())
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .build(),
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .employees(Employee.builder()
                                .id(2L)
                                .employeeName("Arnaldo")
                                .business(Business.builder().build()).build())
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .active(true)
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(LocalDate.now())
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .build()

        );

        when(repository.findBySeat_Id(any())).thenReturn(reservationsWithId);
        when(mapper.toReservationResponse(any())).thenCallRealMethod();
        //ação
        List<ReservationResponse> bySeatAndData = reservationService.getBySeatAndData(seatUuid, null);

        //verificação
        assertEquals(3, bySeatAndData.size());
    }
    @Test
    void shouldDefineDateForTodayIfIsNull() {
        //cenario
        UUID seatUuid = UUID.randomUUID();
        ArgumentCaptor<LocalDate> argumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        var reservationsWithId = List.of(

                Reservation.builder().id(UUID.randomUUID())
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(LocalDate.now())
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .active(true)
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .employees(employee)
                        .build(),
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .employees(Employee.builder()
                                .id(2L)
                                .employeeName("Jonson")
                                .business(Business.builder().build()).build())
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .active(true)
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(LocalDate.now())
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .build(),
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .employees(Employee.builder()
                                .id(2L)
                                .employeeName("Arnaldo")
                                .business(Business.builder().build()).build())
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .active(true)
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(LocalDate.now())
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .build()

        );
        when(repository.findByReservationPeriod_reservationDay(any())).thenReturn(reservationsWithId);
        when(mapper.toReservationResponse(any())).thenCallRealMethod();


        //ação
        List<ReservationResponse> byDay = reservationService.getByDay(argumentCaptor.capture());
        //verificação
        verify(repository).findByReservationPeriod_reservationDay(argumentCaptor.capture());
        assertEquals(LocalDate.now(), argumentCaptor.getValue());
    }
    @Test
    void shouldIgnoreIfDate() {
        //cenario
        var tomorrow = LocalDate.now().plusDays(1L);
        UUID seatUuid = UUID.randomUUID();
        var reservationsWithId = List.of(

                Reservation.builder().id(UUID.randomUUID())
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(tomorrow)
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .active(true)
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .employees(employee)
                        .build(),
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .employees(Employee.builder()
                                .id(2L)
                                .employeeName("Jonson")
                                .business(Business.builder().build()).build())
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .active(true)
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(tomorrow)
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .build(),
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .employees(Employee.builder()
                                .id(2L)
                                .employeeName("Arnaldo")
                                .business(Business.builder().build()).build())
                        .seat(Seat.builder()
                                .id(seatUuid)
                                .build())
                        .active(true)
                        .reservationPeriod(ReservationPeriod.builder()
                                .reservationDay(tomorrow)
                                .startTimeLocation(LocalTime.parse("08:00"))
                                .endTimeLocation(LocalTime.parse("18:00"))
                                .build())
                        .build()

        );
        when(repository.findByReservationPeriod_reservationDay(any())).thenReturn(reservationsWithId);
        when(mapper.toReservationResponse(any())).thenCallRealMethod();
        //ação
        List<ReservationResponse> byDay = reservationService.getByDay(tomorrow);
        //verificação
        assertEquals(tomorrow, byDay.getFirst().reservationPeriod().getReservationDay());
    }
    @Test
    void shouldThrowsExceptionWhenReservationIdNotFound() {
        when(repository.findById(any())).thenThrow(new ReservationNotFoundException("Reservation Not Found"));
        UUID uuid = UUID.randomUUID();
        //ação
        FindASetException exception = assertThrows(ReservationNotFoundException.class,
                () -> reservationService.close(uuid));
        //verificação
        assertEquals("Reservation Not Found", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsExceptionWHenReservationStatusIsNotConfimed() {
        //cenario
        UUID uuid = UUID.randomUUID();
        var reservation1 = Reservation.builder()
                .id(uuid)
                .seat(Seat.builder()
                        .seatName("Flink_Link")
                        .build())
                .employees(Employee.builder()
                        .employeeName("Tinki")
                        .build())
                .reservationPeriod(ReservationPeriod.builder()
                        .reservationDay(LocalDate.now())
                        .startTimeLocation(LocalTime.parse("08:00"))
                        .endTimeLocation(LocalTime.parse("18:00"))
                        .build())
                .active(true)
                .reservationStatus(ReservationStatus.PENDING)
                .build();
        when(repository.findById(uuid)).thenReturn(Optional.of(reservation1));
        //ação
        FindASetException exception = assertThrows(ReservationConflictException.class,
                () -> reservationService.close(uuid));
        //verificação
        assertEquals("Não é possivel fechar reserverva para status PENDING", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldSetAttributesForReservation() {
        //cenario
        UUID uuid = UUID.randomUUID();
        var reservation1 = Reservation.builder()
                .id(uuid)
                .seat(Seat.builder()
                        .seatName("Flink_Link")
                        .build())
                .employees(Employee.builder()
                        .employeeName("Tinki")
                        .build())
                .reservationPeriod(ReservationPeriod.builder()
                        .reservationDay(LocalDate.now())
                        .startTimeLocation(LocalTime.parse("08:00"))
                        .endTimeLocation(LocalTime.parse("18:00"))
                        .build())
                .active(true)
                .reservationStatus(ReservationStatus.CONFIRMED)
                .build();
        when(repository.findById(any())).thenReturn(Optional.of(reservation1));
        when(mapper.toReservationResponse(any())).thenCallRealMethod();
        when(repository.save(any())).thenReturn(reservation1);

        //ação
        ReservationResponse closed = reservationService.close(uuid);

        //verificação
        assertEquals(ReservationStatus.FINISHED, closed.reservationStatus());
        assertFalse(closed.activate());


    }
    @Test
    void shouldReturnListWith1ItemWhenGetByStatus() {
        //cenario
        when(repository.findByActiveTrueAndReservationStatus(any())).thenReturn(List.of(reservation));
        //ação
        List<Reservation> reservationList1 = reservationService.verifyUnconfirmedReservations();
        //verificação
        verify(repository, times(1)).findByActiveTrueAndReservationStatus(any());
    }
    @Test
    void shouldSendNotificationForCancellReservations() {
        //cenario
        UUID uuid = UUID.randomUUID();
        LocalTime start = LocalTime.now().minusMinutes(30);
        Reservation reservation1 = Reservation.builder()
                .id(uuid)
                .reservationStatus(ReservationStatus.PENDING)
                .active(true)
                .reservationPeriod(ReservationPeriod.builder()
                        .reservationDay(LocalDate.now())
                        .startTimeLocation(start)
                        .endTimeLocation(LocalTime.parse("18:00"))
                        .build())
                .employees(employee)
                .seat(deaskSeat)
                .build();
        when(userRepository.findByEmployees_Id(employee.getId())).thenReturn(Optional.of(new User(
                "email@teste.com", "12345", List.of("USER"), reservation1.getEmployees()
        )));
        //ação
        reservationService.sendNotificationReservation(reservation1);
        //verificação
        verify(reservationService, atLeastOnce()).sendCancellEmail(any(), any(), any(), any());
    }
    @Test
    void shouldSendNotificationForConfirmReservations() {
        //cenario
        UUID uuid = UUID.randomUUID();
        LocalTime start = LocalTime.now().plusMinutes(30);
        Reservation reservation1 = Reservation.builder()
                .id(uuid)
                .reservationStatus(ReservationStatus.PENDING)
                .active(true)
                .reservationPeriod(ReservationPeriod.builder()
                        .reservationDay(LocalDate.now())
                        .startTimeLocation(start)
                        .endTimeLocation(LocalTime.parse("18:00"))
                        .build())
                .employees(employee)
                .seat(deaskSeat)
                .build();
        when(userRepository.findByEmployees_Id(employee.getId())).thenReturn(Optional.of(new User(
                "email@teste.com", "12345", List.of("USER"), reservation1.getEmployees()
        )));
        //ação
        reservationService.sendNotificationReservation(reservation1);
        //verificação
        verify(reservationService, atLeastOnce()).sendEmailToConfirm(any(), any(), any(), any());
    }
    @Test
    void shouldSkipSettingActivefalseWhenAllReservationStillValid(){
        //cenario
        when(repository.findByActiveTrueAndReservationStatus(ReservationStatus.PENDING)).thenReturn(reservationList);
        //ação
        reservationService.verifyInactivedReservations();
        //verificação
        verify(repository, never()).save(any());
    }
    @Test
    void shouldSettingActivefalseWhenAllReservationStillValid(){
        //cenario
        var reservations = List.of(new Reservation(
                        UUID.randomUUID(),
                        new ReservationPeriod(
                                LocalDate.now(),
                                LocalTime.now().minusMinutes(10).truncatedTo(ChronoUnit.MINUTES),
                                LocalTime.now().plusMinutes(15).truncatedTo(ChronoUnit.MINUTES)
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
                                LocalTime.now().minusMinutes(20).truncatedTo(ChronoUnit.MINUTES),
                                LocalTime.now().plusMinutes(45).truncatedTo(ChronoUnit.MINUTES)
                        ),
                        new Seat(),
                        new Employee(),
                        true,
                        ReservationStatus.PENDING
                ));
        when(repository.findByActiveTrueAndReservationStatus(ReservationStatus.PENDING)).thenReturn(reservations);
        //ação
        reservationService.verifyInactivedReservations();
        //verificação
        verify(repository, times(2)).save(any());
    }

    @Test
    void shouldthrowsExceptioinWhenreservationNotFoun(){
        //cenario

        UUID uuid = UUID.randomUUID();
        //ação
        FindASetException exception = assertThrows(ReservationNotFoundException.class,
                () -> reservationService.confirmReservation(uuid));
        //verificação
        assertEquals("Reservation Not Found", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsExceptionWhenReservationCanNotConfirmed(){
        UUID uuid = UUID.randomUUID();
        Reservation reservation1 = Reservation.builder()
                .id(uuid)
                .seat(deaskSeat)
                .employees(employee)
                .reservationPeriod(ReservationPeriod.builder()
                        .reservationDay(LocalDate.now())
                        .startTimeLocation(LocalTime.parse("09:00"))
                        .endTimeLocation(LocalTime.parse("18:00"))
                        .build())
                .active(false)
                .reservationStatus(ReservationStatus.PENDING)
                .build();
        when(repository.findById(uuid)).thenReturn(Optional.of(reservation1));

        //ação
        FindASetException exception = assertThrows(ReservationDesativatedException.class,
                ()-> reservationService.confirmReservation(uuid));
        //verificação
        assertEquals("Reserva não pode ser confirmada pois passou do prazo para confirmação", exception.toProblemDetail().getDetail());

    }
    @Test
    void shouldConfirmReservation(){
        //cenario
        UUID uuid = UUID.randomUUID();
        Reservation reservation1 = Reservation.builder()
                .id(uuid)
                .seat(deaskSeat)
                .employees(employee)
                .reservationPeriod(ReservationPeriod.builder()
                        .reservationDay(LocalDate.now())
                        .startTimeLocation(LocalTime.now().truncatedTo(ChronoUnit.MINUTES))
                        .endTimeLocation(LocalTime.parse("18:00"))
                        .build())
                .active(true)
                .reservationStatus(ReservationStatus.PENDING)
                .build();
        when(repository.findById(uuid)).thenReturn(Optional.of(reservation1));
        when(mapper.toReservationResponse(any())).thenCallRealMethod();
        when(repository.save(any())).thenReturn(reservation1);
        //verificação
        ReservationResponse reservationResponse = reservationService.confirmReservation(uuid);

        //verificação
        assertEquals(ReservationStatus.CONFIRMED,reservationResponse.reservationStatus());
    }
}