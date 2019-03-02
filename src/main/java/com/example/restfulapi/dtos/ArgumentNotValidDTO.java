package com.example.restfulapi.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArgumentNotValidDTO {

    private LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("errors")
    private List<FieldErrorDTO> fieldErrorDTOs = new ArrayList<>();

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<FieldErrorDTO> getFieldErrorDTOs() {
        return fieldErrorDTOs;
    }

    public void setFieldErrorDTOs(List<FieldErrorDTO> fieldErrorDTOs) {
        this.fieldErrorDTOs = fieldErrorDTOs;
    }
}