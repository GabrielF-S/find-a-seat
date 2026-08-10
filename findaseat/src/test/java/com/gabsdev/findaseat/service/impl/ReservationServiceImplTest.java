package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.mapper.ReservationMapper;
import com.gabsdev.findaseat.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceImplTest {


    @Mock
    private  ReservationRepository repository;
    @Mock
    private  SeatRepository seatRepository;
    @Mock
    private  EmployeeRepository employeeRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  WaitlistRepository waitlistRepository;

    @InjectMocks
    private  ReservationMapper mapper;
    @Mock
    private  ReservationMapper mapperSpy;



    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ReservationRepository.class);
        seatRepository  = Mockito.mock(SeatRepository.class);
        employeeRepository  = Mockito.mock(EmployeeRepository.class);
        userRepository  = Mockito.mock(UserRepository.class);
        waitlistRepository = Mockito.mock(WaitlistRepository.class);
        mapperSpy  = Mockito.spy(mapper);

        ReservationRequest request = new ReservationRequest(LocalDate.now(), UUID.randomUUID(), 1L );

    }

    @Test
    void shouldCreateAReservation() {
        //cenati

        //ação

        //verificação
    }

    @Test
    void createQuickReservation() {
    }

    @Test
    void updateReservation() {
    }

    @Test
    void getReservation() {
    }

    @Test
    void testUpdateReservation() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void getBySeatAndData() {
    }

    @Test
    void getByDay() {
    }

    @Test
    void close() {
    }

    @Test
    void verifyUnconfirmedReservations() {
    }

    @Test
    void sendNotificationReservation() {
    }

    @Test
    void verifyInactivedReservations() {
    }

    @Test
    void confirmReservation() {
    }
}