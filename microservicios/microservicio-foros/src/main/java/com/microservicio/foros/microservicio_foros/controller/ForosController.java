package com.microservicio.foros.microservicio_foros.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.microservicio.foros.microservicio_foros.Model.Foros;
import com.microservicio.foros.microservicio_foros.Services.ForosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/foros")
public class ForosController {
@Autowired
    private ForosService forosService;

    @GetMapping()
    public ResponseEntity<List<Foros>> obtenerCategorias() {
        List<Foros> forosDisponibles = forosService.listarForos();
        if (forosDisponibles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(forosDisponibles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCategoriaPorId(@PathVariable Long id) {
        try {
            Foros foro = forosService.buscarForos(id);
            return ResponseEntity.ok(foro);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> crearForo(@RequestBody @Valid Foros foro) {
        try {
            Foros foroGuardado = forosService.guardarForo(foro);
            return ResponseEntity.status(HttpStatus.CREATED).body(foroGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al crear el foro: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(
        @PathVariable Long id,
        @RequestBody @Valid Foros foro) {
        try {
            Foros foroAct = forosService.actualizarForo(id, foro);
            return ResponseEntity.ok(foroAct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al actualizar el foro: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            String resultado = forosService.borrarForo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}")
public ResponseEntity<List<Foros>> obtenerForosPorUsuario(@PathVariable Long idUsuario) {
    List<Foros> foros = forosService.buscarForosPorUsuario(idUsuario);
    if (foros.isEmpty()) return ResponseEntity.noContent().build();
    return ResponseEntity.ok(foros);
}

@GetMapping("/categoria/{idCategoria}")
public ResponseEntity<List<Foros>> obtenerForosPorCategoria(@PathVariable Long idCategoria) {
    List<Foros> foros = forosService.buscarForosPorCategoria(idCategoria);
    if (foros.isEmpty()) return ResponseEntity.noContent().build();
    return ResponseEntity.ok(foros);
}

}
