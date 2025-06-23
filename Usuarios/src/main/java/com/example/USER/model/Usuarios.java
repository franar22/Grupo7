package com.example.USER.model;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a los usuarios registrados en el sistema.")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "5")
    private Long id;

    @NotNull(message = "El id del rol no puede estar vacío")
    @Schema(description = "ID del rol asociado al usuario", example = "1")
    private Long idRol;

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 3, max = 60)
    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 100)
    @Schema(description = "Nombre único del usuario", example = "fran.arg")
    private String nombreUsuario;

    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "El correo debe tener un formato válido.")
    @Column(name = "correo", nullable = false, unique = true)
    @Schema(description = "Correo electrónico del usuario", example = "fran@example.com")
    private String correo;

    @Size(min = 3, max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    @Schema(description = "Contraseña del usuario se encriptara", example = "********")
    private String password;

    @Column(name = "fecha_creacion", updatable = false)
    @Schema(description = "Fecha de creación del usuario", example = "2025-06-20T12:00:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Nombre real del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Pérez López")
    private String apellidos;

    @PrePersist
    protected void inicializacionDatos() {
        this.fechaCreacion = LocalDateTime.now();
        this.nombre = null;
        this.apellidos = null;
    }
}



