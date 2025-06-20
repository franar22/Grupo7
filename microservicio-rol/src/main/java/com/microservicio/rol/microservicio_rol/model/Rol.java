package com.microservicio.rol.microservicio_rol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "roles")
@Schema(description = "Entidad que representa los distintos tipos de roles del sistema.")
public class Rol {

    public enum TipoRol {
        ADMIN,
        MODERADOR,
        SOPORTE,
        GESTOR_ANUNCIOS,
        USUARIO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol", nullable = false, updatable = false)
    @Schema(description = "ID único del rol", example = "1")
    private Long idRol;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rol", nullable = false)
    @Schema(description = "Tipo de rol del usuario", example = "ADMIN")
    private TipoRol tipoRol;
}
