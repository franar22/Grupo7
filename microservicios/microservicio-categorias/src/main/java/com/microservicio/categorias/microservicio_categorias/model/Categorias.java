package com.microservicio.categorias.microservicio_categorias.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categorias")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Categorias {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El titúlo no puede estar vacío")
  @Size(max = 120, message = "El titúlo no puede exceder los 120 caracteres")
  @Column(nullable = false, unique = true)
  private String titulo;

  @Size(max = 550, message = "La descripción no puede exceder los 550 caracteres")
  private String descripcion;

  @Column(name = "fecha_creacion", updatable = false)
  private LocalDateTime fechaCreacion;

  @PrePersist
  protected void creacionFecha() {
      this.fechaCreacion = LocalDateTime.now();
  }

}