package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorMessage {
    private Throwable cause;
    private StackTraceElement[] stackTrace;
    private HttpStatus httpStatus;
    private String userMessage;
    private String message;
    private Throwable[] suppressed;
    private String localizedMessage;

    public ErrorMessage(Exception exception, HttpStatus httpStatus, String userMessage) {
        this.cause = exception.getCause();
        this.stackTrace = exception.getStackTrace();
        this.httpStatus = httpStatus;
        this.userMessage = userMessage;
        this.message = exception.getMessage();
        this.suppressed = exception.getSuppressed();
        this.localizedMessage = exception.getLocalizedMessage();


    }
}
