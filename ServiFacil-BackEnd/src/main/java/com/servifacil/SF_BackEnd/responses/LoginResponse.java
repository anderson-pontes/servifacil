package com.servifacil.SF_BackEnd.responses;

import java.time.LocalDateTime;

public class LoginResponse<T> {

    private boolean success;
    private String message;
    private String token;
    private LocalDateTime timestamp;

    public LoginResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
