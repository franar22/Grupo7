package com.microservicio.anuncios.controller;

import com.microservicio.anuncios.model.Anuncio;
import com.microservicio.anuncios.service.AnuncioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
 * Controlador REST para gestionar los anuncios del sistema de foros.
 * Proporciona endpoints para crear, consultar y eliminar anuncios,
 * así como para filtrar anuncios por foro o publicación específica.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/anuncios")
public class AnuncioController {
    @Autowired
    private AnuncioService anuncioService;

    @Operation(summary = "Obtener todos los anuncios", description = "Devuelve una lista con todos los anuncios del sistema. Si no hay anuncios, retorna 204 No Content.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de anuncios", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Anuncio.class)))),
    @ApiResponse(responseCode = "204", description = "No hay anuncios disponibles"),
    @ApiResponse(responseCode = "500", description = "Error al obtener los anuncios", content = @Content)
})
    @GetMapping
    public ResponseEntity<List<Anuncio>> getAll() {
        List<Anuncio> lista = anuncioService.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener un anuncio por ID", description = "Devuelve un anuncio específico según su identificador único. Si no existe, retorna 404 Not Found.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Anuncio encontrado", content = @Content(schema = @Schema(implementation = Anuncio.class))),
    @ApiResponse(responseCode = "404", description = "Anuncio no encontrado"),
    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
})
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(anuncioService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Crear un nuevo anuncio", description = "Crea un nuevo anuncio en el sistema. Retorna el anuncio creado o 400 Bad Request si hay errores de validación.")
    @ApiResponses(value = {
    @ApiResponse(
        responseCode = "201",
        description = "Anuncio creado exitosamente",
        content = @Content(schema = @Schema(implementation = Anuncio.class))
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud inválida - Error de validación o datos incorrectos",
        content = @Content(schema = @Schema(implementation = String.class))
    ),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(schema = @Schema(implementation = String.class))
    )
})
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid Anuncio anuncio) {
        try {
            Anuncio saved = anuncioService.save(anuncio);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al crear el anuncio: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar un anuncio por ID", description = "Elimina un anuncio específico según su identificador único. Retorna 204 No Content si se elimina correctamente o 404 Not Found si no existe.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Anuncio eliminado correctamente"),
    @ApiResponse(responseCode = "404", description = "Anuncio no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno al eliminar el anuncio", content = @Content(schema = @Schema(implementation = String.class)))
})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            anuncioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener anuncios por foro", description = "Devuelve una lista de anuncios asociados a un foro específico.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Anuncios encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Anuncio.class)))),
    @ApiResponse(responseCode = "204", description = "No hay anuncios para este foro"),
    @ApiResponse(responseCode = "404", description = "Foro no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno al obtener anuncios", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/foro/{foroId}")
    public ResponseEntity<List<Anuncio>> getByForo(@PathVariable Long foroId) {
        return ResponseEntity.ok(anuncioService.findByForoId(foroId));
    }

    @Operation(summary = "Obtener anuncios por publicación", description = "Devuelve una lista de anuncios asociados a una publicación específica.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Anuncios encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Anuncio.class)))),
    @ApiResponse(responseCode = "204", description = "No hay anuncios para esta publicación"),
    @ApiResponse(responseCode = "404", description = "Publicación no encontrada", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno al obtener anuncios", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/publicacion/{publicacionId}")
    public ResponseEntity<List<Anuncio>> getByPublicacion(@PathVariable Long publicacionId) {
        return ResponseEntity.ok(anuncioService.findByPublicacionId(publicacionId));
    }
} 