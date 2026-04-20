package com.gabsdev.findaseat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class SeatAlredyExistException extends FindASetException {
    private String detail;
    public SeatAlredyExistException(String string) {
        this.detail = string;
    }


    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
        pb.setTitle("Seat Exception");
        pb.setType(URI.create("errors/resource-already-exist"));
        pb.setProperty("timestamp", Instant.now());
        return  pb;
    }
}
