package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.RoleDao;
import com.libraryrest.exceptions.DaoSystemException;
import com.libraryrest.exceptions.NoSuchEntityException;
import com.libraryrest.models.Role;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Component
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private SessionFactory sessionFactory;

    public RoleDaoImpl() {
    }

    public RoleDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public List<Role> findAll() throws DaoSystemException  {
        List<Role> categoryList = new ArrayList<Role>();

        try {
            categoryList = sessionFactory.getCurrentSession().createCriteria(Role.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return categoryList;
    }

    @Override
    @Transactional
    public Role findById(Long id) throws DaoSystemException, NoSuchEntityException {
        String hql = "from Role r where r.id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        query.setMaxResults(1);

        @SuppressWarnings("unchecked")
        Role roleList = (Role) query.uniqueResult();

        return roleList;
    }

    @Override
    @Transactional
    public void saveOrUpdate(Role role) {
        sessionFactory.getCurrentSession().saveOrUpdate(role);
    }

    @Override
    @Transactional
    public void remove(Role role) {
        sessionFactory.getCurrentSession().delete(role);
    }

    @Override
    @Transactional
    public void remove(Long id) throws DaoSystemException, NoSuchEntityException {
        Role role = findById(id);
        sessionFactory.getCurrentSession().delete(role);
    }
}
