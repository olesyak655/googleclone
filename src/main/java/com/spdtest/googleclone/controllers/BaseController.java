package com.spdtest.googleclone.controllers;

import com.spdtest.googleclone.exceptions.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;

@Controller
public class BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler()
    public ResponseEntity<AppException> handleException(Throwable exception) {

        AppException appException;

        if (exception instanceof AppException) {
            appException = (AppException) exception;
        } else {
            if (exception instanceof MissingServletRequestParameterException) {
                appException = new AppException(HttpStatus.BAD_REQUEST, exception);
            } else if (exception instanceof HttpClientErrorException) {
                HttpClientErrorException httpException = (HttpClientErrorException) exception;
                appException = new AppException(httpException.getStatusCode(), exception);
            } else if (exception instanceof BindException) {
                appException = new AppException(HttpStatus.BAD_REQUEST, exception);
            } else if (exception instanceof ConstraintViolationException) {
                appException = new AppException(HttpStatus.BAD_REQUEST, "Url isn't correct. Please try again");
            } else {
                logger.error("Stack trace: ", exception);

                appException = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, exception);
            }
        }
        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }
}
