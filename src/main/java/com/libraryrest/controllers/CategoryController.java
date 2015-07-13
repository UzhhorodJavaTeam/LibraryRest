package com.libraryrest.controllers;

import com.libraryrest.DAO.*;
import com.libraryrest.exceptions.InvalidRequestException;
import com.libraryrest.models.Book;
import com.libraryrest.models.BookCategory;
import com.libraryrest.validators.BookValidator;
import com.libraryrest.validators.CategoryValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Created by yura on 27.05.15.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    BookDAO bookDAO;

    @Autowired
    ImageDao imageDao;

    @Autowired
    UserDao userDAO;

    @Autowired
    CategoryValidator categoryValidator;

    @Autowired
    BookValidator bookValidator;


    @RequestMapping(method = RequestMethod.GET)
    public List<BookCategory> getCategories() {
        logger.info("GET: /categories");
        return categoryDAO.getAllCategory();
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    public BookCategory showCategory(@PathVariable Integer categoryId) {
        logger.info("GET: /categories/" + categoryId);
        return categoryDAO.findById(categoryId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public BookCategory addCategory(@RequestBody BookCategory category, BindingResult bindingResult) {
        logger.info("POST: /categories/add");
        categoryValidator.validate(category, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("POST: /categories/add" + bindingResult);
            throw new InvalidRequestException("Invalid category", bindingResult);
        }
        Integer categoryId = categoryDAO.saveOrUpdate(category);
        category.setCategoryId(categoryId);
        return category;
    }

    @RequestMapping(value = "/{category}", method = RequestMethod.PUT)
    public BookCategory editCategory(@PathVariable Integer category, @RequestBody BookCategory bookCategory, BindingResult bindingResult) {
        logger.info("PUT: /categories/" + category + "/edit");
        categoryValidator.validate(bookCategory, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("PUT: /categories/" + category + "/edit" + bindingResult);
            throw new InvalidRequestException("Invalid category", bindingResult);
        }
        bookCategory.setCategoryId(category);
        categoryDAO.update(bookCategory);
        return bookCategory;
    }

    @RequestMapping(value = "/{category}", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable Integer category) {
        logger.info("DELETE: /categories/" + category + "/delete");
        categoryDAO.findById(category);
        categoryDAO.deleteCategory(category);
        return "The delete was successful";
    }


    @RequestMapping(value = "/{categoryId}/books/page={pageNum}", method = RequestMethod.GET)
    public List<Book> getBooksByCategoryIdAndPage(@PathVariable("categoryId") Integer categoryId,
                                                  @PathVariable("pageNum") Integer page) {
        logger.info("GET: categories/" + categoryId + "/books/page=" + page);

        List<Book> books = bookDAO.getBooksByCategoryAndPage(categoryId, page);

        return books;
    }

    @RequestMapping(value = "/{categoryId}/books", method = RequestMethod.GET)
    public List<Book> getBooksByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        logger.info("GET: categories/" + categoryId + "/books");

        List<Book> books = bookDAO.findByCategoryId(categoryId);

        return books;
    }


    @RequestMapping(value = "/{categoryId}/books", method = RequestMethod.POST)
    public Book postAddBookPage(@PathVariable("categoryId") Integer categoryId,
                                @RequestBody Book book, BindingResult bindingResult) {
        logger.info("POST: categories/" + categoryId + "/books/add");
        bookValidator.validate(book, bindingResult);
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

    @RequestMapping(value = "/{categoryId}/books/{bookId}", method = RequestMethod.PUT)
    public Book postEditBookPage(@PathVariable("categoryId") Integer categoryId, @PathVariable("bookId") Integer bookId, @RequestBody Book book, BindingResult bindingResult) {
        logger.info("POST: categories/" + categoryId + "/books/" + bookId + "/edit");

        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("POST: categories/" + categoryId + "/books/" + bookId + "/edit" + bindingResult);
            throw new InvalidRequestException("Invalid book", bindingResult);
        }
        book.setBookCategory(categoryDAO.findById(categoryId));
        book.setBookId(bookId);

        bookDAO.update(book);

        return book;
    }

    @RequestMapping(value = "/{categoryId}/books/{bookId}", method = RequestMethod.GET)
    public Book getBookPage(@PathVariable("categoryId") Integer categoryId, @PathVariable("bookId") Integer bookId) {
        logger.info("GET: categories/" + categoryId + "/books/" + bookId);
        Book book = bookDAO.findById(bookId);
        BookCategory category = categoryDAO.findById(categoryId);
        return book;
    }


    @RequestMapping(value = "/{categoryId}/books/{bookId}", method = RequestMethod.DELETE)
    public String getDeleteCurrentCategory(@PathVariable("categoryId") Integer categoryId, @PathVariable("bookId") Integer bookId) {
        logger.info("DELETE: categories/" + categoryId + "/books/" + bookId + "/delete");
        bookDAO.deleteBook(bookId);

        return "Deleted Successfully";
    }


}
