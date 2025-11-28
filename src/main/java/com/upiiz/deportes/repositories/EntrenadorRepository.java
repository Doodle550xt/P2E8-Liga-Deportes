package com.upiiz.deportes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upiiz.deportes.entities.Entrenador;

public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
}