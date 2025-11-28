package com.upiiz.deportes.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany; 
import jakarta.persistence.Table;

@Entity
@Table(name = "ligas")
public class Liga {

    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String pais;

    private String temporadaActual;

    @OneToMany(mappedBy = "liga", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Equipo> equipos;

    public Liga() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTemporadaActual() {
        return temporadaActual;
    }

    public void setTemporadaActual(String temporadaActual) {
        this.temporadaActual = temporadaActual;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public void addEquipo(Equipo equipo) {
        equipos.add(equipo);
    }
}