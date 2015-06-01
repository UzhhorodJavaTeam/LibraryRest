package com.libraryrest.validators;


import com.libraryrest.DAO.BookDAO;
import com.libraryrest.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;



@Component
public class BookValidator implements Validator {

    @Autowired
    BookDAO bookDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
                "Book name can not be empty", "Field name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description",
                "Book description can not be empty", "Field description is required.");

    }

}
