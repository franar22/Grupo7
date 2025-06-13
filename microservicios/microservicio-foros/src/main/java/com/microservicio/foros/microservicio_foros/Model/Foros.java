package com.microservicio.foros.microservicio_foros.Model;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "foros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Foros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Id de otro microservicio para luego buscarlo y llamarlo :)
    @NotNull(message = "El id de categoria no puede estar vacío")
    @Column(name = "id_categoria", nullable = false)
    private Long idCategoria;

    @NotNull(message = "El id del usuario no puede estar vacío")
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;


    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede exceder los 100 caracteres")
    @Column(nullable = false, unique = true)
    private String titulo;

    @Size(max = 500, message = "La descripción no puede exceder los 5000 caracteres")
    private String contenido;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void creacionFecha() {
        this.fechaCreacion = LocalDateTime.now();
    }
}