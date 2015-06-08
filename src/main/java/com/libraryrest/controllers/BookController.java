package com.libraryrest.controllers;

import com.libraryrest.DAO.BookDAO;
import com.libraryrest.DAO.CategoryDAO;
import com.libraryrest.exceptions.InvalidRequestException;
import com.libraryrest.models.Author;
import com.libraryrest.models.Book;
import com.libraryrest.models.BookCategory;
import com.libraryrest.validators.BookValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
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



    @RequestMapping(value = "categories/{categoryId}/books", method = RequestMethod.GET)
    public List<Book> getBooksByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        logger.info("GET: categories/"+ categoryId +"/books");

        List<Book> books = bookDAO.findByCategoryId(categoryId);

        return books;
    }


    @RequestMapping(value = "categories/{categoryId}/books/add", method = RequestMethod.POST)
    public Book postAddBookPage(@PathVariable("categoryId") Integer categoryId, @RequestBody Book book, BindingResult bindingResult) {
        logger.info("POST: categories/" + categoryId + "/books/add");
        validator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("POST: categories/" + categoryId + "/books/add " + bindingResult);
            throw new InvalidRequestException("Invalid book", bindingResult);
        }
        BookCategory bookCategory = categoryDAO.findById(categoryId);
        book.setBookCategory(bookCategory);
        Integer bookId = bookDAO.saveOrUpdate(book);
        book.setBookId(bookId);

        return book;
    }


    @RequestMapping(value = "categories/{categoryId}/books/{bookId}/edit", method = RequestMethod.POST)
    public Book postEditBookPage(@PathVariable("categoryId") Integer categoryId, @PathVariable("bookId") Integer bookId, @RequestBody Book book, BindingResult bindingResult) {
        logger.info("POST: categories/" + categoryId + "/books/" + bookId + "/edit");

        validator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("POST: categories/" + categoryId + "/books/" + bookId + "/edit" + bindingResult);
            throw new InvalidRequestException("Invalid book", bindingResult);
        }
        book.setBookCategory(categoryDAO.findById(categoryId));
        book.setBookId(bookId);

        bookDAO.update(book);

        return book;
    }


    @RequestMapping(value = "categories/{categoryId}/books/{bookId}", method = RequestMethod.GET)
    public Book getBookPage(@PathVariable("categoryId") Integer categoryId,@PathVariable("bookId") Integer bookId) {
        logger.info("GET: categories/"+ categoryId+"/books/" + bookId);
        Book book = bookDAO.findById(bookId);
        BookCategory category = categoryDAO.findById(categoryId);
        if (!book.getBookCategory().equals(category)){
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return book;
    }


    @RequestMapping(value = "categories/{categoryId}/books/{bookId}/delete", method = RequestMethod.GET)
    public String getDeleteCurrentCategory(@PathVariable("categoryId") Integer categoryId ,@PathVariable("bookId") Integer bookId) {
        logger.info("GET: categories/"+ categoryId +"/books/" + bookId + "/delete");
        bookDAO.deleteBook(bookId);

        return "Deleted Successfully";
    }
}
