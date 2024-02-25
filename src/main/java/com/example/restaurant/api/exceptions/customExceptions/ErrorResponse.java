package com.example.restaurant.api.exceptions.customExceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    String message;
    public ErrorResponse() {}
}
