package com.libraryrest.DAO;

import com.libraryrest.exceptions.DaoSystemException;
import com.libraryrest.exceptions.NoSuchEntityException;
import com.libraryrest.models.Role;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface RoleDao {

    public List<Role> findAll()  throws DaoSystemException;

    public Role findById(Long id) throws DaoSystemException, NoSuchEntityException;

    public void saveOrUpdate(Role role);

    @Secured({"ROLE_ADMIN"})
    public void remove(Role role);

    @Secured({"ROLE_ADMIN"})
    public void remove(Long id) throws DaoSystemException, NoSuchEntityException;

}