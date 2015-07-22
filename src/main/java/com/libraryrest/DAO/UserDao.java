package com.libraryrest.DAO;

import org.springframework.security.access.annotation.Secured;
import com.libraryrest.models.User;

import java.util.List;

public interface UserDao {

    public List<User> findAll();

    public User findById(Integer id);

    public User findByName(String login);

    public Integer save(User user);

    public void update(User user);

    @Secured({"ROLE_ADMIN"})
    public void remove(User user);

    @Secured({"ROLE_ADMIN"})
    public void remove(Integer id);
}