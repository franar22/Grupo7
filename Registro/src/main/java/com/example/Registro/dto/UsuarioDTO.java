package com.example.Registro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para transferir datos del usuario durante el registro.")
public class UsuarioDTO {

    @Schema(description = "ID del rol asignado al usuario", example = "2")
    private Long idRol;

    @Schema(description = "Nombre de usuario", example = "juan_perez")
    private String nombreUsuario;

    @Schema(description = "Correo electrónico del usuario", example = "juan@example.com")
    private String correo;

    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}
