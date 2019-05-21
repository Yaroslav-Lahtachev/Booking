package com.pingvin.autoservice.form;


import com.pingvin.autoservice.dao.UserDAO;
import com.pingvin.autoservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserDAO usersDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UsersForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UsersForm usersForm = (UsersForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "NotEmpty.usersForm.login");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.usersForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.usersForm.confirmPassword");

        if (!errors.hasFieldErrors("login")) {
            User user = usersDAO.findByLogin(usersForm.getLogin());
            if (user != null) {
                errors.rejectValue("login", "Duplicate.usersForm.login");
            }
        }

        if (!errors.hasErrors()) {
            if (!usersForm.getConfirmPassword().equals(usersForm.getPassword())) {
                errors.rejectValue("confirmPassword", "Match.usersForm.confirmPassword");
            }
        }
    }
}