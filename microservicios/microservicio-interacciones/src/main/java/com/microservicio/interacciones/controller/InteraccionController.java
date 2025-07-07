package com.microservicio.interacciones.controller;

import com.microservicio.interacciones.model.Interaccion;
import com.microservicio.interacciones.service.InteraccionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las interacciones de usuarios (likes/dislikes).
 * Proporciona endpoints para crear, consultar y eliminar interacciones,
 * así como para obtener estadísticas de interacciones por publicación o comentario.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/interacciones")
public class InteraccionController {
    @Autowired
    private InteraccionService interaccionService;

    @Operation(summary = "Obtener todas las interacciones", description = "Devuelve una lista con todas las interacciones del sistema. Si no hay interacciones, retorna 204 No Content.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interacciones encontradas", content = @Content(schema = @Schema(implementation = Interaccion.class))),
        @ApiResponse(responseCode = "204", description = "No hay interacciones", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<List<Interaccion>> getAll() {
        List<Interaccion> lista = interaccionService.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener una interacción por ID", description = "Devuelve una interacción específica según su identificador único. Si no existe, retorna 404 Not Found.")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interacción encontrada", content = @Content(schema = @Schema(implementation = Interaccion.class))),
        @ApiResponse(responseCode = "404", description = "Interacción no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(interaccionService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Crear una nueva interacción", description = "Crea una nueva interacción en el sistema. Retorna la interacción creada o 400 Bad Request si hay errores de validación.")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Interacción creada correctamente", content = @Content(schema = @Schema(implementation = Interaccion.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación al crear interacción", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid Interaccion interaccion) {
        try {
            Interaccion saved = interaccionService.save(interaccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al crear la interacción: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una interacción por ID", description = "Elimina una interacción específica según su identificador único. Retorna 204 No Content si se elimina correctamente o 404 Not Found si no existe.")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Interacción eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Interacción no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            interaccionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Contar likes de una publicación", description = "Devuelve el número de likes de una publicación específica.")
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad de likes obtenida", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("/publicacion/{publicacionId}/likes")
    public ResponseEntity<Long> countLikesByPublicacion(@PathVariable Long publicacionId) {
        return ResponseEntity.ok(interaccionService.countLikesByPublicacion(publicacionId));
    }

    @Operation(summary = "Contar dislikes de una publicación", description = "Devuelve el número de dislikes de una publicación específica.")
       @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad de dislikes obtenida", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("/publicacion/{publicacionId}/dislikes")
    public ResponseEntity<Long> countDislikesByPublicacion(@PathVariable Long publicacionId) {
        return ResponseEntity.ok(interaccionService.countDislikesByPublicacion(publicacionId));
    }

    @Operation(summary = "Contar likes de un comentario", description = "Devuelve el número de likes de un comentario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad de likes obtenida", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("/comentario/{comentarioId}/likes")
    public ResponseEntity<Long> countLikesByComentario(@PathVariable Long comentarioId) {
        return ResponseEntity.ok(interaccionService.countLikesByComentario(comentarioId));
    }

    @Operation(summary = "Contar dislikes de un comentario", description = "Devuelve el número de dislikes de un comentario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad de dislikes obtenida", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("/comentario/{comentarioId}/dislikes")
    public ResponseEntity<Long> countDislikesByComentario(@PathVariable Long comentarioId) {
        return ResponseEntity.ok(interaccionService.countDislikesByComentario(comentarioId));
    }

    @Operation(summary = "Obtener interacciones por publicación", description = "Devuelve una lista de interacciones asociadas a una publicación específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interacciones encontradas", content = @Content(schema = @Schema(implementation = Interaccion.class)))
    })
    @GetMapping("/publicacion/{publicacionId}")
    public ResponseEntity<List<Interaccion>> getByPublicacion(@PathVariable Long publicacionId) {
        return ResponseEntity.ok(interaccionService.findByPublicacionId(publicacionId));
    }

    @Operation(summary = "Obtener interacciones por comentario", description = "Devuelve una lista de interacciones asociadas a un comentario específico.")
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interacciones encontradas", content = @Content(schema = @Schema(implementation = Interaccion.class)))
    })
    @GetMapping("/comentario/{comentarioId}")
    public ResponseEntity<List<Interaccion>> getByComentario(@PathVariable Long comentarioId) {
        return ResponseEntity.ok(interaccionService.findByComentarioId(comentarioId));
    }

    @Operation(summary = "Obtener interacciones por usuario", description = "Devuelve una lista de interacciones realizadas por un usuario específico. Si no tiene interacciones, retorna 204 No Content.")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interacciones encontradas", content = @Content(schema = @Schema(implementation = Interaccion.class))),
        @ApiResponse(responseCode = "204", description = "No hay interacciones para este usuario", content = @Content)
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Interaccion>> getByUsuario(@PathVariable Long usuarioId) {
        List<Interaccion> interacciones = interaccionService.findByUsuarioId(usuarioId);
        if (interacciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(interacciones);
    }
} 