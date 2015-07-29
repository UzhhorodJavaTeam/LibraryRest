package com.libraryrest.controllers;

import com.libraryrest.DAO.UserDao;
import com.libraryrest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yura on 21.07.15.
 */
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @RequestMapping(value = "/users/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(Authentication authentication) {
        return userDao.findByName(authentication.getName());
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable("userId") Integer userId){
        userDao.remove(userId);

        return "Deleted Successfully";
    }
}
