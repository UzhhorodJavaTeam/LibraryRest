package com.libraryrest.DAO;

import com.libraryrest.models.User;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface UserDao {

    public List<User> findAll();

    public User findById(Integer id);

    public User findByName(String login);

    public User findByConfirmEmailToken(String confirmEmailToken);

    public User findByConfirmResetPasswordToken(String confirmResetPasswordToken);

    public User findByEmail(String email);

    public void save(User user);

    public void update(User user);

    @Secured({"ROLE_ADMIN"})
    public void remove(User user);

    @Secured({"ROLE_ADMIN"})
    public void remove(Integer id);
}