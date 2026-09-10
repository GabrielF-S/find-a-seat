package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.dto.response.WaitlistResponse;
import com.gabsdev.findaseat.exception.EmployeeNotFoundException;
import com.gabsdev.findaseat.exception.WaitlistNotFoundException;
import com.gabsdev.findaseat.model.entity.*;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import com.gabsdev.findaseat.model.enums.Type;
import com.gabsdev.findaseat.repository.EmployeeRepository;
import com.gabsdev.findaseat.repository.ReservationRepository;
import com.gabsdev.findaseat.repository.SeatRepository;
import com.gabsdev.findaseat.repository.WaitlistRepository;
import com.gabsdev.findaseat.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
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
class WaitlistServiceImplTest {

    @Mock
    private WaitlistRepository waitlistRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private  ReservationService reservationService;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ReservationRepository reservationRepository;


    @InjectMocks @Spy
    private WaitlistServiceImpl waitlistService;


    private List<Waitlist> reservationTodayList;

    @BeforeEach
    void setUp(){
        reservationTodayList = List.of(Waitlist.builder()
                        .id(1L)
                        .reservationDay(LocalDate.now())
                        .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                        .reservationStatus(ReservationStatus.PENDING)
                        .duration(Duration.ofHours(2L))
                .build(),
                Waitlist.builder()
                        .id(3L)
                        .reservationDay(LocalDate.now())
                        .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusMinutes(18L))
                        .reservationStatus(ReservationStatus.PENDING)
                        .duration(Duration.ofHours(2L))
                        .build(),
                Waitlist.builder()
                        .id(2L)
                        .reservationDay(LocalDate.now())
                        .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                        .reservationStatus(ReservationStatus.PENDING)
                        .duration(Duration.ofHours(2L))
                        .build()
        );
    }


    @Test void shouldNotChangeStatusWhenWaitlistIsWithinToleranceTime() {
        // cenário
        Waitlist waitlist = Waitlist.builder()
                .id(1L)
                .reservationDay(LocalDate.now())
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .reservationStatus(ReservationStatus.PENDING)
                .duration(Duration.ofHours(2))
                .build();
        when(waitlistRepository.findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(ReservationStatus.PENDING, LocalDate.now()))
                .thenReturn(List.of(waitlist));
        // ação
        waitlistService.verifyStatusWaitlist();
        // verificação
        assertEquals( ReservationStatus.PENDING, waitlist.getReservationStatus());
        verify(waitlistRepository, never()).save(any(Waitlist.class));
    }

    @Test
    void shouldChangeStatusToNotConfirmedWhenWaitlistExceedsToleranceTime() {
        // cenário
        Waitlist waitlist = Waitlist.builder()
                .id(1L)
                .reservationDay(LocalDate.now())
                .createdAt(LocalDateTime.now().minusMinutes(20))
                .reservationStatus(ReservationStatus.PENDING)
                .duration(Duration.ofHours(2))
                .build();

        when(waitlistRepository
                .findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(
                        ReservationStatus.PENDING,
                        LocalDate.now()))
                .thenReturn(List.of(waitlist));

        // ação
        waitlistService.verifyStatusWaitlist();

        // verificação
        assertEquals(
                ReservationStatus.NOT_CONFIRMED,
                waitlist.getReservationStatus()
        );

        verify(waitlistRepository).save(waitlist);
    }

    @Test
    void shouldUpdateWaitlistStatus() {
        // cenário
         Long waitlistId = 1L;
         Waitlist waitlist = Waitlist.builder()
                 .id(waitlistId)
                 .employeeId(10L)
                 .reservationDay(LocalDate.now())
                 .duration(Duration.ofHours(2))
                 .reservationStatus(ReservationStatus.PENDING)
                 .build();
         Employee employee = Employee.builder()
                 .id(10L)
                 .employeeName("Gabriel")
                 .build();
         when(waitlistRepository.findById(waitlistId))
                 .thenReturn(Optional.of(waitlist));
         when(waitlistRepository.save(waitlist)).thenReturn(waitlist);
         when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee));
         // ação
        WaitlistResponse response = waitlistService.updateStatus( waitlistId, ReservationStatus.CONFIRMED );
        // verificação
        assertEquals(waitlistId, response.id()); assertEquals("Gabriel", response.employeeName());
        assertEquals(LocalDate.now(), response.reservationDay());
        assertEquals(Duration.ofHours(2), response.duration());
        assertEquals( ReservationStatus.CONFIRMED, response.reservationStatus() );
        verify(waitlistRepository).findById(waitlistId); verify(waitlistRepository).save(waitlist);
        verify(employeeRepository).findById(10L);
    }

    @Test
    void shouldThrowExceptionWhenWaitlistDoesNotExist() {
        // cenário
        Long waitlistId = 1L;
        when(waitlistRepository.findById(waitlistId))
                .thenReturn(Optional.empty());
        // ação + verificação
        assertThrows( WaitlistNotFoundException.class,
                () -> waitlistService.updateStatus( waitlistId, ReservationStatus.CONFIRMED ) );
        verify(waitlistRepository).findById(waitlistId); verify(waitlistRepository, never()).save(any());
    }
    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {
        // cenário
        Long waitlistId = 1L;
        Waitlist waitlist = Waitlist.builder()
                .id(waitlistId) .employeeId(10L)
                .reservationDay(LocalDate.now())
                .duration(Duration.ofHours(2))
                .reservationStatus(ReservationStatus.PENDING)
                .build(); when(waitlistRepository.findById(waitlistId))
                .thenReturn(Optional.of(waitlist));
                when(waitlistRepository.save(waitlist))
                        .thenReturn(waitlist);
                when(employeeRepository.findById(10L))
                        .thenReturn(Optional.empty());
                // ação + verificação
        assertThrows( EmployeeNotFoundException.class,
                () -> waitlistService.updateStatus( waitlistId, ReservationStatus.CONFIRMED )
        );
        verify(waitlistRepository).save(waitlist);
        verify(employeeRepository).findById(10L);
    }
    @Test
    void shouldReturnConfirmedWaitlist() {
        // cenário
        List<Waitlist> waitlists = List.of(
                Waitlist.builder()
                        .id(1L)
                        .reservationStatus(ReservationStatus.CONFIRMED)
                        .reservationDay(LocalDate.now())
                        .build(),
                Waitlist.builder()
                        .id(2L)
                        .reservationStatus(ReservationStatus.CONFIRMED)
                        .reservationDay(LocalDate.now())
                        .build()
        );

        when(waitlistRepository
                .findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(
                        ReservationStatus.CONFIRMED,
                        LocalDate.now()))
                .thenReturn(waitlists);

        // ação
        List<Waitlist> result = waitlistService.getWaitlist();

        // verificação
        assertEquals(waitlists, result);

        verify(waitlistRepository)
                .findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(
                        ReservationStatus.CONFIRMED,
                        LocalDate.now()
                );
    }



    @Test
    void shouldReturnEmptyListWhenThereAreNoConfirmedWaitlists() {
        // cenário
        when(waitlistRepository
                .findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(
                        ReservationStatus.CONFIRMED,
                        LocalDate.now()))
                .thenReturn(List.of());

        // ação
        List<Waitlist> result = waitlistService.getWaitlist();

        // verificação
        assertTrue(result.isEmpty());

        verify(waitlistRepository)
                .findByReservationStatusAndReservationDayOrderByUpdatedAtAsc(
                        ReservationStatus.CONFIRMED,
                        LocalDate.now()
                );
    }

    @Test
    void shouldCreateReservationWhenSeatIsAvailable() {
        // cenário
        Waitlist waitlist = Waitlist.builder()
                .id(1L)
                .employeeId(10L)
                .businessUuid(UUID.randomUUID())
                .reservationDay(LocalDate.now())
                .duration(Duration.ofHours(2))
                .reservationStatus(ReservationStatus.PENDING)
                .build();

        Seat seat = Seat.builder()
                .id(UUID.randomUUID())
                .type(Type.TABLE)
                .build();

        ReservationResponse reservationResponse =
                new ReservationResponse(
                        UUID.randomUUID(),
                        "seat",
                        "Joao",
                        ReservationPeriod
                                .builder()
                        .reservationDay(LocalDate.now())
                        .startTimeLocation(LocalTime.now()
                                .truncatedTo(ChronoUnit.MINUTES))
                        .endTimeLocation(LocalTime.now()
                                .truncatedTo(ChronoUnit.MINUTES)
                                .plusMinutes(30L))
                                .build(),
                        true,
                        ReservationStatus.PENDING
                );

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .build();

        when(seatRepository
                .findByTypeAndFloor_Business_Uuid(
                        Type.TABLE,
                        waitlist.getBusinessUuid()))
                .thenReturn(List.of(seat));

        when(reservationRepository
                .existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(
                        any(),
                        any()))
                .thenReturn(false);

        when(reservationService.createReservation(
                any(ReservationRequest.class),
                any(LocalTime.class),
                any(LocalTime.class)))
                .thenReturn(reservationResponse);

        when(reservationRepository.findById(any()))
                .thenReturn(Optional.of(reservation));

        // ação
        waitlistService.verifySeatsAvaliable(waitlist);

        // verificação

        assertEquals(
                ReservationStatus.FINISHED,
                waitlist.getReservationStatus()
        );

        verify(reservationService).createReservation(
                any(ReservationRequest.class),
                any(LocalTime.class),
                any(LocalTime.class)
        );

        verify(waitlistRepository).save(waitlist);

        verify(reservationRepository).findById(any());

        verify(reservationService)
                .sendNotificationReservation(reservation);
    }



    @Test
    void shouldNotCreateReservationWhenThereAreNoAvailableSeats() {
        // cenário
        Waitlist waitlist = Waitlist.builder()
                .id(1L)
                .employeeId(10L)
                .businessUuid(UUID.randomUUID())
                .reservationDay(LocalDate.now())
                .duration(Duration.ofHours(2))
                .reservationStatus(ReservationStatus.PENDING)
                .build();

        Seat seat = Seat.builder()
                .id(UUID.randomUUID())
                .type(Type.TABLE)
                .build();

        when(seatRepository
                .findByTypeAndFloor_Business_Uuid(
                        Type.TABLE,
                        waitlist.getBusinessUuid()))
                .thenReturn(List.of(seat));

        when(reservationRepository
                .existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(
                        any(),
                        any()))
                .thenReturn(true);

        // ação
        waitlistService.verifySeatsAvaliable(waitlist);

        // verificação
        verify(reservationService, never())
                .createReservation(
                        any(),
                        any(),
                        any()
                );

        verify(waitlistRepository, never())
                .save(any());

        verify(reservationService, never())
                .sendNotificationReservation(any());
    }


}