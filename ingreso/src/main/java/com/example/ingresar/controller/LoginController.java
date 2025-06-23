package com.example.ingresar.controller;

import com.example.ingresar.dto.LoginDTO;
import com.example.ingresar.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Autenticar un usuario con credenciales",description = "Recibe un correo y una contraseña. Devuelve un mensaje indicando si la autenticación fue exitosa o si las credenciales son invalidas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public Mono<ResponseEntity<String>> login(@RequestBody LoginDTO loginDTO) {
        return loginService.iniciarSesion(loginDTO)
                .map(autenticado -> {
                    if (autenticado) {
                        return ResponseEntity.ok("Autenticación exitosa");
                    } else {
                        return ResponseEntity.status(401).body("Credenciales inválidas");
                    }
                });
    }
}
