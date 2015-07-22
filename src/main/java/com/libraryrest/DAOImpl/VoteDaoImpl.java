package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.VoteDao;
import com.libraryrest.models.Vote;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class VoteDaoImpl implements VoteDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Integer saveOrUpdate(Vote vote) {
        sessionFactory.getCurrentSession().saveOrUpdate(vote);
        return vote.getId();
    }

    @Override
    @Transactional
    public Vote findByBookIdAndUserId(Integer bookId, Integer userId) {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Vote v where v.book.bookId = :bookId " +
                "and v.user.id = :userId");
        query.setParameter("bookId", bookId);
        query.setParameter("userId", userId);
        query.setMaxResults(1);

        @SuppressWarnings("unchecked")
        Vote vote = (Vote) query.uniqueResult();
        return vote;
    }

    @Override
    @Transactional
    public List<Vote> getAllVotes() {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Vote");
        return query.list();
    }

    @Override
    @Transactional
    public Vote findById(Integer id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Vote where id = :id");
        query.setParameter("id", id);
        return (Vote) query.uniqueResult();
    }

    @Override
    @Transactional
    public void deleteVote(Integer id) {
        Vote vote = findById(id);
        sessionFactory.getCurrentSession().delete(vote);
    }
}
