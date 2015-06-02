package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.BookDAO;
import com.libraryrest.models.Book;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by superuser on 27.05.15.
 */

@Component
public class BookDAOImpl implements BookDAO {


    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @Transactional
    public List<Book> getAllBook() {
        Query query = sessionFactory.getCurrentSession().createQuery("from Book");
        List<Book> books = (List<Book>) query.list();;
        return books;
    }

    @Override
    @Transactional
    public Book findById(Integer bookId) {
        String hql = "from Book b where b.id = :bookId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("bookId", bookId);
        query.setMaxResults(1);

        @SuppressWarnings("unchecked")
        Book itemList = (Book) query.uniqueResult();

        return itemList;
    }

    @Override
    @Transactional
    public Integer saveOrUpdate(Book book) {
        sessionFactory.getCurrentSession().saveOrUpdate(book);
        return book.getBookId();
    }

    @Override
    @Transactional
    public Integer update(Book book){
        sessionFactory.getCurrentSession().merge(book);
        return book.getBookId();
    }



    @Override
    @Transactional
    public void deleteBook(Integer bookId) {
        Book book = findById(bookId);
        sessionFactory.getCurrentSession().delete(book);
    }



}
