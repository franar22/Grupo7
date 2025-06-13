package com.microservicio.comentarios.microservicio_comentarios.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comentarios")
public class Comentarios {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El notenido no puede estar vacíó.")
  @Column(nullable = false)
  private String contenido;

  // PROTEGIDOS CON UPDATABLE
  @NotNull(message = "El id de la publicación no puede estar vacío.")
  @Column(name = "id_publicacion", nullable = false, updatable = false)
  private Long idPost;

  @NotNull(message = "El id del usuario no puede estar vacío.")
  @Column(name = "id_usuario", nullable = false, updatable = false)
  private Long idUsuario;

  @Column(name = "fecha_creacion", updatable = false)
  private LocalDateTime fechaCreacion;

  @PrePersist
  private void creacionClase() {
    this.fechaCreacion = LocalDateTime.now();
  }

}