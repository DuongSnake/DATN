package com.example.bloodbankmanagement.common.exception;

import java.util.List;

/**
 *
 * @author PhongPV
 */
public class IPlusValidateException extends RuntimeException {

    final List<IPlusExceptionEntity.FieldError> errors;

    String locale = "en";

    public IPlusValidateException(List<IPlusExceptionEntity.FieldError> errors) {
        super();
        this.errors = errors;
    }

    public IPlusValidateException(List<IPlusExceptionEntity.FieldError> errors, String locale) {
        super();
        this.errors = errors;
        this.locale = locale;
    }

    public List<IPlusExceptionEntity.FieldError> getErrors() {
        return errors;
    }

    public String getLocale() {
        return locale;
    }

}
