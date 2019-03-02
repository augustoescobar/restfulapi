package com.example.restfulapi.controllers;

import com.example.restfulapi.dtos.ArgumentNotValidDTO;
import com.example.restfulapi.dtos.FieldErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ArgumentNotValidDTO argumentNotValidDTO = new ArgumentNotValidDTO();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {

            FieldErrorDTO fieldErrorDTO = new FieldErrorDTO();
            fieldErrorDTO.setFieldName(fieldError.getField());
            fieldErrorDTO.setErrorMessage(fieldError.getDefaultMessage());

            argumentNotValidDTO.getFieldErrorDTOs().add(fieldErrorDTO);
        });

        return ResponseEntity.badRequest().body(argumentNotValidDTO);
    }
}
