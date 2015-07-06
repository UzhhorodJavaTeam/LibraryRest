package com.libraryrest.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.libraryrest.models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class RegistrationFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login",
                "required.login", "Field name is required.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
                "required.password", "Field name is required.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "matchingPassword",
                "required.matchingPassword", "Field name is required.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email",
                "required.email", "Field name is required.");

        User user = (User)target;

//        if(!(user.getPassword().equals(user.getMatchingPassword()))){
//            errors.rejectValue("password", "notmatch.password");
//        }

        if (!user.getEmail().equals("") && !isValidEmailAddress(user.getEmail())) errors.rejectValue("email",
                "invalid.email", "Email address is invalid");
    }

    public boolean isValidEmailAddress(String emailAddress){
        String expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }
}
