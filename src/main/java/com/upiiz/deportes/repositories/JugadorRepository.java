package com.upiiz.deportes.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upiiz.deportes.entities.Jugador;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    
    List<Jugador> findByEquipoId(Long equipoId);
    
    List<Jugador> findByPosicion(String posicion);
}