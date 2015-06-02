package com.libraryrest.controllers;

import com.libraryrest.DAO.CategoryDAO;
import com.libraryrest.exceptions.InvalidRequestException;
import com.libraryrest.models.BookCategory;
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
    CategoryValidator validator;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List getCategory() {
        logger.info("GET: /categories");
        return categoryDAO.getAllCategory();
    }

    @RequestMapping(value = "/{category}", method = RequestMethod.GET)
    public BookCategory showCategory(@PathVariable Integer category) {
        logger.info("Get: /category");
        return categoryDAO.findById(category);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BookCategory addCategory(@RequestBody BookCategory category, BindingResult bindingResult) {
        logger.info("Add category");
        validator.validate(category, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Invalid category", bindingResult);
        }
        Integer categoryId = categoryDAO.saveOrUpdate(category);
        category.setCategoryId(categoryId);
        return category;
    }

    @RequestMapping(value = "/{category}/edit", method = RequestMethod.POST)
    public BookCategory editCategory(@PathVariable Integer category, @RequestBody BookCategory bookCategory, BindingResult bindingResult) {
        logger.info("Edit" + category + "category");
        validator.validate(bookCategory, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Invalid category", bindingResult);
        }
        bookCategory.setCategoryId(category);
        categoryDAO.update(bookCategory);
        return bookCategory;
    }

    @RequestMapping(value = "/{category}/delete", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable Integer category) {
        logger.info("Delete" + category + "category");
        categoryDAO.findById(category);
        categoryDAO.deleteCategory(category);
        return "The delete was successful";
    }
}
