package com.microservicio.soporte.controller;

import com.microservicio.soporte.model.Respuesta;
import com.microservicio.soporte.service.RespuestaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @Operation(summary = "Crear una nueva respuesta de soporte", description = "Crea una nueva respuesta en el sistema. Retorna la respuesta creada y código 201 Created.")
    @PostMapping
    public ResponseEntity<Respuesta> createRespuesta(@Valid @RequestBody Respuesta respuesta) {
        Respuesta savedRespuesta = respuestaService.save(respuesta);
        return new ResponseEntity<>(savedRespuesta, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener una respuesta por ID", description = "Devuelve una respuesta específica según su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<Respuesta> getRespuestaById(@PathVariable Long id) {
        return ResponseEntity.ok(respuestaService.findById(id));
    }

    @Operation(summary = "Obtener todas las respuestas", description = "Devuelve una lista con todas las respuestas del sistema.")
    @GetMapping
    public ResponseEntity<List<Respuesta>> getAllRespuestas() {
        return ResponseEntity.ok(respuestaService.findAll());
    }

    @Operation(summary = "Obtener respuestas por ticket", description = "Devuelve una lista de respuestas asociadas a un ticket específico.")
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<Respuesta>> getRespuestasByTicketId(@PathVariable Long ticketId) {
        return ResponseEntity.ok(respuestaService.findByTicketId(ticketId));
    }

    @Operation(summary = "Obtener respuestas por usuario", description = "Devuelve una lista de respuestas realizadas por un usuario específico.")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Respuesta>> getRespuestasByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(respuestaService.findByUsuarioId(usuarioId));
    }

    @Operation(summary = "Actualizar una respuesta existente", description = "Actualiza los datos de una respuesta existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Respuesta> updateRespuesta(@PathVariable Long id, @Valid @RequestBody Respuesta respuesta) {
        return ResponseEntity.ok(respuestaService.update(id, respuesta));
    }

    @Operation(summary = "Eliminar una respuesta por ID", description = "Elimina una respuesta específica según su identificador único. Retorna 204 No Content si se elimina correctamente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRespuesta(@PathVariable Long id) {
        respuestaService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 