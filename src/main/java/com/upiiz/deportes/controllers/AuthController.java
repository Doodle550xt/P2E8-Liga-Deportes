package com.upiiz.deportes.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upiiz.deportes.entities.User;
import com.upiiz.deportes.services.JwtService;
import com.upiiz.deportes.services.UserService;


@RestController

@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody User user) {
        
        if(userService.getUserByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(409).body(
                    Map.of("estado",0,
                            "mensaje", "El usuario ya existe")
            );
        }else 
            return ResponseEntity.status(201).body(
                    Map.of("estado",1,
                            "mensaje", "Usuario registrado",
                            "usuario", userService.saveUser(user))
            );
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User usuario = userService.getUserByUsername(user.getUsername()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body(
                    Map.of("estado",0,
                            "mensaje","Usuario no encontrado"
            ));
        }else
            return ResponseEntity.status(200).body(
                    Map.of("estado",1,
                            "mensaje","Usuario logueado",
                            "token", jwtService.generateJwtToken(usuario.getUsername()))
            );
    }
}
