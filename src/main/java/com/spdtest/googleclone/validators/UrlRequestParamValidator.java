package com.spdtest.googleclone.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlRequestParamValidator implements ConstraintValidator<Url, String> {

    @Override
    public void initialize(Url paramA) {
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext ctx) {
        if(url == null){
            return false;
        }
        String regex = "\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (url.matches(regex)) return true;
        else return false;
    }
}
