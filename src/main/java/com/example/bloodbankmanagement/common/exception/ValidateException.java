package com.example.bloodbankmanagement.common.exception;

import java.util.List;

public class ValidateException  extends RuntimeException {
    final List<ExceptionEntity.FieldError> errors;
    String locale = "en";

    public ValidateException(List<ExceptionEntity.FieldError> errors) {
        this.errors = errors;
    }

    public ValidateException(List<ExceptionEntity.FieldError> errors, String locale) {
        this.errors = errors;
        this.locale = locale;
    }

    public List<ExceptionEntity.FieldError> getErrors() {
        return this.errors;
    }

    public String getLocale() {
        return this.locale;
    }
}
