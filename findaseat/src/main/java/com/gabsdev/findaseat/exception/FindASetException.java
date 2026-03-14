package com.gabsdev.findaseat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class FindASetException extends RuntimeException {

    public ProblemDetail toProblemDetail(){
        var pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pb.setTitle("Business internal Error Server");
        return pb;
    }
}
