package br.com.ifes.backend_pit.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ExceptionDto {
    private HttpStatus statusCode;
    private String message;
}
