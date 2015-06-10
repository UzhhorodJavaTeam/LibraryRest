package com.libraryrest.controllers;

import com.libraryrest.DAO.AuthorDAO;
import com.libraryrest.models.Author;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by superuser on 09.06.15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test_config.xml")
@WebAppConfiguration
@Transactional
public class AuthorControllerTest {

    @InjectMocks
    BookController bookController;

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    AuthorDAO authorDAO;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddAuthor() throws Exception {
        mockMvc.perform(post("/authors")
                .content("{\"firstName\":\"Leonardo\", \"lastName\":\"Da Vinchi\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Leonardo")))
                .andExpect(jsonPath("$.lastName", is("Da Vinchi")))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetAllAuthors() throws Exception {
        Author author1 = new Author("test1", "test1");
        Author author2 = new Author("test2", "test2");
        authorDAO.saveOrUpdate(author1);
        authorDAO.saveOrUpdate(author2);
        mockMvc.perform(get("/authors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].authorId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("test1")))
                .andExpect(jsonPath("$[0].lastName", is("test1")))
                .andExpect(jsonPath("$[1].authorId", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("test2")))
                .andExpect(jsonPath("$[1].lastName", is("test2")));
        authorDAO.deleteAuthor(author1.getAuthorId());
        authorDAO.deleteAuthor(author2.getAuthorId());
    }

    @Test
    public void testGetAuthorById() throws Exception {
        Author author = new Author("Test for view", "Test for view");
        authorDAO.saveOrUpdate(author);
        mockMvc.perform(get("/authors/" + author.getAuthorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorId", is(author.getAuthorId())))
                .andExpect(jsonPath("$.firstName", is("Test for view")))
                .andExpect(jsonPath("$.lastName", is("Test for view")));
        authorDAO.deleteAuthor(author.getAuthorId());
    }


    @Test
    public void testEditAuthor() throws Exception {
        mockMvc.perform(put("/authors/1")
                .content("{\"firstName\":\"Changed\", \"lastName\":\"Changed\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId", is(1)))
                .andExpect(jsonPath("$.firstName", is("Changed")))
                .andExpect(jsonPath("$.lastName", is("Changed")))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        Author author = new Author("Test for delete", "Test for delete");
        authorDAO.saveOrUpdate(author);
        mockMvc.perform(delete("/authors/" + author.getAuthorId())
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("The delete was successful"));
    }

    @Test
    public void testAddAuthorWithErrors() throws Exception {
        mockMvc.perform(post("/authors")
                .content("{\"firstName\":\"\", \"lastName\":\"\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("InvalidRequest")))
                .andExpect(jsonPath("$.message", is("Invalid author")))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[*].resource", containsInAnyOrder("author", "author")))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder(
                        "firstName", "lastName")))
                .andExpect(jsonPath("$.fieldErrors[*].code", containsInAnyOrder(
                        "First name must not be empty", "Last name must not be empty")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
                        "Field first name is required.", "Field last name is required.")))
                .andExpect(status().is4xxClientError());
    }

}

