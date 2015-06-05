package com.libraryrest.DAO;

import com.libraryrest.models.Author;

import java.util.List;

/**
 * Created by yura on 02.06.15.
 */
public interface AuthorDAO {

    public Author findById(Integer authorId);

    public List<Author> getAll();

    public void deleteAuthor(Integer authorId);

    public Integer saveOrUpdate(Author author);

    public Integer updateAuthor(Author author);

}
