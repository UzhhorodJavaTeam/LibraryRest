package com.libraryrest.controllers;

import com.libraryrest.DAO.UserDao;
import com.libraryrest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yura on 21.07.15.
 */
@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(Authentication authentication) {
        return userDao.findByName(authentication.getName());
    }
}
