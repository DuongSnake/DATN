package com.example.bloodbankmanagement.common.exception;

public class AuthenticationException extends RuntimeException {
    private final String errorCode;
    private final String locale;

    public AuthenticationException(String errorCode) {
        this.errorCode = errorCode;
        this.locale = "en";
    }

    public AuthenticationException(String errorCode, String locale) {
        this.errorCode = errorCode;
        this.locale = locale;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getLocale() {
        return this.locale;
    }
}