package com.microservicio.post.microservicio_post.model;

import java.time.LocalDateTime;

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

@Entity
@Table(name = "publicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El titulo no puede estar vacío.")
  @Size(min = 1, max = 120, message = "El titulo debe estar entre 1 a 120 caracteres.")
  @Column(nullable = false)
  private String titulo;

  @NotBlank(message = "El contenido no puede estar vacío")
  @Lob  
  @Column(nullable = false)
  private String contenido;

  @NotNull(message = "El ID del foro no puede ser nulo")
  @Column(name = "id_foro", nullable = false, updatable = false)
  private Long idForo;

  @NotNull(message = "El ID del usuario no puede ser nulo")
  @Column(name = "id_usuario", nullable = false, updatable = false)
  private Long idUsuario;

  @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

  @PrePersist
  protected void creacionFecha() {
      this.fechaCreacion = LocalDateTime.now();
  }
}