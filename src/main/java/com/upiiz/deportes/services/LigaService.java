package com.upiiz.deportes.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.upiiz.deportes.entities.Liga;
import com.upiiz.deportes.repositories.LigaRepository;

@Service
public class LigaService {

    private final LigaRepository ligaRepository;

    public LigaService(LigaRepository ligaRepository) {
        this.ligaRepository = ligaRepository;
    }

    public List<Liga> findAllLigas() {
        return ligaRepository.findAll();
    }

    public Liga saveLiga(Liga liga) {
        return ligaRepository.save(liga);
    }

    public Optional<Liga> findLigaById(Long id) {
        return ligaRepository.findById(id);
    }

    public Liga updateLiga(Liga liga){
        return ligaRepository.save(liga); 
    }

    public void deleteLiga(Long id)
    {
        ligaRepository.deleteById(id);
    }

}