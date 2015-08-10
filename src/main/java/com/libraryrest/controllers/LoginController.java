package com.libraryrest.controllers;


import com.libraryrest.DAO.RoleDao;
import com.libraryrest.DAO.UserDao;
import com.libraryrest.enums.UserRole;
import com.libraryrest.enums.UserStatus;
import com.libraryrest.mail.Mailer;
import com.libraryrest.models.Role;
import com.libraryrest.models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class LoginController {

    Logger logger = LogManager.getLogger(LoginController.class);

    private static final Integer MILLISECONDS_IN_DAY = 86400000;

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
    public User postRegisterPage(@RequestBody User user) throws Exception {
        logger.info("POST: /register");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        Properties prop = new Properties();
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
        newUser.setConfirmEmailToken(confirmKey);
        newUser.setStatus(UserStatus.INACTIVE);
        newUser.setRoles(roles);
        userDao.save(newUser);

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mail.xml");
        prop.load(getClass().getClassLoader().getResourceAsStream("mail.properties"));
        Mailer mailer = (Mailer) context.getBean("mailer");
        mailer.sendMail(newUser, prop.getProperty("mail.username"), prop.getProperty("mail.confirmRegistrationSubject"), prop.getProperty("mail.confirmRegistrationTemplate"));

        return newUser;
    }

    @RequestMapping(value = "/confirm/{confirmEmailToken}", method = RequestMethod.GET)
    public String confirmRegister(@PathVariable("confirmEmailToken") String confirmEmailToken){
        User user = userDao.findByConfirmEmailToken(confirmEmailToken);
        if (user.getStatus()==UserStatus.INACTIVE){
            user.setStatus(UserStatus.ACTIVE);
            userDao.update(user);
            return "Thank you for registering!";
        }else{
            return "Account is already active!";
        }
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public ResponseEntity<String> sendMailToConfirmChangePassword(@RequestBody User userWithEmail) {
        try {
            Properties prop = new Properties();
            Date currentDate = new Date();
            User user = userDao.findByEmail(userWithEmail.getEmail());
            user.setConfirmResetPasswordToken(UUID.randomUUID().toString());
            user.setResetPasswordTokenCreationDate(currentDate);
            userDao.update(user);
            if (user != null) {
                ApplicationContext context = new ClassPathXmlApplicationContext("spring-mail.xml");
                prop.load(getClass().getClassLoader().getResourceAsStream("mail.properties"));
                Mailer mailer = (Mailer) context.getBean("mailer");
                mailer.sendMail(user, prop.getProperty("mail.username"), prop.getProperty("mail.confirmResetPasswordSubject"), prop.getProperty("mail.confirmResetPasswordTemplate"));

                return new ResponseEntity<String>("Please check your email!", null, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("The user with the email does not exist!", null, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getStackTrace().toString(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/resetPassword/{confirmResetPasswordToken}", method = RequestMethod.POST)
    public ResponseEntity<String> resetPassword(@PathVariable("confirmResetPasswordToken") String confirmResetPasswordToken, @RequestBody User userWithNewPassword){
            User user = userDao.findByConfirmResetPasswordToken(confirmResetPasswordToken);
            Date dateCreationToken = user.getResetPasswordTokenCreationDate();
            Date currentDate = new Date();
            Integer dayAgoOrNo = (int) (currentDate.getTime()-dateCreationToken.getTime())/MILLISECONDS_IN_DAY;

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

            if (dayAgoOrNo<1) {
                user.setPassword(bCryptPasswordEncoder.encode(userWithNewPassword.getPassword()));

                userDao.update(user);

                return new ResponseEntity<String>("Password successfully changed", null, HttpStatus.OK);
            }else{
                return new ResponseEntity<String>("Token is no longer valid, please try again from the beginning.", null, HttpStatus.BAD_REQUEST);
            }
    }
}