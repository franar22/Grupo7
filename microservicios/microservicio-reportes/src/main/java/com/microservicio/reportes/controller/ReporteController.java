package com.microservicio.reportes.controller;

import com.microservicio.reportes.model.Reporte;
import com.microservicio.reportes.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping
    public ResponseEntity<List<Reporte>> getAllReportes() {
        return ResponseEntity.ok(reporteService.findAll());
    }

    @Operation(summary = "Obtener un reporte por ID", description = "Devuelve un reporte específico según su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> getReporteById(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findById(id));
    }

    @Operation(summary = "Crear un nuevo reporte", description = "Crea un nuevo reporte en el sistema. Retorna el reporte creado.")
    @PostMapping
    public ResponseEntity<Reporte> createReporte(@Valid @RequestBody Reporte reporte) {
        Reporte savedReporte = reporteService.save(reporte);
        return new ResponseEntity<>(savedReporte, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un reporte existente", description = "Actualiza los datos de un reporte existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Reporte> updateReporte(@PathVariable Long id, @Valid @RequestBody Reporte reporte) {
        reporte.setId(id);
        return ResponseEntity.ok(reporteService.save(reporte));
    }

    @Operation(summary = "Eliminar un reporte por ID", description = "Elimina un reporte específico según su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReporte(@PathVariable Long id) {
        reporteService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener reportes por estado", description = "Devuelve una lista de reportes filtrados por estado (PENDIENTE, EN_REVISION, RESUELTO, RECHAZADO).")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reporte>> getReportesByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(reporteService.findByEstado(estado));
    }

    @Operation(summary = "Obtener reportes por usuario reportante", description = "Devuelve una lista de reportes realizados por un usuario específico.")
    @GetMapping("/usuario-reportante/{id}")
    public ResponseEntity<List<Reporte>> getReportesByUsuarioReportante(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findByIdUsuarioReportante(id));
    }

    @Operation(summary = "Obtener reportes por usuario reportado", description = "Devuelve una lista de reportes dirigidos a un usuario específico.")
    @GetMapping("/usuario-reportado/{id}")
    public ResponseEntity<List<Reporte>> getReportesByUsuarioReportado(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findByIdUsuarioReportado(id));
    }

    @Operation(summary = "Obtener reportes por foro", description = "Devuelve una lista de reportes relacionados con un foro específico.")
    @GetMapping("/foro/{id}")
    public ResponseEntity<List<Reporte>> getReportesByForo(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findByIdForo(id));
    }

    @Operation(summary = "Obtener reportes por categoría", description = "Devuelve una lista de reportes relacionados con una categoría específica.")
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<Reporte>> getReportesByCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findByIdCategoria(id));
    }

    @Operation(summary = "Obtener reportes por tipo", description = "Devuelve una lista de reportes de un tipo específico (SPAM, INAPPROPRIATE, HARASSMENT, etc.).")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> getReportesByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(reporteService.findByTipoReporte(tipo));
    }

    @Operation(summary = "Actualizar el estado de un reporte", description = "Actualiza el estado de un reporte específico.")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reporte> updateEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(reporteService.updateEstado(id, nuevoEstado));
    }
} 