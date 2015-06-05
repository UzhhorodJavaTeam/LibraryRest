package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.AuthorDAO;
import com.libraryrest.models.Author;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by yura on 02.06.15.
 */
@Repository
public class AuthorDAOImpl implements AuthorDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Author findById(Integer authorId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Author where authorId = :authorId");
        query.setParameter("authorId", authorId);
        return (Author) query.uniqueResult();
    }

    @Override
    @Transactional
    public List<Author> getAll() {
        Query query = sessionFactory.getCurrentSession().createQuery("from Author ");
        return query.list();
    }

    @Override
    @Transactional
    public void deleteAuthor(Integer authorId) {
        Author author = findById(authorId);
        sessionFactory.getCurrentSession().delete(author);
    }

    @Override
    @Transactional
    public Integer saveOrUpdate(Author author) {
        sessionFactory.getCurrentSession().saveOrUpdate(author);
        return author.getAuthorId();
    }

    @Override
    @Transactional
    public Integer updateAuthor(Author author) {
        sessionFactory.getCurrentSession().update(author);
        return author.getAuthorId();
    }
}

