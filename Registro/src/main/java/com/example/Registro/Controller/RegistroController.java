package com.example.Registro.Controller;

import com.example.Registro.Service.RegistroService;
import com.example.Registro.dto.UsuarioDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {
    @Autowired
    private RegistroService registroService;
    
    @Operation(summary = "Registrar un nuevo usuario",description = "Permite registrar un nuevo usuario con los datos necesarios. Devuelve el usuario registrado si es exitoso.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos para el registro", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error al registrar el usuario", content = @Content)
})
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioDTO usuario) {
        try {
            UsuarioDTO nuevoRegistro = registroService.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al registrar: " + e.getMessage());
        }
    }
}