package com.fasten.wp4.fpsot.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasten.wp4.fpsot.exception.NotFoundException;

@ControllerAdvice
public class ConfiguracaoSpringException extends ResponseEntityExceptionHandler {

    public ConfiguracaoSpringException() {
        super();
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> handleUserNotFoundException(NotFoundException ex, WebRequest request) {
      return ResponseEntity.notFound().build();
    }
    
}
