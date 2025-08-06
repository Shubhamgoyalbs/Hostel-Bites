package com.shubham.backend.service;

import com.shubham.backend.entity.AuthUserDetails;
import com.shubham.backend.entity.User;
import com.shubham.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public AuthUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepo.findByEmail(username);
            return new AuthUserDetails(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found with email: " + username, e);
        }
    }
}
