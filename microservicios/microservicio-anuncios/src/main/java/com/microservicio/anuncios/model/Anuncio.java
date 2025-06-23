package com.microservicio.anuncios.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un anuncio en el sistema de foros.
 * Los anuncios son mensajes creados por moderadores para comunicar
 * información importante a los usuarios de un foro o publicación específica.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "anuncios")
@Schema(description = "Entidad que representa un anuncio en el sistema de foros. Los anuncios son mensajes creados por moderadores para comunicar información importante a los usuarios de un foro o publicación específica.")
public class Anuncio {
    
    /**
     * Identificador único del anuncio.
     * Se genera automáticamente al persistir la entidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del anuncio", example = "1")
    private Long id;

    /**
     * Mensaje del anuncio que se mostrará a los usuarios.
     * Es obligatorio y debe tener entre 3 y 1000 caracteres.
     */
    @NotBlank(message = "El mensaje del anuncio es obligatorio")
    @Size(min = 3, max = 1000, message = "El mensaje debe tener entre 3 y 1000 caracteres")
    @Column(nullable = false, length = 1000)
    @Schema(description = "Mensaje del anuncio que se mostrará a los usuarios", example = "El foro estará en mantenimiento el sábado a las 22:00.")
    private String mensaje;

    /**
     * Identificador del moderador que creó el anuncio.
     * Es obligatorio y no puede ser nulo.
     */
    @NotNull(message = "El ID del moderador es obligatorio")
    @Column(name = "moderador_id", nullable = false)
    @Schema(description = "Identificador del moderador que creó el anuncio", example = "5")
    private Long moderadorId;

    /**
     * Identificador del foro al que pertenece el anuncio.
     * Puede ser nulo si el anuncio es general del sistema.
     */
    @Column(name = "foro_id")
    @Schema(description = "Identificador del foro al que pertenece el anuncio. Puede ser nulo si el anuncio es general del sistema.", example = "2")
    private Long foroId;

    /**
     * Identificador de la publicación específica a la que se refiere el anuncio.
     * Puede ser nulo si el anuncio es general del foro o del sistema.
     */
    @Column(name = "publicacion_id")
    @Schema(description = "Identificador de la publicación específica a la que se refiere el anuncio. Puede ser nulo si el anuncio es general del foro o del sistema.", example = "10")
    private Long publicacionId;

    /**
     * Fecha y hora de creación del anuncio.
     * Se establece automáticamente al persistir la entidad.
     */
    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha y hora de creación del anuncio", example = "2024-06-01T15:30:00")
    private LocalDateTime fechaCreacion;

    /**
     * Método que se ejecuta antes de persistir la entidad.
     * Establece automáticamente la fecha de creación.
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
} 