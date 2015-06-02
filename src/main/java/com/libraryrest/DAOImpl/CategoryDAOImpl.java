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
    public Integer saveOrUpdate(BookCategory category) {
        sessionFactory.getCurrentSession().saveOrUpdate(category);
        return category.getCategoryId();
    }

    @Override
    @Transactional
    public BookCategory findById(Integer categoryId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from BookCategory where categoryId = :categoryId");
        query.setParameter("categoryId", categoryId);
        return (BookCategory) query.uniqueResult();
    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryId) {
        BookCategory bookCategory = findById(categoryId);
        sessionFactory.getCurrentSession().delete(bookCategory);
    }

    @Override
    @Transactional
    public Integer update(BookCategory category){
        sessionFactory.getCurrentSession().merge(category);
        return category.getCategoryId();
    }
}
