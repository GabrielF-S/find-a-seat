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
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(BusinessExistsException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(ConflictReservationException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(EmployeeNotFoundException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(FloorAlredyExistException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(FloorNoFoundException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(NumberOfSeatsException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(ReservationConflictException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(ReservationDesativatedException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(ReservationNotFoundException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(ReservationPeriodDayException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(SeatAlredyExistException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(SeatNotFoundException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(UserNotFoundException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(WaitlistException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handle(WaitlistNotFoundException exception){
        return ResponseEntity.of(exception.toProblemDetail()).build();
    }
}
