package com.pumahawk.rest.db.bridge.rest;

import com.pumahawk.rest.db.bridge.dto.ServerException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerException> exceptionHandler(Exception execption) {
        ServerException exceptionResponse = new ServerException();
        exceptionResponse.setMessage("Server error");
        for (Throwable e = execption; e != null; e = e.getCause()) {
            exceptionResponse.getExceptionsMessage().add(e.getMessage());
        }
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }
}
 