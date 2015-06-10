package com.libraryrest.controllers;

import com.libraryrest.DAO.BookDAO;
import com.libraryrest.DAO.CategoryDAO;
import com.libraryrest.models.Book;
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
 * Created by yura on 09.06.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test_config.xml")
@WebAppConfiguration
public class BookControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void addBookShouldReturnNewBook() throws Exception {
        Book book = new Book("Example", "Example");
        BookCategory category = new BookCategory("Test");
        categoryDAO.saveOrUpdate(category);
        book.setBookCategory(category);
        bookDAO.saveOrUpdate(book);
        Integer categoryId = category.getCategoryId();
        Integer bookId = book.getBookId();
        mockMvc.perform(get("/categories/{categoryId}/books/{bookId}", categoryId, bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("bookId", is(bookId)))
                .andExpect(jsonPath("name", is("Example")))
                .andExpect(jsonPath("description", is("Example")));
    }

    @Test
    public void editBookShouldReturnChangeBook() throws Exception {
        Book book = new Book("Example", "Example");
        BookCategory category = new BookCategory("Test");
        categoryDAO.saveOrUpdate(category);
        book.setBookCategory(category);
        bookDAO.saveOrUpdate(book);
        Integer categoryId = category.getCategoryId();
        Integer bookId = book.getBookId();
        book.setName("Change name");
        book.setDescription("Change description");
        bookDAO.update(book);
        mockMvc.perform(get("/categories/{categoryId}/books/{bookId}", categoryId, bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("bookId", is(bookId)))
                .andExpect(jsonPath("name", is("Change name")))
                .andExpect(jsonPath("description", is("Change description")));
    }

    @Test
    public void deleteBookShouldReturnSuccessfully() throws Exception {
        Book book = new Book("Example", "Example");
        BookCategory category = new BookCategory("Test");
        categoryDAO.saveOrUpdate(category);
        book.setBookCategory(category);
        bookDAO.saveOrUpdate(book);
        Integer categoryId = category.getCategoryId();
        Integer bookId = book.getBookId();
        mockMvc.perform(delete("/categories/{categoryId}/books/{bookId}", categoryId, bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN))
                .andExpect(content().string("Deleted Successfully"));
    }

    @Test
    public void showAllBooksShouldReturnAllBooks() throws Exception {
        Book fistBook = new Book("First book", "First book");
        Book secondBook = new Book("Second book", "Second book");
        BookCategory category = new BookCategory("Test");
        categoryDAO.saveOrUpdate(category);
        fistBook.setBookCategory(category);
        secondBook.setBookCategory(category);
        bookDAO.saveOrUpdate(fistBook);
        bookDAO.saveOrUpdate(secondBook);
        Integer categoryId = category.getCategoryId();
        mockMvc.perform(get("/categories/{categoryId}/books", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].name", is("First book")))
                .andExpect(jsonPath("$[0].description", is("First book")))
                .andExpect(jsonPath("$[1].name", is("Second book")))
                .andExpect(jsonPath("$[1].description", is("Second book")));
    }

    @Test
    public void nameAndDescriptionIsEmptyShouldReturnValidationErrors() throws Exception {
        Book book = new Book("", "");
        BookCategory category = new BookCategory("Test");
        categoryDAO.saveOrUpdate(category);
        Integer categoryId = category.getCategoryId();
        mockMvc.perform(post("/categories/{categoryId}/books", categoryId)
                        .contentType(TestUtil.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(book))
        )
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("name", "description")))
                .andExpect(jsonPath("$.fieldErrors[*].code", containsInAnyOrder("Book name can not be empty", "Book description can not be empty")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder("Field name is required.", "Field description is required.")));
    }

}
