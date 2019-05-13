package com.pingvin.autoservice.form;

import com.pingvin.autoservice.dao.OrderHistoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ReserveValidator implements Validator {

    @Autowired
    private OrderHistoryDAO orderHistoryDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ReserveForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReserveForm reserveForm = (ReserveForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "idOffer", "NotEmpty.reserveForm.idOffer");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateStart", "NotEmpty.reserveForm.dateStart");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateFinish", "NotEmpty.reserveForm.dateFinish");

        if (!errors.hasFieldErrors("dateStart")) {
            if (!orderHistoryDAO.CheckReserveOfferByIdAndDate(reserveForm.getIdOffer(), reserveForm.getDateStart(), reserveForm.getDateFinish())) {
                errors.rejectValue("dateStart", "Duplicate.reserveForm.dateStart");
            }
        }
    }

}
