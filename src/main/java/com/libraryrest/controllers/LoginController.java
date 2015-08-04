package com.libraryrest.controllers;


import com.libraryrest.DAO.RoleDao;
import com.libraryrest.DAO.UserDao;
import com.libraryrest.enums.UserRole;
import com.libraryrest.enums.UserStatus;
import com.libraryrest.mail.MailMail;
import com.libraryrest.models.Role;
import com.libraryrest.models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegisterPage(@RequestBody User user) throws Exception {
        logger.info("POST: /register");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        User newUser = user;
        if (roleDao.findById(1L) == null){
            Role aRole = new Role(UserRole.ROLE_ADMIN);
            roleDao.saveOrUpdate(aRole);
            Role uRole = new Role(UserRole.ROLE_USER);
            roleDao.saveOrUpdate(uRole);
        }
        Role userrole = roleDao.findById(2L);
        Set<Role> roles = new HashSet<Role>();
        roles.add(userrole);
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String confirmKey = UUID.randomUUID().toString();
        newUser.setConfirmKey(confirmKey);
        newUser.setStatus(UserStatus.INACTIVE);
        newUser.setRoles(roles);
        userDao.save(newUser);

        ApplicationContext context =
                new ClassPathXmlApplicationContext("Spring-Mail.xml");

        MailMail mm = (MailMail) context.getBean("mailMail");
        mm.sendMail("saddsafsda@gmail.com",
                newUser.getEmail(),
                "Confirm Email",
                "You are receiving this because you (or someone else) signed up for an npm user account with the username \"" + newUser.getFirstName() + "\".\n\n" +
                        "To confirm that this is the email address associated with that account, click the following link or paste it into your browser:" +
                        "\n\n http://localhost:8085/#/confirm/"+ confirmKey + "\n\n If you received this in error, you can safely ignore it.");

        return "Register successfully";
    }

    @RequestMapping(value = "/confirm/{confirmKey}", method = RequestMethod.GET)
    public String confirmRegister(@PathVariable("confirmKey") String confirmKey){
        User user = userDao.findByConfirmKey(confirmKey);
        if (user.getStatus()==UserStatus.INACTIVE){
            user.setStatus(UserStatus.ACTIVE);
            userDao.update(user);
            return "Thank you for registering!";
        }else{
            return "Account is already active!";
        }
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public String sendMailToConfirmChangePassword(@RequestBody User userWithEmail){
        User user = userDao.findByEmail(userWithEmail.getEmail());
        if (user!=null){
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("Spring-Mail.xml");

            MailMail mm = (MailMail) context.getBean("mailMail");
            mm.sendMail("saddsafsda@gmail.com",
                    user.getEmail(),
                    "Confirm Reset Password",
                    "You are receiving this because you (or someone else) requested the reset password of the \"" + user.getFirstName() + "\" user account.\n\n" +
                            "Please click on the following link, or paste this into your browser to complete the process:" +
                            "\n\n http://localhost:8085/#/forgotPassword/"+ user.getConfirmKey() + "\n\n If you received this in error, you can safely ignore it.");

            return "Please check your email!";
        }else{
            return "The user with the email does not exist!";
        }
    }

    @RequestMapping(value = "/forgotPassword/{confirmKey}", method = RequestMethod.GET)
    public String confirmChangePassword(@PathVariable("confirmKey") String confirmKey){
        User user = userDao.findByConfirmKey(confirmKey);

        return user.getLogin();
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestBody User userForChangePassword){
        User user = userDao.findByName(userForChangePassword.getLogin());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

        user.setPassword(bCryptPasswordEncoder.encode(userForChangePassword.getPassword()));
        user.setConfirmKey(UUID.randomUUID().toString());

        userDao.update(user);

        return "Password successfully changed";
    }
}


// hsadhdsajdsa@mail.com - qwertyuu11
// saddsafsda@gmail.com - qwertyuu