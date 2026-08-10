package com.gabsdev.findaseat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class WaitlistException extends FindASetException {
    private String detail;
    private Long id;
    public WaitlistException(String s, Long waitlistId) {
        this.detail = s;
        this.id = waitlistId;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatusAndDetail(HttpStatus.ACCEPTED, detail);
        pb.setTitle("Waitlist Exception");
        pb.setType(URI.create("errors/resource-not-found/waitlist"));
        pb.setProperty("timestamp", Instant.now());
        pb.setProperty("confirm", URI.create("api/waitlist/confirmwaitlist/"+id));
        return  pb;
    }
}
