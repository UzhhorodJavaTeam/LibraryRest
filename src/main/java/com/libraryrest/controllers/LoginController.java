package com.libraryrest.controllers;


import com.libraryrest.DAO.RoleDao;
import com.libraryrest.enums.UserRole;
import com.libraryrest.enums.UserStatus;
import com.libraryrest.models.Role;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.libraryrest.DAO.UserDao;
import com.libraryrest.models.User;

import java.util.HashSet;
import java.util.Set;


@RestController
public class LoginController {

    Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    @Qualifier("registrationFormValidator")
    private Validator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(ModelMap model) {
        logger.info("GET: /login");

        return "LoginPage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegisterPage(@RequestBody User user) throws Exception {
        logger.info("POST: /register");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        User newUser = user;
        System.out.println(roleDao.findById(2L));
        if(roleDao.findById(2L) == null){
            System.out.println(1);
            Role uRole = new Role(UserRole.ROLE_USER);
            Role aRole = new Role(UserRole.ROLE_ADMIN);
            System.out.println(2);
            roleDao.saveOrUpdate(aRole);
            roleDao.saveOrUpdate(uRole);
            System.out.println(3);
        }
        System.out.println(4);
        Role userrole = roleDao.findById(2L);
        Set<Role> roles = new HashSet<Role>();
        roles.add(userrole);
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setStatus(UserStatus.ACTIVE);
        newUser.setRoles(roles);
        userDao.save(newUser);
        return "Register successfully";
    }

}
