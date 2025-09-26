package com.example.bloodbankmanagement.common.exception;

import com.example.bloodbankmanagement.common.untils.ErrorCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExceptionEntity {
    private String message;
    private String code;
    private List<FieldError> errors;

    private ExceptionEntity(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.errors = errors;
        this.code = code.getCode();
    }

    private ExceptionEntity(final ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = new ArrayList();
    }

    public static ExceptionEntity of(final ErrorCode code, final BindingResult bindingResult) {
        return new ExceptionEntity(code, FieldError.of(bindingResult));
    }

    public static ExceptionEntity of(final ErrorCode code) {
        return new ExceptionEntity(code);
    }

    public static ExceptionEntity of(final ErrorCode code, final List<FieldError> errors) {
        return new ExceptionEntity(code, errors);
    }

    public static ExceptionEntity of(MethodArgumentTypeMismatchException e) {
        String value = e.getValue() == null ? "" : String.valueOf(e.getValue());
        List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
        return new ExceptionEntity(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }

    public List<FieldError> getErrors() {
        return this.errors;
    }

    protected ExceptionEntity() {
    }

    public String toString() {
        return "ExceptionEntity(message=" + this.getMessage() + ", code=" + this.getCode() + ", errors=" + this.getErrors() + ")";
    }

    public static class FieldError implements Serializable {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        public static List<FieldError> of(final BindingResult bindingResult) {
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return (List)fieldErrors.stream().map((error) -> {
                return new FieldError(error.getField().replace("data.", ""), error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(), error.getDefaultMessage());
            }).collect(Collectors.toList());
        }

        public static FieldError from(final String field, final String value, final String reason) {
            return new FieldError(field, value, reason);
        }

        public String getField() {
            return this.field;
        }

        public String getValue() {
            return this.value;
        }

        public String getReason() {
            return this.reason;
        }

        protected FieldError() {
        }

        public String toString() {
            return "ExceptionEntity.FieldError(field=" + this.getField() + ", value=" + this.getValue() + ", reason=" + this.getReason() + ")";
        }
    }
}
