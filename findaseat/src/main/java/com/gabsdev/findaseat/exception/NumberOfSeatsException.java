package com.gabsdev.findaseat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class NumberOfSeatsException extends FindASetException {
    private  String detail;
    public NumberOfSeatsException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pb.setTitle("Seat Exception");
        pb.setType(URI.create("errors/"));
        pb.setProperty("timestamp", Instant.now());
        return  pb;
    }
}
