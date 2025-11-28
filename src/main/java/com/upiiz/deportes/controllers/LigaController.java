package com.upiiz.deportes.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upiiz.deportes.entities.Liga;
import com.upiiz.deportes.services.LigaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/ligas")
@Tag(name = "Ligas", description = "Gestión de las ligas y sus temporadas.")
public class LigaController {

    private final LigaService ligaService;

    public LigaController(LigaService ligaService) {
        this.ligaService = ligaService;
    }

    
    @Operation(summary = "Obtener todas las ligas", description = "Recupera una lista con todas las ligas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ligas obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Liga.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al obtener la lista de ligas" }
                    """)))
    })
    @GetMapping
    public ResponseEntity<?> getAllLigas() {
        try {
            List<Liga> ligas = ligaService.findAllLigas();
            return ResponseEntity.ok(ligas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al obtener la lista de ligas"));
        }
    }

    
    @Operation(summary = "Obtener liga por ID", description = "Devuelve los datos de la liga correspondiente al ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liga encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Liga.class))),
            @ApiResponse(responseCode = "404", description = "Liga no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró una liga con el ID proporcionado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al buscar la liga" }
                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getLigaById(@PathVariable Long id) {
        try {
            Optional<Liga> liga = ligaService.findLigaById(id);

            if (liga.isPresent()) {
                return ResponseEntity.ok(liga.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "estado", "No encontrado",
                    "mensaje", "No se encontró una liga con el ID proporcionado"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al buscar la liga"));
        }
    }

    @Operation(summary = "Crear una nueva liga", description = "Crea y registra una nueva liga. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Liga creada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Liga.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos (ej. nombre repetido)", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Petición inválida", "mensaje": "Faltan campos requeridos o la liga ya existe." }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al crear la liga" }
                    """)))
    })
    @PostMapping
    public ResponseEntity<?> createLiga(@RequestBody Liga liga) {
        try {
            Liga nuevaLiga = ligaService.saveLiga(liga);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaLiga);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al crear la liga"));
        }
    }

    
    @Operation(summary = "Actualizar una liga", description = "Actualiza los datos de la liga especificada por ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liga actualizada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Liga.class))),
            @ApiResponse(responseCode = "404", description = "Liga no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró una liga con el ID proporcionado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error inesperado al actualizar la liga" }
                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLiga(@PathVariable Long id, @RequestBody Liga ligaDetails) {
        try {
            if (!ligaService.findLigaById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "estado", "No encontrado",
                        "mensaje", "No se encontró una liga con el ID proporcionado"));
            }

            ligaDetails.setId(id);
            Liga updatedLiga = ligaService.updateLiga(ligaDetails);
            return ResponseEntity.ok(updatedLiga);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error inesperado al actualizar la liga"));
        }
    }

    
    @Operation(summary = "Eliminar una liga", description = "Elimina una liga existente por su ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Liga eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Liga no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró una liga con el ID especificado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "No se pudo eliminar la liga (Ej: Hay equipos asociados)" }
                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLiga(@PathVariable Long id) {
        try {
            if (!ligaService.findLigaById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "estado", "No encontrado",
                        "mensaje", "No se encontró una liga con el ID especificado"));
            }
            ligaService.deleteLiga(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "No se pudo eliminar la liga"));
        }
    }
}