package com.libraryrest.controllers;

import com.libraryrest.DAO.UserDao;
import com.libraryrest.models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yura on 21.07.15.
 */
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    final Logger logger = LogManager.getLogger(getClass());

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable("userId") Integer userId) {
        return userDao.findById(userId);
    }

    @RequestMapping(value = "/users/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(Authentication authentication) {
        logger.info("GET: /users/user");
        return userDao.findByName(authentication.getName());
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable("userId") Integer userId) {
        logger.info("DELETE: /users/" + userId);
        userDao.remove(userId);
        return "Deleted Successfully";
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
    public User editUser(@PathVariable("userId") Integer userId, @RequestBody User user){
        logger.info("PUT: /users/"+userId);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        User u = userDao.findById(userId);
        if(user.getFirstName() != null){
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setEmail(user.getEmail());
        }
        if (user.getPassword()!= null){
            u.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userDao.update(u);
        return user;
    }
}
