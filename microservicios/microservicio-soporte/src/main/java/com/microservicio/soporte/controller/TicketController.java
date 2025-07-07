package com.microservicio.soporte.controller;

import com.microservicio.soporte.model.Ticket;
import com.microservicio.soporte.service.TicketService;

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
    @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Ticket creado correctamente", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el ticket", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        Ticket savedTicket = ticketService.save(ticket);
        return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener un ticket por ID", description = "Devuelve un ticket específico según su identificador único.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Ticket encontrado", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @Operation(summary = "Obtener todos los tickets", description = "Devuelve una lista con todos los tickets del sistema.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tickets encontrados", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "204", description = "No hay tickets registrados", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.findAll());
    }

    @Operation(summary = "Obtener tickets por usuario", description = "Devuelve una lista de tickets creados por un usuario específico.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tickets encontrados", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "204", description = "El usuario no tiene tickets", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Ticket>> getTicketsByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ticketService.findByUsuarioId(usuarioId));
    }

    @Operation(summary = "Obtener tickets por moderador", description = "Devuelve una lista de tickets asignados a un moderador específico.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tickets encontrados", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "204", description = "El moderador no tiene tickets asignados", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/moderador/{moderadorId}")
    public ResponseEntity<List<Ticket>> getTicketsByModeradorId(@PathVariable Long moderadorId) {
        return ResponseEntity.ok(ticketService.findByModeradorId(moderadorId));
    }

    @Operation(summary = "Obtener tickets por estado", description = "Devuelve una lista de tickets filtrados por estado.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tickets encontrados", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "204", description = "No hay tickets con ese estado", content = @Content),
    @ApiResponse(responseCode = "400", description = "Estado inválido", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Ticket>> getTicketsByEstado(@PathVariable Ticket.EstadoTicket estado) {
        return ResponseEntity.ok(ticketService.findByEstado(estado));
    }

    @Operation(summary = "Obtener tickets por categoría", description = "Devuelve una lista de tickets filtrados por categoría.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tickets encontrados", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "204", description = "No hay tickets en esta categoría", content = @Content),
    @ApiResponse(responseCode = "400", description = "Categoría inválida", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Ticket>> getTicketsByCategoria(@PathVariable Ticket.CategoriaTicket categoria) {
        return ResponseEntity.ok(ticketService.findByCategoria(categoria));
    }

    @Operation(summary = "Actualizar un ticket existente", description = "Actualiza los datos de un ticket existente.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Ticket actualizado correctamente", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar el ticket", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.update(id, ticket));
    }

    @Operation(summary = "Eliminar un ticket por ID", description = "Elimina un ticket específico según su identificador único. Retorna 204 No Content si se elimina correctamente.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Ticket eliminado correctamente"),
    @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asignar moderador a un ticket", description = "Asigna un moderador a un ticket específico y retorna el ticket actualizado.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Moderador asignado correctamente", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "404", description = "Ticket o moderador no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos para asignar moderador", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @PutMapping("/{ticketId}/asignar/{moderadorId}")
    public ResponseEntity<Ticket> asignarModerador(@PathVariable Long ticketId, @PathVariable Long moderadorId) {
        return ResponseEntity.ok(ticketService.asignarModerador(ticketId, moderadorId));
    }

    @Operation(summary = "Cambiar el estado de un ticket", description = "Cambia el estado de un ticket específico y retorna el ticket actualizado.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Estado del ticket actualizado correctamente", content = @Content(schema = @Schema(implementation = Ticket.class))),
    @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "400", description = "Estado inválido", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @PutMapping("/{ticketId}/estado/{estado}")
    public ResponseEntity<Ticket> cambiarEstado(@PathVariable Long ticketId, @PathVariable Ticket.EstadoTicket estado) {
        return ResponseEntity.ok(ticketService.cambiarEstado(ticketId, estado));
    }
} 