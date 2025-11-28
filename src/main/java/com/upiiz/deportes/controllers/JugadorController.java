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

import com.upiiz.deportes.entities.Jugador;
import com.upiiz.deportes.services.JugadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/jugadores")
@Tag(name = "Jugadores", description = "Gestión de la información de los atletas y su asignación a equipos.")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @Operation(summary = "Obtener todos los jugadores", description = "Recupera una lista con todos los jugadores registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de jugadores obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Jugador.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al obtener la lista de jugadores" }
                    """)))
    })
    @GetMapping
    public ResponseEntity<?> getAllJugadores() {
        try {
            List<Jugador> jugadores = jugadorService.findAllJugadors();
            return ResponseEntity.ok(jugadores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al obtener la lista de jugadores"));
        }
    }

    @Operation(summary = "Obtener jugador por ID", description = "Devuelve los datos del jugador correspondiente al ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugador encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Jugador.class))),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un jugador con el ID proporcionado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al buscar el jugador" }
                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getJugadorById(@PathVariable Long id) {
        try {
            Optional<Jugador> jugador = jugadorService.findJugadorById(id);

            if (jugador.isPresent()) {
                return ResponseEntity.ok(jugador.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "estado", "No encontrado",
                    "mensaje", "No se encontró un jugador con el ID proporcionado"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al buscar el jugador"));
        }
    }

    @Operation(summary = "Crear un nuevo jugador", description = "Crea y asigna un nuevo jugador a un equipo usando el objeto Equipo con el ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jugador creado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Jugador.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, incompletos o equipo no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Petición inválida", "mensaje": "Se debe especificar el ID del Equipo al que pertenece el jugador." }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error al crear el jugador" }
                    """)))
    })
    @PostMapping
    public ResponseEntity<?> createJugador(@RequestBody Jugador jugador) {
        try {
            Jugador nuevoJugador = jugadorService.saveJugador(jugador);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJugador);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error al crear el jugador"));
        }
    }

    @Operation(summary = "Actualizar un jugador", description = "Actualiza los datos del jugador especificado por ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugador actualizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Jugador.class))),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un jugador con el ID proporcionado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "Error inesperado al actualizar el jugador" }
                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJugador(@PathVariable Long id, @RequestBody Jugador jugadorDetails) {
        try {
            if (!jugadorService.findJugadorById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "estado", "No encontrado",
                        "mensaje", "No se encontró un jugador con el ID proporcionado"));
            }

            jugadorDetails.setId(id);
            Jugador updatedJugador = jugadorService.updateJugador(jugadorDetails);
            return ResponseEntity.ok(updatedJugador);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "Petición inválida",
                    "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "Error inesperado al actualizar el jugador"));
        }
    }

    @Operation(summary = "Eliminar un jugador", description = "Elimina un jugador existente por su ID. Requiere autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Jugador eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "No encontrado", "mensaje": "No se encontró un jugador con el ID especificado" }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    { "estado": "Error", "mensaje": "No se pudo eliminar el jugador" }
                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJugador(@PathVariable Long id) {
        try {
            if (!jugadorService.findJugadorById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "estado", "No encontrado",
                        "mensaje", "No se encontró un jugador con el ID especificado"));
            }
            jugadorService.deleteJugador(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "estado", "Error",
                    "mensaje", "No se pudo eliminar el jugador"));
        }
    }
}