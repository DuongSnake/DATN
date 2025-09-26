package com.example.bloodbankmanagement.common.exception;

import java.util.List;

public class ValidateDuplicateException extends RuntimeException {

    private final List<ExceptionEntity.FieldError> errors;

    String locale = "en";


    public ValidateDuplicateException(List<ExceptionEntity.FieldError> errors) {
        super();
        this.errors = errors;
    }

    public ValidateDuplicateException(List<ExceptionEntity.FieldError> errors, String locale) {
        super();
        this.errors = errors;
        this.locale = locale;
    }

    public List<ExceptionEntity.FieldError> getErrors() {
        return errors;
    }

    public String getLocale() {
        return locale;
    }

}