package com.libraryrest.DAOImpl;

import com.libraryrest.DAO.ImageDao;
import com.libraryrest.models.Image;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class ImageDaoImpl implements ImageDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Integer saveOrUpdate(Image image) {
        sessionFactory.getCurrentSession().saveOrUpdate(image);
        return image.getId();
    }

    @Override
    @Transactional
    public void deleteImage(Integer id) {
        Image image = findById(id);
        sessionFactory.getCurrentSession().delete(image);
    }

    @Override
    @Transactional
    public Image findById(Integer imageId) {
        String hql = "from Image i where i.id = :imageId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("imageId", imageId);
        query.setMaxResults(1);

        @SuppressWarnings("unchecked")
        Image itemList = (Image) query.uniqueResult();

        return itemList;
    }
}
