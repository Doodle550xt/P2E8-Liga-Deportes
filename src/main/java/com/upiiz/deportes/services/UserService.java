package com.upiiz.deportes.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.upiiz.deportes.entities.User;
import com.upiiz.deportes.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    public Optional<User> getUserByUsername(String username) {
        
        return userRepository.findByUsername(username);
    }

    
    public User saveUser(User user) {
        
        user.setPasswordCodificado(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
