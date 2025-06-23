package com.example.USER.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.USER.dto.LoginDTO;
import com.example.USER.dto.UsuarioDTO;
import com.example.USER.model.Usuarios;
import com.example.USER.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Listar todos los usuarios",description = "Devuelve una lista con todos los usuarios registrados en el sistema. Si no hay usuarios, retorna 204 No Content.")
    @GetMapping()
    public ResponseEntity<List<Usuarios>> listaDeUsuarios() {
        List<Usuarios> usuarios = userService.listarUsuarios();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Obtener usuario por ID",description = "Obtiene los datos de un usuario específico a partir de su ID. Si no existe, retorna 404.")
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long idUsuario) {
        try {
            Usuarios usuario = userService.obtenerUsuarioPorId(idUsuario);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @Operation(summary = "Crear nuevo usuario",description = "Crea un nuevo usuario a partir de los datos proporcionados. Verifica si el correo ya está registrado.")
    @PostMapping
    public ResponseEntity<?> crearUsuarioss(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        try {
            Usuarios usuario = new Usuarios();
            usuario.setIdRol(usuarioDTO.getIdRol());
            usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
            usuario.setCorreo(usuarioDTO.getCorreo());
            usuario.setPassword(usuarioDTO.getPassword());

            Usuarios usuarioGuardado = userService.guardarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("El correo ya está en uso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al crear el usuario: " + e.getMessage());
        }
    }

    @Operation(summary = "Login de los usuarios", description = "Verifica las credenciales del usuario (correo y contraseña) y devuelve un mensaje de éxito o error.")
    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Usuarios usuario = userService.obtenerUsuarioPorCorreo(loginDTO.getCorreo());

            if (passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
                return ResponseEntity.ok("Login exitoso");  
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }
    }

    @Operation(summary = "Actualizar información de usuario",description = "Actualiza los datos de un usuario específico utilizando su ID y los nuevos datos enviados.")
    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> actualizarInformacionUsuario(
        @PathVariable Long idUsuario, 
        @RequestBody @Valid Usuarios nuevaInfoUsuario) {
        try {
            Usuarios usuarioActualzado = userService.actualizarUsuario(idUsuario, nuevaInfoUsuario);
            return ResponseEntity.ok(usuarioActualzado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al actualizar el usuario: " + e.getMessage());
        }
    }


    @Operation(summary = "Eliminar usuario por ID",description = "Elimina un usuario del sistema usando su ID. Si no existe, retorna 404.")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<?> borrarUsuarioPorId(@PathVariable Long idUsuario) {
        try {
            String resultado = userService.borrarUsuario(idUsuario);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
