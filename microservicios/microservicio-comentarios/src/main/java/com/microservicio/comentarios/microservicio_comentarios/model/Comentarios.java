package com.microservicio.comentarios.microservicio_comentarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comentarios")
@Schema(description = "Entidad que representa un comentario realizado por un usuario sobre una publicación.")
public class Comentarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del comentario", example = "10")
    private Long id;

    @NotBlank(message = "El contenido no puede estar vacío.")
    @Column(nullable = false)
    @Schema(description = "Contenido  del comentario", example = "Este es un comentario.")
    private String contenido;

    @NotNull(message = "El id de la publicación no puede estar vacío.")
    @Column(name = "id_publicacion", nullable = false, updatable = false)
    @Schema(description = "ID de la publicación relacionada", example = "10")
    private Long idPost;

    @NotNull(message = "El id del usuario no puede estar vacío.")
    @Column(name = "id_usuario", nullable = false, updatable = false)
    @Schema(description = "ID del usuario que comentó", example = "23")
    private Long idUsuario;

    @Column(name = "fecha_creacion", updatable = false)
    @Schema(description = "Fecha de creación del comentario", example = "2025-06-20T12:00:00")
    private LocalDateTime fechaCreacion;

    @PrePersist
    private void creacionClase() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
