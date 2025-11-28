package com.upiiz.deportes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upiiz.deportes.entities.Liga;

public interface LigaRepository extends JpaRepository<Liga, Long> {
    
    Liga findByNombre(String nombre);
}