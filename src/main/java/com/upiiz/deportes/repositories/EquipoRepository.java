package com.upiiz.deportes.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upiiz.deportes.entities.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    
    Optional<Equipo> findByNombre(String nombre);
    
}