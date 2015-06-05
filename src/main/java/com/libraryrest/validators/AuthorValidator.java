package com.libraryrest.validators;

import com.libraryrest.models.Author;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by yura on 02.06.15.
 */
@Component
public class AuthorValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Author.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "First name must not be empty", "Field first name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Last name must not be empty", "Field last name is required.");
    }
}

