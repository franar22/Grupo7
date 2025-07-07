package com.example.ingresar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para manejar los datos de inicio de sesión del usuario.")
public class LoginDTO {

    @Schema(description = "Correo electrónico del usuario", example = "juan@example.com")
    private String correo;

    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}
