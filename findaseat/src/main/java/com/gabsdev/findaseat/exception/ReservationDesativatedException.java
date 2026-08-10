package com.gabsdev.findaseat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.LocalDate;

public class ReservationDesativatedException extends FindASetException {

    private final String detail;

    public ReservationDesativatedException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pb.setTitle("Confirmation Error");
        pb.setType(URI.create("error/unvaliable"));
        pb.setProperty("timestamp", LocalDate.now());
        return pb;
    }
}
