package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ShoppingCartExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleNotAuthorizedUserException(NotAuthorizedUserException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.UNAUTHORIZED, "User is not authorized");
    }
}
