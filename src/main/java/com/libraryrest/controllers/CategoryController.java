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
@RequestMapping("/")
public class CategoryController {

    final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    CategoryValidator validator;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List getCategory() {
        logger.info("GET: /categories");
        return categoryDAO.getAllCategory();
    }

    @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.GET)
    public BookCategory showCategory(@PathVariable Integer categoryId) {
        logger.info("Get: /categories/" + categoryId);
        return categoryDAO.findById(categoryId);
    }

    @RequestMapping(value = "/categories/add", method = RequestMethod.POST)
    public BookCategory addCategory(@RequestBody BookCategory category, BindingResult bindingResult) {
        logger.info("POST: /categories/add");
        validator.validate(category, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("POST: /categories/add" + bindingResult);
            throw new InvalidRequestException("Invalid category", bindingResult);
        }
        Integer categoryId = categoryDAO.saveOrUpdate(category);
        category.setCategoryId(categoryId);
        return category;
    }

    @RequestMapping(value = "/categories/{category}/edit", method = RequestMethod.POST)
    public BookCategory editCategory(@PathVariable Integer category, @RequestBody BookCategory bookCategory, BindingResult bindingResult) {
        logger.info("POST: /categories/" + category + "/edit");
        validator.validate(bookCategory, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("POST: /categories/" + category + "/edit" + bindingResult);
            throw new InvalidRequestException("Invalid category", bindingResult);
        }
        bookCategory.setCategoryId(category);
        categoryDAO.update(bookCategory);
        return bookCategory;
    }

    @RequestMapping(value = "/categories/{category}/delete", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable Integer category) {
        logger.info("DELETE: /categories" + category + "/delete");
        categoryDAO.findById(category);
        categoryDAO.deleteCategory(category);
        return "The delete was successful";
    }
}
