package com.odontoagenda.exception;

/**
 * Exceção lançada quando há uma violação de regra de negócio.
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
}
