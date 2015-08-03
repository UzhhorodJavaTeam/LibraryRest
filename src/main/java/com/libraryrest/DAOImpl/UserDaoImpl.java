package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.UserDao;
import com.libraryrest.models.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        return sessionFactory.getCurrentSession().createQuery("from User").list();
    }

    @Override
    @Transactional
    public User findById(Integer userId) {
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
        sessionFactory.getCurrentSession().delete(user);
    }

    @Override
    @Transactional
    public void remove(Integer id) {
        User user = findById(id);
        sessionFactory.getCurrentSession().delete(user);
    }
}
