package com.libraryrest.controllers;

import com.libraryrest.DAO.BookDAO;
import com.libraryrest.DAO.CategoryDAO;
import com.libraryrest.exceptions.InvalidRequestException;
import com.libraryrest.models.Book;
import com.libraryrest.models.BookCategory;
import com.libraryrest.validators.BookValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by superuser on 27.05.15.
 */
@RestController
@RequestMapping(name = "/")
public class BookController {

    final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    BookDAO bookDAO;

    @Autowired
    CategoryDAO categoryDAO;


    @Autowired
    BookValidator validator;


    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public List<Book> getBooks() {
        logger.info("GET: /books");

        List<Book> books = bookDAO.getAllBook();

        return books;
    }

}
