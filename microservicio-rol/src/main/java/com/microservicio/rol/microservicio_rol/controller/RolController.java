package com.microservicio.rol.microservicio_rol.controller;

import com.microservicio.rol.microservicio_rol.clients.UsuarioClient;
import com.microservicio.rol.microservicio_rol.model.Rol;
import com.microservicio.rol.microservicio_rol.model.Usuarios;
import com.microservicio.rol.microservicio_rol.service.RolService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @Autowired
    private UsuarioClient usuarioClient;

    @Operation(summary = "Asignar un rol a un usuario")
    @PostMapping("/asignar/{idUsuario}")
    public ResponseEntity<?> asignarRolAUsuario(
            @PathVariable Long idUsuario,
            @RequestParam String tipoRol) {
        try {
           
            Usuarios usuario = usuarioClient.obtenerUsuarioPorId(idUsuario);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            
            Rol.TipoRol rolEnum = Rol.TipoRol.valueOf(tipoRol.toUpperCase());
            
            
            Rol rolAsignado = rolService.asignarRolAUsuario(idUsuario, rolEnum);
            return ResponseEntity.status(HttpStatus.CREATED).body(rolAsignado);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tipo de rol no válido. Opciones: ADMIN, MODERADOR, SOPORTE, GESTOR_ANUNCIOS, USUARIO");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar rol: " + e.getMessage());
        }
    }
    
    @Operation(summary = "Listar todos los roles")
    @GetMapping
    public ResponseEntity<List<Rol>> listarRoles() {
        List<Rol> roles = rolService.listarRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Obtener rol por ID")
    @GetMapping("/{idRol}")
    public ResponseEntity<?> obtenerRolPorId(@PathVariable Long idRol) {
        try {
            Rol rol = rolService.obtenerRolPorId(idRol);
            return ResponseEntity.ok(rol);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @Operation(summary = "Crear un nuevo rol")
    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody @Valid Rol nuevoRol) {
        try {
            Rol rolGuardado = rolService.guardarRol(nuevoRol);
            return ResponseEntity.status(HttpStatus.CREATED).body(rolGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al crear el rol: " + e.getMessage());
        }
    }
    
    @Operation(summary = "Actualizar un rol existente")
    @PutMapping("/{idRol}")
    public ResponseEntity<?> actualizarRol(@PathVariable Long idRol, @RequestBody @Valid Rol nuevaInfo) {
        try {
            Rol rolActualizado = rolService.actualizarRol(idRol, nuevaInfo);
            return ResponseEntity.ok(rolActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al actualizar el rol: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar un rol por ID")
    @DeleteMapping("/{idRol}")
    public ResponseEntity<?> borrarRol(@PathVariable Long idRol) {
        try {
            String mensaje = rolService.borrarRol(idRol);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

