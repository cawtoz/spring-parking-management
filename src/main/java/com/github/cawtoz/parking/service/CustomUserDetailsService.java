package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.CustomUserDetails;
import com.github.cawtoz.parking.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new AuthenticationServiceException("El usuario " + username + " no existe");
        }
        return new CustomUserDetails(user);
    }

}
