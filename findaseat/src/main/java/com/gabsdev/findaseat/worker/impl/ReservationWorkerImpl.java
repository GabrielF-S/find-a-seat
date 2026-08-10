package com.gabsdev.findaseat.worker.impl;

import com.gabsdev.findaseat.model.entity.Reservation;
import com.gabsdev.findaseat.service.ReservationService;
import com.gabsdev.findaseat.worker.ReservationWorker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
@Component
public class ReservationWorkerImpl implements ReservationWorker {


    private final ReservationService service;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public ReservationWorkerImpl( ReservationService service) {

        this.service = service;
    }

    @Scheduled(fixedRate = 1800000)
    @Override
    public void notificarReservas() {

        List<Reservation> reservationList = service.verifyUnconfirmedReservations();

        log.info("Executando Notificar Reservas");
        for (Reservation reservation : reservationList) {
            executorService.submit(() -> sendNotification(reservation));
        }
    }

    @Scheduled(fixedRate =2000000)
    @Override
    public void verificarReservas() {
        log.info("Veriricar reservas");
        service.verifyInactivedReservations();
    }

    @Override
    public void sendNotification(Reservation reservation) {
        service.sendNotificationReservation(reservation);
    }
}
