package com.libraryrest.controllers;

import com.libraryrest.DAO.AuthorDAO;
import com.libraryrest.exceptions.InvalidRequestException;
import com.libraryrest.models.Author;
import com.libraryrest.validators.AuthorValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yura on 02.06.15.
 */
@RestController
@RequestMapping("/authors")
public class AuthorController {

    final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    AuthorDAO authorDAO;

    @Autowired
    AuthorValidator validator;

    @RequestMapping(method = RequestMethod.GET)
    public List<Author> getAuthors() {
        return authorDAO.getAll();
    }

    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public Author findById(@PathVariable Integer author) {
        logger.info("GET: /authors" + author);
        return authorDAO.findById(author);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Author addAuthor(@RequestBody Author author, BindingResult bindingResult) {
        logger.info("POST: /authors/add" + author);
        validator.validate(author, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("POST: /authors/add" + author + bindingResult);
            throw new InvalidRequestException("Invalid author", bindingResult);
        }
        authorDAO.saveOrUpdate(author);
        return authorDAO.findById(author.getAuthorId());
    }

    @RequestMapping(value = "/{author}", method = RequestMethod.PUT)
    public Author editAuthor(@PathVariable Integer author, @RequestBody Author bookAuthor, BindingResult bindingResult) {
        logger.info("PUT: /authors" + author + "/edit");
        validator.validate(author, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("PUT: /authors" + author + "edit" + bindingResult);
            throw new InvalidRequestException("Invalid author", bindingResult);
        }
        bookAuthor.setAuthorId(author);
        authorDAO.updateAuthor(bookAuthor);
        return bookAuthor;
    }

    @RequestMapping(value = "/{author}", method = RequestMethod.DELETE)
    public String deleteAuthor(@PathVariable Integer author) {
        logger.info("DELETE: /authors" + author + "/delete");
        authorDAO.findById(author);
        authorDAO.deleteAuthor(author);
        return "The delete was successful";
    }
}
