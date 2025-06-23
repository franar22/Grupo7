package com.microservicio.post.microservicio_post.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una publicación en el sistema de foros.
 * Las publicaciones son el contenido principal que los usuarios crean
 * dentro de un foro específico.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "publicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una publicación en el sistema de foros. Las publicaciones son el contenido principal que los usuarios crean dentro de un foro específico.")
public class Post {

  /**
   * Identificador único de la publicación.
   * Se genera automáticamente al persistir la entidad.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Identificador único de la publicación", example = "1")
  private Long id;

  /**
   * Título de la publicación.
   * Es obligatorio y debe tener entre 1 y 120 caracteres.
   */
  @NotBlank(message = "El titulo no puede estar vacío.")
  @Size(min = 1, max = 120, message = "El titulo debe estar entre 1 a 120 caracteres.")
  @Column(nullable = false)
  @Schema(description = "Título de la publicación", example = "¿Cómo mejorar el rendimiento en Java?")
  private String titulo;

  /**
   * Contenido principal de la publicación.
   * Es obligatorio y puede contener texto largo.
   */
  @NotBlank(message = "El contenido no puede estar vacío")
  @Lob  
  @Column(nullable = false)
  @Schema(description = "Contenido principal de la publicación", example = "Estoy buscando consejos y buenas prácticas para optimizar el rendimiento de mis aplicaciones Java.")
  private String contenido;

  /**
   * Identificador del foro al que pertenece la publicación.
   * Es obligatorio y no se puede modificar una vez creada.
   */
  @NotNull(message = "El ID del foro no puede ser nulo")
  @Column(name = "id_foro", nullable = false, updatable = false)
  @Schema(description = "Identificador del foro al que pertenece la publicación", example = "2")
  private Long idForo;

  /**
   * Identificador del usuario que creó la publicación.
   * Es obligatorio y no se puede modificar una vez creada.
   */
  @NotNull(message = "El ID del usuario no puede ser nulo")
  @Column(name = "id_usuario", nullable = false, updatable = false)
  @Schema(description = "Identificador del usuario que creó la publicación", example = "7")
  private Long idUsuario;

  /**
   * Fecha y hora de creación de la publicación.
   * Se establece automáticamente al persistir la entidad.
   */
  @Column(name = "fecha_creacion", updatable = false)
  @Schema(description = "Fecha y hora de creación de la publicación", example = "2024-06-01T15:30:00")
  private LocalDateTime fechaCreacion;

  /**
   * Método que se ejecuta antes de persistir la entidad.
   * Establece automáticamente la fecha de creación.
   */
  @PrePersist
  protected void creacionFecha() {
      this.fechaCreacion = LocalDateTime.now();
  }
}
