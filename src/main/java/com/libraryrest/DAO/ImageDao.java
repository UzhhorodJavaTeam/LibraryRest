package com.libraryrest.DAO;

import com.libraryrest.models.Image;

/**
 * Created by superuser on 06.07.15.
 */
public interface ImageDao {

    public Image findById(Integer bookId);

    public Integer saveOrUpdate(Image book);

    public void deleteImage(Integer id);

}
