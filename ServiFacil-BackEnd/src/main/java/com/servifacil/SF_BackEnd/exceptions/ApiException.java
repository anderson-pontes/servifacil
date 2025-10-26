package com.servifacil.SF_BackEnd.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {

        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){ return this.status; }
}