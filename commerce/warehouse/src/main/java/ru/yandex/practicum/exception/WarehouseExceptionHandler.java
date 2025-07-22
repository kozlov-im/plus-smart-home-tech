package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class WarehouseExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage handleSpecifiedProductAlreadyInWarehouseException(SpecifiedProductAlreadyInWarehouseException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.BAD_REQUEST, "SpecifiedProductAlreadyInWarehouseException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.BAD_REQUEST, "NoSpecifiedProductInWarehouseException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleProductNotFoundException(ProductNotFoundException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.NOT_FOUND, "ProductNotFoundException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleProductInShoppingCartLowQuantityInWarehouse(ProductInShoppingCartLowQuantityInWarehouse exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.NOT_FOUND, "ProductInShoppingCartLowQuantityInWarehouse");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoOrderFoundException(NoOrderFoundException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.NOT_FOUND, "NoOrderFoundException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(NotFoundException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorMessage(exception, HttpStatus.NOT_FOUND, "NotFoundException");
    }
}
