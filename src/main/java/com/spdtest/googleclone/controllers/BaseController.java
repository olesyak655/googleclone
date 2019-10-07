package com.spdtest.googleclone.controllers;

import com.spdtest.googleclone.exceptions.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<AppException> handleMissingServletRequestParameter(MissingServletRequestParameterException exception) {
        AppException appException = new AppException(HttpStatus.BAD_REQUEST, exception);
        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<AppException> handleHttpClientError(HttpClientErrorException exception) {
        AppException appException = new AppException(exception.getStatusCode(), exception);
        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<AppException> handleBind(BindException exception) {
        AppException appException = new AppException(HttpStatus.BAD_REQUEST, exception);
        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppException> handleConstraintViolation() {
        AppException appException = new AppException(HttpStatus.BAD_REQUEST, "Url isn't correct. Please try again");
        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<AppException> handleThrowable(Throwable exception) {
        AppException appException;
        if (exception instanceof AppException) {
            appException = (AppException) exception;
        } else {
            logger.error("Stack trace: ", exception);
            appException = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, exception);
        }
        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }
}
