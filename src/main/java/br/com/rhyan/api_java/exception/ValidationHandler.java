package br.com.rhyan.api_java.exception;

import org.springframework.http.*;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

public class ValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex){
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .toList();

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", 400);
        body.put("errors", erros);

        return ResponseEntity.badRequest().body(body);
    }
}
