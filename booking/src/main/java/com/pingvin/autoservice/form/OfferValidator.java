package com.pingvin.autoservice.form;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class OfferValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == OfferForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        OfferForm offerForm = (OfferForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "NotEmpty.offerForm.city");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.offerForm.address");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maxPeopleCount", "NotEmpty.offerForm.maxPeopleCount");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.offerForm.price");
    }

}
