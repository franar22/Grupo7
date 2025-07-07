package com.microservicio.reportes.controller;

import com.microservicio.reportes.model.Reporte;
import com.microservicio.reportes.service.ReporteService;

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
 * Controlador REST para gestionar los reportes del sistema de foros.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar reportes,
 * así como para filtrar reportes por diferentes criterios como estado, usuario, foro, etc.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Operation(summary = "Obtener todos los reportes", description = "Devuelve una lista con todos los reportes del sistema.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "204", description = "No hay reportes", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping
    public ResponseEntity<List<Reporte>> getAllReportes() {
        return ResponseEntity.ok(reporteService.findAll());
    }

    @Operation(summary = "Obtener un reporte por ID", description = "Devuelve un reporte específico según su identificador único.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reporte encontrado", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> getReporteById(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findById(id));
    }

    @Operation(summary = "Crear un nuevo reporte", description = "Crea un nuevo reporte en el sistema. Retorna el reporte creado.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Reporte creado correctamente", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "400", description = "Error de validación en el reporte", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @PostMapping
    public ResponseEntity<Reporte> createReporte(@Valid @RequestBody Reporte reporte) {
        Reporte savedReporte = reporteService.save(reporte);
        return new ResponseEntity<>(savedReporte, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un reporte existente", description = "Actualiza los datos de un reporte existente.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reporte actualizado correctamente", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar el reporte", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @PutMapping("/{id}")
    public ResponseEntity<Reporte> updateReporte(@PathVariable Long id, @Valid @RequestBody Reporte reporte) {
        reporte.setId(id);
        return ResponseEntity.ok(reporteService.save(reporte));
    }

    @Operation(summary = "Eliminar un reporte por ID", description = "Elimina un reporte específico según su identificador único.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reporte eliminado correctamente"),
    @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReporte(@PathVariable Long id) {
        reporteService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener reportes por estado", description = "Devuelve una lista de reportes filtrados por estado (PENDIENTE, EN_REVISION, RESUELTO, RECHAZADO).")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "204", description = "No hay reportes con ese estado", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reporte>> getReportesByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(reporteService.findByEstado(estado));
    }

    @Operation(summary = "Obtener reportes por usuario reportante", description = "Devuelve una lista de reportes realizados por un usuario específico.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "204", description = "El usuario no ha realizado reportes", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/usuario-reportante/{id}")
    public ResponseEntity<List<Reporte>> getReportesByUsuarioReportante(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findByIdUsuarioReportante(id));
    }

    @Operation(summary = "Obtener reportes por usuario reportado", description = "Devuelve una lista de reportes dirigidos a un usuario específico.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "204", description = "No se han realizado reportes hacia este usuario", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/usuario-reportado/{id}")
    public ResponseEntity<List<Reporte>> getReportesByUsuarioReportado(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findByIdUsuarioReportado(id));
    }

    @Operation(summary = "Obtener reportes por foro", description = "Devuelve una lista de reportes relacionados con un foro específico.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "204", description = "No hay reportes en este foro", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/foro/{id}")
    public ResponseEntity<List<Reporte>> getReportesByForo(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findByIdForo(id));
    }

    @Operation(summary = "Obtener reportes por categoría", description = "Devuelve una lista de reportes relacionados con una categoría específica.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "204", description = "No hay reportes en esta categoría", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<Reporte>> getReportesByCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findByIdCategoria(id));
    }

    @Operation(summary = "Obtener reportes por tipo", description = "Devuelve una lista de reportes de un tipo específico (SPAM, INAPPROPRIATE, HARASSMENT, etc.).")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "204", description = "No hay reportes de este tipo", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> getReportesByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(reporteService.findByTipoReporte(tipo));
    }

    @Operation(summary = "Actualizar el estado de un reporte", description = "Actualiza el estado de un reporte específico.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Estado del reporte actualizado correctamente", content = @Content(schema = @Schema(implementation = Reporte.class))),
    @ApiResponse(responseCode = "400", description = "Estado inválido o datos erróneos", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reporte> updateEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(reporteService.updateEstado(id, nuevoEstado));
    }
} 