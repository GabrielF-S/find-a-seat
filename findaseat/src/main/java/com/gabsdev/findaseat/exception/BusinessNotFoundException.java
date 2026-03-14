package com.gabsdev.findaseat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class BusinessNotFoundException extends FindASetException {

    private final String detail;

    public BusinessNotFoundException(String detail) {
        this.detail = detail;
    }


    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail);
        pb.setTitle("Business Exception");
        pb.setType(URI.create("errors/resource-not-found"));
       pb.setProperty("timestamp", Instant.now());
        return  pb;
    }
}
