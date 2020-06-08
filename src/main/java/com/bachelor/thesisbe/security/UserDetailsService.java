package com.bachelor.thesisbe.security;

import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = service.getUserByEmail(email);
        if (user != null) {
            if (user.getEmail().equals(email)) {
                return new User(email, user.getPassword(), new ArrayList<>());
            } else {
                throw new UsernameNotFoundException("Wrong password for email: " + email);
            }
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}
