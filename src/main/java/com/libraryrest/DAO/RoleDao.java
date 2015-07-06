package com.libraryrest.DAO;

import com.libraryrest.exceptions.DaoSystemException;
import com.libraryrest.exceptions.NoSuchEntityException;
import com.libraryrest.models.Role;

import java.util.List;

/**
 * @author Vladimir Martynyuk
 */
public interface RoleDao {

    public List<Role> findAll()  throws DaoSystemException;

    public Role findById(Long id) throws DaoSystemException, NoSuchEntityException;

    public void saveOrUpdate(Role role);

    public void remove(Role role);

    public void remove(Long id) throws DaoSystemException, NoSuchEntityException;

}