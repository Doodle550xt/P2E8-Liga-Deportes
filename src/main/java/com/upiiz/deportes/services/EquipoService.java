
package com.upiiz.deportes.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.upiiz.deportes.entities.Equipo;
import com.upiiz.deportes.repositories.EquipoRepository;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<Equipo> findAllEquipos() {
        return equipoRepository.findAll();
    }

    public Equipo saveEquipo(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    public Optional<Equipo> findEquipoById(Long id) {
        return equipoRepository.findById(id);
    }

    public Equipo updateEquipo(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    public void deleteEquipo(Long id) {
        equipoRepository.deleteById(id);
    }

}