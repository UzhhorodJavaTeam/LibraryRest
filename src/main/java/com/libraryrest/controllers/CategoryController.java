package com.libraryrest.controllers;

import com.libraryrest.DAO.CategoryDAO;
import com.libraryrest.models.BookCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yura on 27.05.15.
 */
@RestController
public class CategoryController {

    @Autowired
    CategoryDAO bookCategoryDAO;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List getBookCategory() {
        return bookCategoryDAO.getAllCategory();
    }

    @RequestMapping(value = "/categories/{category}", method = RequestMethod.GET)
    public BookCategory showCategory(@PathVariable Integer category) {
        return bookCategoryDAO.findById(category);
    }

    @RequestMapping(value = "/categories/add", method = RequestMethod.POST)
    public BookCategory addCategory(@ModelAttribute BookCategory category) {
        bookCategoryDAO.addCategory(category);
        return bookCategoryDAO.findById(category.getCategory_id());
    }

    @RequestMapping(value = "/categories/{category}/edit", method = RequestMethod.POST)
    public BookCategory editCategory(@PathVariable Integer category,@ModelAttribute BookCategory bookCategory) {
        bookCategoryDAO.findById(category);
        bookCategory.setCategory_id(category);
        bookCategoryDAO.editCategory(bookCategory);
        return bookCategoryDAO.findById(bookCategory.getCategory_id());
    }

    @RequestMapping(value = "/categories/{category}/delete", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable Integer category) {
        bookCategoryDAO.findById(category);
        bookCategoryDAO.deleteCategory(category);
        return "The delete was successful";
    }
}
