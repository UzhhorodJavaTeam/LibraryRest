package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.RatingDao;
import com.libraryrest.models.Rating;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class RatingDaoImpl implements RatingDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Integer saveOrUpdate(Rating rating) {
        sessionFactory.getCurrentSession().saveOrUpdate(rating);
        return rating.getId();
    }

    @Override
    @Transactional
    public List<Rating> getAllRatings() {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Rating");
        return query.list();
    }

    @Override
    @Transactional
    public Rating findById(Integer id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Rating where id = :id");
        query.setParameter("id", id);
        return (Rating) query.uniqueResult();
    }

    @Override
    @Transactional
    public Rating findByBookId(Integer id){
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Rating r where r.book.bookId = :bookId");
        query.setParameter("bookId", id);

        return (Rating) query.uniqueResult();
    }

    @Override
    @Transactional
    public void deleteRating(Integer id) {
        Rating rating = findById(id);
        sessionFactory.getCurrentSession().delete(rating);
    }

}
