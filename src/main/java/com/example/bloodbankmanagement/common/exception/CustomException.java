package com.example.bloodbankmanagement.common.exception;

public class CustomException extends RuntimeException {
    private final String errorCode;
    private final String locale;

    public CustomException(String errorCode) {
        this.errorCode = errorCode;
        this.locale = "en";
    }

    public CustomException(String errorCode, String locale) {
        this.errorCode = errorCode;
        this.locale = locale;
    }

    public CustomException(String message, String errorCode, String locale) {
        super(message);
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
