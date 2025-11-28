package com.upiiz.deportes.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.upiiz.deportes.entities.Entrenador;
import com.upiiz.deportes.repositories.EntrenadorRepository;

@Service
public class EntrenadorService {
    
    private final EntrenadorRepository entrenadorRepository;

    public EntrenadorService(EntrenadorRepository entrenadorRepository) {
        this.entrenadorRepository = entrenadorRepository;
    }

    public List<Entrenador> findAllEntrenadors() {
        return entrenadorRepository.findAll();
    }

    public Entrenador saveEntrenador(Entrenador entrenador) {
        return entrenadorRepository.save(entrenador);
    }

    public Optional<Entrenador> findEntrenadorById(Long id) {
        return entrenadorRepository.findById(id);
    }

    public Entrenador updateEntrenador(Entrenador entrenador) {
        return entrenadorRepository.save(entrenador);
    }

    public void deleteEntrenador(Long id) {
        entrenadorRepository.deleteById(id);
    }
}
