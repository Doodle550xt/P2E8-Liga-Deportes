
package com.upiiz.deportes.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.upiiz.deportes.entities.Jugador;
import com.upiiz.deportes.repositories.JugadorRepository;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public List<Jugador> findAllJugadors() {
        return jugadorRepository.findAll();
    }

    public Jugador saveJugador(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    public Optional<Jugador> findJugadorById(Long id) {
        return jugadorRepository.findById(id);
    }

    public Jugador updateJugador(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    public void deleteJugador(Long id) {
        jugadorRepository.deleteById(id);
    }

}