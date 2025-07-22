package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class DeliveryExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoOrderFoundException(NoOrderFoundException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.NOT_FOUND, "NoOrderFoundException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoDeliveryFoundException(NoDeliveryFoundException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.NOT_FOUND, "NoDeliveryFoundException");
    }
}
