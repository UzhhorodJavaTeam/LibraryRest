package com.libraryrest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.libraryrest.DAO.UserDao;
import com.libraryrest.enums.UserStatus;
import com.libraryrest.models.Role;
import com.libraryrest.models.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Vladimir Martynyuk
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userDao.findByName(userName);

        if (user != null) {

            String password = user.getPassword();
            boolean enabled = user.getStatus().equals(UserStatus.ACTIVE);
            boolean credentialsNonExpired = user.getStatus().equals(UserStatus.ACTIVE);
            boolean accountNonLocker = user.getStatus().equals(UserStatus.ACTIVE);
            boolean accountNonExpired = user.getStatus().equals(UserStatus.ACTIVE);

            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            for (Role role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName().toString()));
            }

            org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(userName, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocker, authorities);


            return securityUser;
        } else {
            throw new UsernameNotFoundException("User Not Found");
        }
    }


}
