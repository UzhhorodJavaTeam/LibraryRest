package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.UserDao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.libraryrest.models.User;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    public UserDaoImpl() {
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return null;
    }

    @Override
    @Transactional
    public User findById(Long userId) {
        String hql = "from User u where u.id = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("userId", userId);

        @SuppressWarnings("unchecked")
        List<User> userList = (List<User>) query.list();

        return userList.get(0);
    }

    @Override
    @Transactional
    public User findByName(String login) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("login", login));
        return (User) criteria.uniqueResult();
    }

    @Override
    @Transactional
    public void save(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    @Transactional
    public void remove(User user) {

    }

    @Override
    @Transactional
    public void remove(Long id) {

    }
}
