package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WarehouseExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage handleSpecifiedProductAlreadyInWarehouseException(SpecifiedProductAlreadyInWarehouseException exception) {
        return new ErrorMessage(exception, HttpStatus.BAD_REQUEST, "Chosen product already in warehouse");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException exception) {
        return new ErrorMessage(exception, HttpStatus.BAD_REQUEST, "Chosen product is absent in warehouse");
    }
}
