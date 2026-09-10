package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.exception.*;
import com.gabsdev.findaseat.mapper.SeatMapper;
import com.gabsdev.findaseat.mapper.impl.SeatMapperImpl;
import com.gabsdev.findaseat.model.entity.Business;
import com.gabsdev.findaseat.model.entity.Floor;
import com.gabsdev.findaseat.model.entity.Seat;
import com.gabsdev.findaseat.model.enums.BusinessType;
import com.gabsdev.findaseat.model.enums.Status;
import com.gabsdev.findaseat.model.enums.Type;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.repository.FloorsRepository;
import com.gabsdev.findaseat.repository.ReservationRepository;
import com.gabsdev.findaseat.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

    @Mock
    private SeatRepository repository;
    @Mock
    private FloorsRepository floorsRepository;
    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private SeatMapperImpl mapper;
    @Spy
    @InjectMocks
    private SeatServiceImpl seatService;



    @Test
    void shouldThrowsExceptionWhenSeatTypeDeskAndNumberOfSeatNull(){
        //cenario
        SeatRequest requestDesk = new SeatRequest(Type.DESK, 1L, "Desk", false, null, UUID.randomUUID());

        //ação
        FindASetException exception = assertThrows(NumberOfSeatsException.class,
                ()-> seatService.createSeat(requestDesk));

        assertEquals("The number of seats most be 1 for a DESK", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsExceptionWhenSeatTypeDeskAndNumberOfSeatGreateThan1(){
        //cenario
        SeatRequest requestDesk = new SeatRequest(Type.DESK, 1L, "Desk", false, 3, UUID.randomUUID());

        //ação
        FindASetException exception = assertThrows(NumberOfSeatsException.class,
                ()-> seatService.createSeat(requestDesk));

        assertEquals("The number of seats most be 1 for a DESK", exception.toProblemDetail().getDetail());

    }
    @Test
    void shouldThrowsExceptionWhenSeatTypeSeatAndNumberOfSeatNull(){
        //cenario
        SeatRequest requestSeat = new SeatRequest(Type.SEAT, 1L, "Seat", false, null, UUID.randomUUID());

        //ação
        FindASetException exception = assertThrows(NumberOfSeatsException.class,
                ()-> seatService.createSeat(requestSeat));

        assertEquals("The number of seats most be 1 for a SEAT", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsExceptionWhenSeatTypeSeatAndNumberOfSeatGreateThan1(){
        //cenario
        SeatRequest requestSeat = new SeatRequest(Type.SEAT, 1L, "Seat", false, 3, UUID.randomUUID());

        //ação
        FindASetException exception = assertThrows(NumberOfSeatsException.class,
                ()-> seatService.createSeat(requestSeat));

        assertEquals("The number of seats most be 1 for a SEAT", exception.toProblemDetail().getDetail());

    }

    @Test
    void shouldThrowsExceptionWhenSeatTypeTableAndNumberOfSeatNull(){
        //cenario
        SeatRequest requestTable = new SeatRequest(Type.TABLE, 1L, "table", false, null, UUID.randomUUID());

        //ação
        FindASetException exception = assertThrows(NumberOfSeatsException.class,
                ()-> seatService.createSeat(requestTable));

        assertEquals("Is required define a number of seats bigger that 1 for a TABLE", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsExceptionWhenSeatTypeTableAndNumberOfSeatSmallerThan2(){
        //cenario
        SeatRequest requestTable = new SeatRequest(Type.TABLE, 1L, "Table", false, 1, UUID.randomUUID());

        //ação
        FindASetException exception = assertThrows(NumberOfSeatsException.class,
                ()-> seatService.createSeat(requestTable));

        assertEquals("Is required define a number of seats bigger that 1 for a TABLE", exception.toProblemDetail().getDetail());

    }
    @Test
    void shouldThrowsExceptionWhenSeatTypeRoomAndNumberOfSeatNull(){
        //cenario
        SeatRequest requestRoom = new SeatRequest(Type.ROOM, 1L, "Room", false, null, UUID.randomUUID());

        //ação
        FindASetException exception = assertThrows(NumberOfSeatsException.class,
                ()-> seatService.createSeat(requestRoom));

        assertEquals("Is required define a number of seats bigger that 1 for a ROOM", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsExceptionWhenSeatTypeRoomAndNumberOfSeatSmallerThan2(){
        //cenario
        SeatRequest requestRoom = new SeatRequest(Type.ROOM, 1L, "Room", false, 1, UUID.randomUUID());

        //ação
        FindASetException exception = assertThrows(NumberOfSeatsException.class,
                ()-> seatService.createSeat(requestRoom));

        assertEquals("Is required define a number of seats bigger that 1 for a ROOM", exception.toProblemDetail().getDetail());

    }
    @Test
    void shouldThrowsExceptionWhenFloorNotFound(){
        //cenario
        UUID floorId = UUID.randomUUID();
        SeatRequest requestDesk = new SeatRequest(Type.DESK, 1L, "Desk", false, 1, floorId);
        //ação
        FindASetException exception = assertThrows(FloorNoFoundException.class,
                () -> seatService.createSeat(requestDesk));

        assertEquals("Floor: " + floorId + ", Not found", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldThrowsExceptionWhenSeatNameAndFloorAlredyExists(){
        //cenario
        UUID floorId = UUID.randomUUID();
        SeatRequest requestDesk = new SeatRequest(Type.DESK, 1L, "Desk", false, 1, floorId);

        when(floorsRepository.findById(floorId)).thenReturn(Optional.of(
                Floor.builder()
                        .id(floorId)
                        .towerName("Vila 1")
                        .floorName("1 andar")
                        .business( Business.builder()
                                .uuid(UUID.randomUUID())
                                .businessName("Business X")
                                .businessType(BusinessType.BUSINESS)
                                .build())
                        .build()
        ));
        when(mapper.toSeat(any(), any())).thenCallRealMethod();
        when(repository.existsBySeatNameAndFloorId(any(), any())).thenReturn(true);
        //ação
        FindASetException exception = assertThrows(SeatAlredyExistException.class,
                () -> seatService.createSeat(requestDesk));
        //verificação
        assertEquals("Seat already exist", exception.toProblemDetail().getDetail());
    }
    @Test
    void shouldVerifySeatSaved(){
        //cenario
        UUID floorId = UUID.randomUUID();
        SeatRequest requestDesk = new SeatRequest(Type.DESK, 1L, "Desk", false, 1, floorId);

        when(floorsRepository.findById(floorId)).thenReturn(Optional.of(
                Floor.builder()
                        .id(floorId)
                        .towerName("Vila 1")
                        .floorName("1 andar")
                        .business( Business.builder()
                                .uuid(UUID.randomUUID())
                                .businessName("Business X")
                                .businessType(BusinessType.BUSINESS)
                                .build())
                        .build()
        ));
        when(mapper.toSeat(any(), any())).thenCallRealMethod();
        when(repository.existsBySeatNameAndFloorId(any(), any())).thenReturn(false);
        //ação
        seatService.createSeat(requestDesk);
        //verificação
        verify(repository, times(1)).save(any());

    }
    @Test
    void shouldThrowsExceptionWhenBusinessNotFound(){
        //cenario
        UUID businessUuid = UUID.randomUUID();
        UUID seatUuid = UUID.randomUUID();

        when(businessRepository.existsById(businessUuid)).thenReturn(false);

        //ação
        FindASetException exception = assertThrows(BusinessNotFoundException.class,
                () -> seatService.deleteByBusinessIuudAndSeatId(businessUuid,seatUuid));
        //verificação
        assertEquals( "Business with id: " + businessUuid + " Not found", exception.toProblemDetail().getDetail());


    }
    @Test
    void shouldThrowsExceptionWhenSeatNotFound(){
        //cenario
        UUID businessUuid = UUID.randomUUID();
        UUID seatUuid = UUID.randomUUID();

        when(businessRepository.existsById(businessUuid)).thenReturn(true);
        when(repository.existsById(seatUuid)).thenReturn(false);

        //ação
        FindASetException exception = assertThrows(SeatNotFoundException.class,
                () -> seatService.deleteByBusinessIuudAndSeatId(businessUuid,seatUuid));
        //verificação
        assertEquals( "Seat with id: " + seatUuid + " Not found", exception.toProblemDetail().getDetail());


    }
    @Test
    void shouldVerifyIfSeatHasDeleted(){
        //cenario
        UUID businessUuid = UUID.randomUUID();
        UUID seatUuid = UUID.randomUUID();

        when(businessRepository.existsById(businessUuid)).thenReturn(true);
        when(repository.existsById(seatUuid)).thenReturn(true);
        //ação
        seatService.deleteByBusinessIuudAndSeatId(businessUuid,seatUuid);
        //verificação
        verify(repository, times(1)).deleteById(seatUuid);

    }
    @Test
    void shouldVerifyIfDateIsDefinidForTodayWhenNull(){
        //cenario
        UUID businessUuid = UUID.randomUUID();
        UUID seatUuid = UUID.randomUUID();
        ArgumentCaptor<LocalDate> argumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        when(businessRepository.existsById(any())).thenReturn(true);
        when(repository.existsById(any())).thenReturn(true);
        when(reservationRepository.existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(eq(seatUuid), argumentCaptor.capture())).thenReturn(true);
        when(repository.findByIdAndFloor_BusinessUuid(any(), any()))
                .thenReturn(Seat.builder()
                        .id(seatUuid)
                        .build()
                );
        //ação
        Seat seatById = seatService.getSeatById(eq(seatUuid), eq(businessUuid), argumentCaptor.capture());
        //verificação
        verify(reservationRepository).existsBySeat_IdAndReservationPeriod_reservationDayAndActiveTrue(any(),argumentCaptor.capture());
        assertEquals(LocalDate.now(), argumentCaptor.getValue());


    }

}