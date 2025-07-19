package ru.yandex.practicum.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        HttpStatus statusCode = HttpStatus.valueOf(response.status());

        try (InputStream body = response.body().asInputStream()) {
            ErrorMessage errorMessage = objectMapper.readValue(body, ErrorMessage.class);

            if (statusCode == HttpStatus.NOT_FOUND && errorMessage.getUserMessage().equals("NotFoundException")) {
                return new NotFoundException(errorMessage.getMessage());
            }
            if (statusCode == HttpStatus.BAD_REQUEST && errorMessage.getUserMessage().equals("NoSpecifiedProductInWarehouseException")) {
                return new NoSpecifiedProductInWarehouseException(errorMessage.getMessage());
            }
            if (statusCode == HttpStatus.NOT_FOUND && errorMessage.getUserMessage().equals("ProductNotFoundException")) {
                return new ProductNotFoundException(errorMessage.getMessage());
            }

        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        return new Exception("error");
    }
}