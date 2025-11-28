package com.upiiz.deportes.services;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtService {
    
    private int TIEMPO_PARA_EXPIRAR=60*60*24*1000;
    private String LLAVE_SECRETA = "Mi_llave_muy_muy_secreta_que_no_la_debo_de_compartir";

    
    
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TIEMPO_PARA_EXPIRAR))
                .signWith(obtenerLLave(), SignatureAlgorithm.HS256)
                .compact(); 
    }
    
    public String obtenerNombreUsuario(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerLLave())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); 
    }
    
    
    
    
    public boolean tokenValido(String userName, String token) {
        return obtenerNombreUsuario(token).equals(userName) && !estaExpirado(token);
    }
    
    public boolean estaExpirado(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerLLave())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
    
    private Key obtenerLLave() {
        return Keys.hmacShaKeyFor(LLAVE_SECRETA.getBytes());
    }

}
