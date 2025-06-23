package com.microservicio.interacciones.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Entidad que representa una interacción de usuario (like/dislike) en el sistema.
 * Las interacciones pueden ser aplicadas tanto a publicaciones como a comentarios.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interacciones")
@Schema(description = "Entidad que representa una interacción de usuario (like/dislike) en el sistema. Las interacciones pueden ser aplicadas tanto a publicaciones como a comentarios.")
public class Interaccion {
    
    /**
     * Identificador único de la interacción.
     * Se genera automáticamente al persistir la entidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la interacción", example = "1")
    private Long id;

    /**
     * Tipo de interacción (LIKE o DISLIKE).
     * Es obligatorio y no puede ser nulo.
     */
    @NotNull(message = "El tipo de interacción es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo de interacción (LIKE o DISLIKE)", example = "LIKE")
    private TipoInteraccion tipo;

    /**
     * Identificador del usuario que realizó la interacción.
     * Es obligatorio y no puede ser nulo.
     */
    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "Identificador del usuario que realizó la interacción", example = "7")
    private Long usuarioId;

    /**
     * Identificador de la publicación sobre la cual se realizó la interacción.
     * Puede ser nulo si la interacción es sobre un comentario.
     */
    @Column(name = "publicacion_id")
    @Schema(description = "Identificador de la publicación sobre la cual se realizó la interacción. Puede ser nulo si la interacción es sobre un comentario.", example = "2")
    private Long publicacionId;

    /**
     * Identificador del comentario sobre el cual se realizó la interacción.
     * Puede ser nulo si la interacción es sobre una publicacion.
     */
    @Column(name = "comentario_id")
    @Schema(description = "Identificador del comentario sobre el cual se realizó la interacción. Puede ser nulo si la interacción es sobre una publicación.", example = "5")
    private Long comentarioId;

    /**
     * Fecha y hora de creación de la interacción.
     * Se establece automáticamente al persistir la entidad.
     */
    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha y hora de creación de la interacción", example = "2024-06-01T15:30:00")
    private LocalDateTime fechaCreacion;

    /**
     * Método que se ejecuta antes de persistir la entidad.
     * Establece automáticamente la fecha de creación.
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    /**
     * Enum que define los tipos de interacción disponibles.
     */
    @Schema(description = "Enum que define los tipos de interacción disponibles.")
    public enum TipoInteraccion {
        /** Interacción positiva */
        @Schema(description = "Interacción positiva")
        LIKE,
        /** Interacción negativa */
        @Schema(description = "Interacción negativa")
        DISLIKE
    }
} 