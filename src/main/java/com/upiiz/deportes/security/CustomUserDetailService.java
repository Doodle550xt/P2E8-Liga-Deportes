package com.upiiz.deportes.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.upiiz.deportes.services.UserService;

@Service
public class CustomUserDetailService implements UserDetailsService {

    
    final UserService userService;

    
    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        return userService.getUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(username+" not found"));
    }
}
