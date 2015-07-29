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

    private Integer count;
    private Integer startAt;

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @Transactional
    public List<Book> getBooksByPage(Integer page) {
        count = 12;
        startAt = (count * (page - 1));

        Query query = sessionFactory.getCurrentSession().createQuery("FROM Book");
        query.setFirstResult(startAt);
        query.setMaxResults(count);

        @SuppressWarnings("unchecked")
        List<Book> books = (List<Book>) query.list();

        return books;
    }

    @Override
    @Transactional
    public List<Book> getAllBook() {
        Query query = sessionFactory.getCurrentSession().createQuery("from Book");
        List<Book> books = (List<Book>) query.list();
        ;
        return books;
    }

    @Override
    @Transactional
    public List<Book> getBooksByCategoryAndPage(Integer categoryId, Integer page) {
        count = 12;
        startAt = (count * (page - 1));

        Query query = sessionFactory.getCurrentSession().createQuery("FROM Book b WHERE b.bookCategory.categoryId = :categoryId");
        query.setParameter("categoryId", categoryId);
        query.setFirstResult(startAt);
        query.setMaxResults(count);

        @SuppressWarnings("unchecked")
        List<Book> books = (List<Book>) query.list();

        return books;
    }

    @Override
    @Transactional
    public List<Book> findByCategoryId(Integer categoryId) {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Book b WHERE b.bookCategory.categoryId = :categoryId");
        query.setParameter("categoryId", categoryId);

        @SuppressWarnings("unchecked")
        List<Book> itemList = (List<Book>) query.list();

        return itemList;
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
    public Integer update(Book book) {
        sessionFactory.getCurrentSession().merge(book);
        return book.getBookId();
    }


    @Override
    @Transactional
    public void deleteBook(Integer bookId) {
        Book book = findById(bookId);
        sessionFactory.getCurrentSession().delete(book);
    }

    @Override
    @Transactional
    public Book findByName(String name) {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Book b WHERE b.name = :name");
        query.setParameter("name", name);
        query.setMaxResults(1);

        @SuppressWarnings("unchecked")
        Book book = (Book) query.uniqueResult();

        return book;
    }
}
