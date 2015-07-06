package com.libraryrest.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import com.libraryrest.DAO.UserDao;
import com.libraryrest.models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OnAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserDao userDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        User user = userDao.findByName(authentication.getName());

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("user", user);

        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
