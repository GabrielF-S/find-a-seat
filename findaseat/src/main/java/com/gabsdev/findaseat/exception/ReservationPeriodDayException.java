package com.gabsdev.findaseat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class ReservationPeriodDayException extends FindASetException {
    private final String detail;

    public ReservationPeriodDayException(String message) {
        this.detail = message;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pb.setTitle("Period Exception");
        pb.setType(URI.create("errors/conflict"));
        pb.setProperty("timestamp", Instant.now());
        return pb;
    }
}
