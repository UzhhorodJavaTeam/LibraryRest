package com.libraryrest.DAO;

import com.libraryrest.models.BookCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yura on 27.05.15.
 */
@Repository
public interface CategoryDAO {

    public Integer saveOrUpdate(BookCategory category);

    public List<BookCategory> getAllCategory();

    public BookCategory findById(Integer category_id);

    public void deleteCategory(Integer category_id);

    public Integer update(BookCategory category);
}
