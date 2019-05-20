package com.pingvin.autoservice.service;

import com.pingvin.autoservice.dao.UsersDAO;
import com.pingvin.autoservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersDAO usersDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersDAO.findByLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        String role = user.getRole();

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();

        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        grantList.add(authority);
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(), enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, grantList);

        return userDetails;
    }

}