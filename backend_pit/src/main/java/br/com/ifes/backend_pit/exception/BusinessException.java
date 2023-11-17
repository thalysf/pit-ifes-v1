package br.com.ifes.backend_pit.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

}