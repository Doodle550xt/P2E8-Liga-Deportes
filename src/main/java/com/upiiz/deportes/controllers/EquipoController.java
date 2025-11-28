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

import com.upiiz.deportes.entities.Equipo;
import com.upiiz.deportes.services.EquipoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/equipos")
@Tag(name = "Equipos", description = "Gestión de la información de los equipos de la liga deportiva.")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    
    @Operation(summary = "Obtener todos los equipos", description = "Recupera una lista con todos los equipos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de equipos obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al obtener la lista de equipos" }
                    """)))
    })
    @GetMapping
    public ResponseEntity<?> getAllEquipos() {
        try {
            List<Equipo> equipos = equipoService.findAllEquipos();
            return ResponseEntity.ok(equipos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al obtener la lista de equipos"));
        }
    }

    
    @Operation(summary = "Obtener equipo por ID", description = "Devuelve los datos del equipo correspondiente al ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un equipo con el ID proporcionado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al buscar el equipo" }
                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipoById(@PathVariable Long id) {
        try {
            Optional<Equipo> equipo = equipoService.findEquipoById(id);

            if (equipo.isPresent()) {
                return ResponseEntity.ok(equipo.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "estado", "No encontrado",
                    "mensaje", "No se encontró un equipo con el ID proporcionado"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al buscar el equipo"));
        }
    }

    
    @Operation(summary = "Crear un nuevo equipo", description = "Crea y registra un nuevo equipo. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipo creado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, incompletos o liga/entrenador no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Petición inválida", "mensaje": "Faltan campos requeridos o la liga/entrenador asignado no existe." }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al crear el equipo" }
                    """)))
    })
    @PostMapping
    public ResponseEntity<?> createEquipo(@RequestBody Equipo equipo) {
        try {
            
            
            
            Equipo nuevoEquipo = equipoService.saveEquipo(equipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEquipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al crear el entrenador"));
        }
    }

    
    @Operation(summary = "Actualizar un equipo", description = "Actualiza los datos del equipo especificado por ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo actualizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un equipo con el ID proporcionado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error inesperado al actualizar el equipo" }
                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEquipo(@PathVariable Long id, @RequestBody Equipo equipoDetails) {
        try {
            if (!equipoService.findEquipoById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "estado", "No encontrado",
                        "mensaje", "No se encontró un equipo con el ID proporcionado"));
            }

            equipoDetails.setId(id);
            Equipo updatedEquipo = equipoService.updateEquipo(equipoDetails);
            return ResponseEntity.ok(updatedEquipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al actualizar el entrenador"));
        }
    }

    
    @Operation(summary = "Eliminar un equipo", description = "Elimina un equipo existente por su ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un equipo con el ID especificado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "No se pudo eliminar el equipo (Ej: Hay jugadores asociados)" }
                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipo(@PathVariable Long id) {
        try {
            if (!equipoService.findEquipoById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "estado", "No encontrado",
                        "mensaje", "No se encontró un equipo con el ID especificado"));
            }
            equipoService.deleteEquipo(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "No se pudo eliminar el equipo"));
        }
    }
}