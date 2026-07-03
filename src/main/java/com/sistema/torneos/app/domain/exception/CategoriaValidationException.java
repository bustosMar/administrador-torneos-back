package com.sistema.torneos.app.domain.exception;

public class CategoriaValidationException extends RuntimeException {
    
    public CategoriaValidationException(String message) {
        super(message);
    }
    
    public CategoriaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
