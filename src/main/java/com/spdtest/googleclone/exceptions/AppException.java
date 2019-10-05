package com.spdtest.googleclone.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
public class AppException extends RuntimeException {

    @JsonProperty("error_message")
    private String message;

    @JsonIgnore
    private HttpStatus httpStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Map<String, Object>> errors;

    public AppException() {}

    public AppException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public AppException(HttpStatus httpStatus, String message, Throwable throwable) {
        this(httpStatus, throwable);
        this.message = message;
    }

    public AppException(HttpStatus httpStatus, Throwable throwable) {
        super(throwable);

        this.httpStatus = httpStatus;
        this.message = throwable.getMessage();

        if (throwable instanceof BindException) {
            this.message = "Validation failure";
            errors = new HashMap<>();

            BindException be = (BindException) throwable;

            for (FieldError error : be.getFieldErrors()) {
                Map<String, Object> objErrors = errors.getOrDefault(error.getObjectName(), new HashMap<>());
                objErrors.put(error.getField(), error.getDefaultMessage());
                errors.put(error.getObjectName(), objErrors);
            }
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map getErrors() {
        return errors;
    }

}
