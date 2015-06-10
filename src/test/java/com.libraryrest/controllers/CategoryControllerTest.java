package com.libraryrest.controllers;

import com.libraryrest.DAO.CategoryDAO;
import com.libraryrest.models.BookCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by yura on 05.06.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test_config.xml")
@WebAppConfiguration
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void addCategoryShouldReturnNewCategory() throws Exception {
        BookCategory category = new BookCategory("Test");
        categoryDAO.saveOrUpdate(category);
        Integer categoryId = category.getCategoryId();

        mockMvc.perform(get("/categories/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("categoryId", is(categoryId)))
                .andExpect(jsonPath("categoryTitle", is("Test")));
    }

    @Test
    public void showCategoriesShouldReturnAllCategories() throws Exception {
        BookCategory firstCategory = new BookCategory("TestOne");
        categoryDAO.saveOrUpdate(firstCategory);
        BookCategory secondCategory = new BookCategory("TestTwo");
        categoryDAO.saveOrUpdate(secondCategory);
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[1].categoryTitle", is("TestOne")))
                .andExpect(jsonPath("$[2].categoryTitle", is("TestTwo")));
    }


    @Test
    public void editCategoryShouldReturnChangeCategory() throws Exception {
        BookCategory category = new BookCategory("Test");
        categoryDAO.saveOrUpdate(category);
        Integer categoryId = category.getCategoryId();
        category.setCategoryTitle("Change title");
        categoryDAO.update(category);
        mockMvc.perform(get("/categories/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("categoryId", is(categoryId)))
                .andExpect(jsonPath("categoryTitle", is("Change title")));
    }

    @Test
    public void deleteCategoryShouldReturnSuccessfully() throws Exception {
        BookCategory category = new BookCategory("Example");
        categoryDAO.saveOrUpdate(category);
        Integer categoryId = category.getCategoryId();
        mockMvc.perform(delete("/categories/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN))
                .andExpect(content().string("The delete was successful"));
    }


    @Test
    public void categoryTitleIsEmptyShouldReturnValidationErrors() throws Exception {
        BookCategory category = new BookCategory("");
        mockMvc.perform(post("/categories")
                        .contentType(TestUtil.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(category))
        )
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("categoryTitle")))
                .andExpect(jsonPath("$.fieldErrors[*].code", containsInAnyOrder("Category title must not be empty")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder("Field category title is required.")));
    }
}
