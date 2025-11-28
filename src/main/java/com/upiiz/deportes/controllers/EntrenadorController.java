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

import com.upiiz.deportes.entities.Entrenador;
import com.upiiz.deportes.services.EntrenadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/entrenadores")
@Tag(name = "Entrenadores", description = "Gestión de la información de entrenadores de la liga deportiva.")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    public EntrenadorController(EntrenadorService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    
    @Operation(summary = "Obtener todos los entrenadores", description = "Recupera una lista con todos los entrenadores registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de entrenadores obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entrenador.class), examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "nombre": "Pep Guardiola",
                            "experiencia": 15,
                            "nacionalidad": "Española"
                        },
                        {
                            "id": 2,
                            "nombre": "Jürgen Klopp",
                            "experiencia": 20,
                            "nacionalidad": "Alemana"
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al obtener la lista de entrenadores" }
                    """)))
    })
    @GetMapping
    public ResponseEntity<?> getAllEntrenadores() {
        try {
            List<Entrenador> entrenadores = entrenadorService.findAllEntrenadors();
            
            return ResponseEntity.ok(entrenadores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al obtener la lista de entrenadores"));
        }
    }

    
    @Operation(summary = "Obtener entrenador por ID", description = "Devuelve los datos del entrenador correspondiente al ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entrenador.class))),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un entrenador con el ID proporcionado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al buscar el entrenador" }
                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getEntrenadorById(@PathVariable Long id) {
        try {
            Optional<Entrenador> entrenador = entrenadorService.findEntrenadorById(id);

            
            if (entrenador.isPresent()) {
                return ResponseEntity.ok(entrenador.get());
            }

            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "estado", "No encontrado",
                    "mensaje", "No se encontró un entrenador con el ID proporcionado"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al buscar el entrenador"));
        }
    }

    
    @Operation(summary = "Crear un nuevo entrenador", description = "Crea y registra un nuevo entrenador. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrenador creado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entrenador.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Petición inválida", "mensaje": "Faltan campos requeridos o hay datos incorrectos" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al crear el entrenador" }
                    """)))
    })
    @PostMapping
    public ResponseEntity<?> createEntrenador(@RequestBody Entrenador entrenador) {
        try {
            Entrenador nuevoEntrenador = entrenadorService.saveEntrenador(entrenador);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEntrenador);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (Exception e) {
                    System.out.print(e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al crear el entrenador"));

        }
    }

    
    @Operation(summary = "Actualizar un entrenador", description = "Actualiza los datos del entrenador especificado por ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador actualizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entrenador.class))),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un entrenador con el ID proporcionado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error inesperado al actualizar el entrenador" }
                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEntrenador(@PathVariable Long id, @RequestBody Entrenador entrenadorDetails) {
        try {
            if (!entrenadorService.findEntrenadorById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "estado", "No encontrado",
                        "mensaje", "No se encontró un entrenador con el ID proporcionado"));
            }

            entrenadorDetails.setId(id);
            Entrenador updatedEntrenador = entrenadorService.updateEntrenador(entrenadorDetails);
            return ResponseEntity.ok(updatedEntrenador);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error inesperado al actualizar el entrenador"));
        }
    }

    
    @Operation(summary = "Eliminar un entrenador", description = "Elimina un entrenador existente por su ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrenador eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un entrenador con el ID especificado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "No se pudo eliminar el entrenador" }
                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntrenador(@PathVariable Long id) {
        try {
            if (!entrenadorService.findEntrenadorById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "estado", "No encontrado",
                        "mensaje", "No se encontró un entrenador con el ID especificado"));
            }
            entrenadorService.deleteEntrenador(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "No se pudo eliminar el entrenador"));
        }
    }
}