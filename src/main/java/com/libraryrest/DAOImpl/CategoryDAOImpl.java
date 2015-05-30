package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.CategoryDAO;
import com.libraryrest.models.BookCategory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by yura on 27.05.15.
 */
@Component
public class CategoryDAOImpl implements CategoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public List<BookCategory> getAllCategory() {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM BookCategory");
        return query.list();
    }

    @Override
    @Transactional
    public void addCategory(BookCategory category) {
        sessionFactory.getCurrentSession().save(category);
    }

    @Override
    @Transactional
    public void editCategory(BookCategory category) {
        sessionFactory.getCurrentSession().update(category);
    }

    @Override
    @Transactional
    public BookCategory findById(Integer category_id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from BookCategory where category_id = :category_id");
        query.setParameter("category_id", category_id);
        return (BookCategory) query.uniqueResult();
    }

    @Override
    @Transactional
    public void deleteCategory(Integer category_id) {
        BookCategory bookCategory = (BookCategory) sessionFactory.getCurrentSession().load(BookCategory.class, category_id);
        sessionFactory.getCurrentSession().delete(bookCategory);
    }
}
