package com.microservicio.reportes.model;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un reporte en el sistema de foros.
 * Los reportes permiten a los usuarios denunciar contenido inapropiado,
 * spam, o comportamientos que violen las reglas del foro.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reportes")
@Schema(description = "Entidad que representa un reporte en el sistema de foros. Los reportes permiten a los usuarios denunciar contenido inapropiado, spam, o comportamientos que violen las reglas del foro.")
public class Reporte {
    
    /**
     * Identificador único del reporte.
     * Se genera automáticamente al persistir la entidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del reporte", example = "1")
    private Long id;
    
    /**
     * Título del reporte.
     * Es obligatorio y debe tener entre 3 y 100 caracteres.
     */
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    @Schema(description = "Título del reporte", example = "Contenido inapropiado")
    private String titulo;
    
    /**
     * Descripción detallada del reporte.
     * Es obligatorio y debe tener entre 10 y 1000 caracteres.
     */
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    @Column(nullable = false, length = 1000)
    @Schema(description = "Descripción detallada del reporte", example = "El usuario publicó mensajes ofensivos en el foro.")
    private String descripcion;
    
    /**
     * Tipo de reporte (ej: SPAM, INAPPROPRIATE, HARASSMENT, etc.).
     * Es obligatorio y define la categoría del reporte.
     */
    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Column(name = "tipo_reporte", nullable = false)
    @Schema(description = "Tipo de reporte (ej: SPAM, INAPPROPRIATE, HARASSMENT, etc.)", example = "SPAM")
    private String tipoReporte;
    
    /**
     * Identificador del foro relacionado con el reporte.
     * Puede ser nulo si el reporte no está relacionado con un foro específico.
     */
    @Column(name = "id_foro")
    @Schema(description = "Identificador del foro relacionado con el reporte. Puede ser nulo si el reporte no está relacionado con un foro específico.", example = "2")
    private Long idForo;
    
    /**
     * Identificador de la categoría relacionada con el reporte.
     * Puede ser nulo si el reporte no está relacionado con una categoría específica.
     */
    @Column(name = "id_categoria")
    @Schema(description = "Identificador de la categoría relacionada con el reporte. Puede ser nulo si el reporte no está relacionado con una categoría específica.", example = "3")
    private Long idCategoria;
    
    /**
     * Identificador del usuario que realiza el reporte.
     * Es obligatorio y no puede ser nulo.
     */
    @NotNull(message = "El ID del usuario reportante es obligatorio")
    @Column(name = "id_usuario_reportante", nullable = false)
    @Schema(description = "Identificador del usuario que realiza el reporte", example = "7")
    private Long idUsuarioReportante;
    
    /**
     * Identificador del usuario que es reportado.
     * Puede ser nulo si el reporte no está dirigido a un usuario específico.
     */
    @Column(name = "id_usuario_reportado")
    @Schema(description = "Identificador del usuario que es reportado. Puede ser nulo si el reporte no está dirigido a un usuario específico.", example = "8")
    private Long idUsuarioReportado;
    
    /**
     * Estado actual del reporte (PENDIENTE, EN_REVISION, RESUELTO, RECHAZADO).
     * Es obligatorio y se establece automáticamente como "PENDIENTE" al crear.
     */
    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false)
    @Schema(description = "Estado actual del reporte (PENDIENTE, EN_REVISION, RESUELTO, RECHAZADO)", example = "PENDIENTE")
    private String estado;
    
    /**
     * Fecha y hora de creación del reporte.
     * Se establece automáticamente al persistir la entidad.
     */
    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha y hora de creación del reporte", example = "2024-06-01T15:30:00")
    private LocalDateTime fechaCreacion;
    
    /**
     * Fecha y hora de la última actualización del reporte.
     * Se actualiza automáticamente al modificar la entidad.
     */
    @Column(name = "fecha_actualizacion")
    @Schema(description = "Fecha y hora de la última actualización del reporte", example = "2024-06-02T10:00:00")
    private LocalDateTime fechaActualizacion;
    
    /**
     * Método que se ejecuta antes de persistir la entidad.
     * Establece automáticamente la fecha de creación y el estado inicial.
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        estado = "PENDIENTE";
    }
    
    /**
     * Método que se ejecuta antes de actualizar la entidad.
     * Establece automáticamente la fecha de actualización.
     */
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
} 