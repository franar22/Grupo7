package com.microservicio.soporte.controller;

import com.microservicio.soporte.model.Ticket;
import com.microservicio.soporte.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar los tickets de soporte del sistema de foros.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar tickets,
 * así como para filtrar tickets por diferentes criterios y gestionar su ciclo de vida.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Operation(summary = "Crear un nuevo ticket de soporte", description = "Crea un nuevo ticket de soporte en el sistema. Retorna el ticket creado y código 201 Created.")
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        Ticket savedTicket = ticketService.save(ticket);
        return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener un ticket por ID", description = "Devuelve un ticket específico según su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @Operation(summary = "Obtener todos los tickets", description = "Devuelve una lista con todos los tickets del sistema.")
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.findAll());
    }

    @Operation(summary = "Obtener tickets por usuario", description = "Devuelve una lista de tickets creados por un usuario específico.")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Ticket>> getTicketsByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ticketService.findByUsuarioId(usuarioId));
    }

    @Operation(summary = "Obtener tickets por moderador", description = "Devuelve una lista de tickets asignados a un moderador específico.")
    @GetMapping("/moderador/{moderadorId}")
    public ResponseEntity<List<Ticket>> getTicketsByModeradorId(@PathVariable Long moderadorId) {
        return ResponseEntity.ok(ticketService.findByModeradorId(moderadorId));
    }

    @Operation(summary = "Obtener tickets por estado", description = "Devuelve una lista de tickets filtrados por estado.")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Ticket>> getTicketsByEstado(@PathVariable Ticket.EstadoTicket estado) {
        return ResponseEntity.ok(ticketService.findByEstado(estado));
    }

    @Operation(summary = "Obtener tickets por categoría", description = "Devuelve una lista de tickets filtrados por categoría.")
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Ticket>> getTicketsByCategoria(@PathVariable Ticket.CategoriaTicket categoria) {
        return ResponseEntity.ok(ticketService.findByCategoria(categoria));
    }

    @Operation(summary = "Actualizar un ticket existente", description = "Actualiza los datos de un ticket existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.update(id, ticket));
    }

    @Operation(summary = "Eliminar un ticket por ID", description = "Elimina un ticket específico según su identificador único. Retorna 204 No Content si se elimina correctamente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asignar moderador a un ticket", description = "Asigna un moderador a un ticket específico y retorna el ticket actualizado.")
    @PutMapping("/{ticketId}/asignar/{moderadorId}")
    public ResponseEntity<Ticket> asignarModerador(@PathVariable Long ticketId, @PathVariable Long moderadorId) {
        return ResponseEntity.ok(ticketService.asignarModerador(ticketId, moderadorId));
    }

    @Operation(summary = "Cambiar el estado de un ticket", description = "Cambia el estado de un ticket específico y retorna el ticket actualizado.")
    @PutMapping("/{ticketId}/estado/{estado}")
    public ResponseEntity<Ticket> cambiarEstado(@PathVariable Long ticketId, @PathVariable Ticket.EstadoTicket estado) {
        return ResponseEntity.ok(ticketService.cambiarEstado(ticketId, estado));
    }
} 