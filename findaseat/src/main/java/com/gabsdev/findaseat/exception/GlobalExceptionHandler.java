package com.gabsdev.findaseat.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(FindASetException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(BusinessNotFoundException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
}
