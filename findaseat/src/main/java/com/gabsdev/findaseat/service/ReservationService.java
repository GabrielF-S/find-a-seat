package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.request.QuickReservationRequest;
import com.gabsdev.findaseat.dto.request.ReservationRequest;
import com.gabsdev.findaseat.dto.response.ReservationResponse;
import com.gabsdev.findaseat.model.entity.Reservation;
import com.gabsdev.findaseat.model.entity.ReservationPeriod;
import com.gabsdev.findaseat.model.enums.ReservationStatus;
import com.gabsdev.findaseat.model.enums.Type;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ReservationService {

    ReservationResponse createReservation(ReservationRequest reservation, LocalTime start, LocalTime end);

    ReservationPeriod defineDate(Type type, LocalDate date, LocalTime start, LocalTime end);

    Type verifySeatType(UUID uuid);



    void verifyEmployeeAbleToReserve(Long employeId, Type type);

    List<ReservationResponse> getReservation(UUID reservationId, String employeeName, LocalDate date);

    ReservationResponse updateReservation(Reservation reservation);

    void deleteById(UUID uuid);

    List<ReservationResponse> getBySeatAndData(UUID seatId, LocalDate date);

    List<ReservationResponse> getByDay(LocalDate localDate);

    ReservationResponse close(UUID uuid, boolean isCancelled);

    ReservationResponse CreateQuickReservation(QuickReservationRequest reservation, LocalTime startTime, LocalTime endTime);

    ReservationResponse updateReservation(UUID uuid, ReservationStatus reservationStatus);

    List<Reservation> verifyUnconfirmedReservations();

    void sendNotificationReservation(Reservation reservation);

    void sendCancellEmail(String email, String employeeName, String nick, LocalDate reservationDay);

    void verifyInactivedReservations();

    ReservationResponse confirmReservation(UUID uuid);

    void sendEmailToConfirm(String email, String name, String seatName, LocalDate date);

    ReservationResponse findById(UUID reservationId);

    void verifyReservationDate(ReservationRequest reservation, ReservationPeriod reservationPeriod);

    Reservation getReservation(ReservationRequest reservation, ReservationPeriod reservationPeriod);

    void verifyEmployee(ReservationRequest reservation);

}
