package com.pingvin.autoservice.form;

import com.pingvin.autoservice.dao.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ReserveValidator implements Validator {

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == SignUpForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "idOffer", "NotEmpty.signUpForm.idOffer");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateStart", "NotEmpty.signUpForm.dateStart");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateFinish", "NotEmpty.signUpForm.dateFinish");

        if (!errors.hasFieldErrors("dateStart")) {
            //if (!orderHistoryDAO.CheckReserveOfferByIdAndDate(signUpForm.getIdOffer(), signUpForm.getDateStart(), signUpForm.getDateFinish())) {
                errors.rejectValue("dateStart", "Duplicate.reserveForm.dateStart");
           // }
        }
    }

}
