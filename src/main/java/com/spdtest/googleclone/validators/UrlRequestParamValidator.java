package com.spdtest.googleclone.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlRequestParamValidator implements ConstraintValidator<Url, String> {

    @Override
    public void initialize(Url paramA) {
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext ctx) {
        boolean isValid = true;
        if(url != null){
            String regex = "\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            if (!url.matches(regex)) {
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return isValid;
    }
}
