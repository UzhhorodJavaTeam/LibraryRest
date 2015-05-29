package com.libraryrest.controllers;

import com.libraryrest.DAO.BookDAO;
import com.libraryrest.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by superuser on 27.05.15.
 */
@RestController
@RequestMapping(name = "/")
public class BookController {

    @Autowired
    BookDAO bookDAO;

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public List<Book> getBooks(ModelMap model) {
        List<Book> books = bookDAO.getAllBook();
        model.addAttribute("books", books);

        return books;
    }


    @RequestMapping(value = "/books/add", method = RequestMethod.POST)
    public Book postAddBookPage(@RequestBody Book book) {

        Integer bookId = bookDAO.saveOrUpdate(book);
        book.setBook_id(bookId);
        return book;
    }



    @RequestMapping(value = "/books/{bookId}/edit", method = RequestMethod.POST)
    public Book postEditBookPage(@PathVariable("bookId") Integer bookId, @RequestBody Book book){
        book.setBook_id(bookId);
        bookDAO.update(book);

        return book;
    }


    @RequestMapping(value = "/books/{bookId}", method = RequestMethod.GET)
    public Book getBookPage(@PathVariable("bookId") Integer bookId, ModelMap model) {
        Book book = bookDAO.findById(bookId);
        model.addAttribute("book", book);

        return book;

    }


    @RequestMapping(value = "/books/{bookId}/delete", method = RequestMethod.GET)
    public String getDeleteCurrentCategory(@PathVariable("bookId") Integer bookId) {

        bookDAO.deleteBook(bookId);

        return "Successfully";
    }

}
