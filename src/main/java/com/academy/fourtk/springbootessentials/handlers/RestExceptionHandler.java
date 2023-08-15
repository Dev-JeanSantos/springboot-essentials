package com.academy.fourtk.springbootessentials.handlers;

import com.academy.fourtk.springbootessentials.exceptions.BadExceptionRequestDetails;
import com.academy.fourtk.springbootessentials.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadExceptionRequestDetails> handlerBadRequestException(BadRequestException badRequestException){
        return new ResponseEntity<>(
                BadExceptionRequestDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad request Exception, Check the Documentation")
                        .details(badRequestException.getMessage())
                        .developerMessage(badRequestException.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
