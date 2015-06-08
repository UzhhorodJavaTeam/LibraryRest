package com.libraryrest.DAO;

import com.libraryrest.models.Author;
import com.libraryrest.models.Book;

import java.util.List;

/**
 * Created by superuser on 27.05.15.
 */
public interface BookDAO {

    public List<Book> getAllBook();

    public Book findById(Integer bookId);

    public Integer saveOrUpdate(Book book);

    public void deleteBook(Integer id);

    public Integer update(Book book);

    public List<Book> findByCategoryId(Integer categoryId);
}
