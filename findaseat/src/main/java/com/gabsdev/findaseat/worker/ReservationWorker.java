package com.gabsdev.findaseat.worker;

import com.gabsdev.findaseat.model.entity.Reservation;

public interface ReservationWorker {

    void notificarReservas();

    void verificarReservas();

    void sendNotification(Reservation reservation);
}
