package com.gabsdev.findaseat.service;

import com.gabsdev.findaseat.dto.response.WaitlistResponse;
import com.gabsdev.findaseat.model.entity.Waitlist;
import com.gabsdev.findaseat.model.enums.ReservationStatus;

import java.util.List;

public interface WaitlistService {
    WaitlistResponse updateStatus(Long waitlistId,ReservationStatus status);

    void verifyStatusWaitlist();

    void verifySeatsAvaliable(Waitlist waitlist);

    List<Waitlist> getWaitlist();
}
