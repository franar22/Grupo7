package com.microservicio.soporte.model;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un ticket de soporte en el sistema de foros.
 * Los tickets permiten a los usuarios solicitar ayuda técnica, reportar problemas
 * con su cuenta, o solicitar soporte sobre contenido.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
@Schema(description = "Entidad que representa un ticket de soporte en el sistema de foros. Los tickets permiten a los usuarios solicitar ayuda técnica, reportar problemas con su cuenta, o solicitar soporte sobre contenido.")
public class Ticket {
    
    /**
     * Identificador único del ticket.
     * Se genera automáticamente al persistir la entidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del ticket", example = "1")
    private Long id;
    
    /**
     * Título del ticket de soporte.
     * Es obligatorio y debe tener entre 3 y 100 caracteres.
     */
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    @Schema(description = "Título del ticket de soporte", example = "No puedo acceder a mi cuenta")
    private String titulo;
    
    /**
     * Descripción detallada del problema o solicitud.
     * Es obligatorio y debe tener entre 10 y 1000 caracteres.
     */
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    @Column(nullable = false, length = 1000)
    @Schema(description = "Descripción detallada del problema o solicitud", example = "Intento iniciar sesión y me dice que la contraseña es incorrecta, pero estoy seguro de que es la correcta.")
    private String descripcion;
    
    /**
     * Identificador del usuario que creó el ticket.
     * Es obligatorio y no puede ser nulo.
     */
    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "Identificador del usuario que creó el ticket", example = "7")
    private Long usuarioId;
    
    /**
     * Identificador del moderador asignado al ticket.
     * Puede ser nulo si el ticket aún no ha sido asignado.
     */
    @Column(name = "moderador_id")
    @Schema(description = "Identificador del moderador asignado al ticket. Puede ser nulo si el ticket aún no ha sido asignado.", example = "3")
    private Long moderadorId;
    
    /**
     * Estado actual del ticket.
     * Es obligatorio y se establece automáticamente como "ABIERTO" al crear.
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del ticket es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Estado actual del ticket", example = "ABIERTO")
    private EstadoTicket estado;
    
    /**
     * Categoría del ticket que define el tipo de soporte requerido.
     * Es obligatorio y ayuda a clasificar y asignar el ticket correctamente.
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "La categoría del ticket es obligatoria")
    @Column(nullable = false)
    @Schema(description = "Categoría del ticket que define el tipo de soporte requerido", example = "TECNICO")
    private CategoriaTicket categoria;
    
    /**
     * Fecha y hora de creación del ticket.
     * Se establece automáticamente al persistir la entidad.
     */
    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha y hora de creación del ticket", example = "2024-06-01T15:30:00")
    private LocalDateTime fechaCreacion;
    
    /**
     * Fecha y hora de la última actualización del ticket.
     * Se actualiza automáticamente al modificar la entidad.
     */
    @Column(name = "fecha_actualizacion")
    @Schema(description = "Fecha y hora de la última actualización del ticket", example = "2024-06-02T10:00:00")
    private LocalDateTime fechaActualizacion;
    
    /**
     * Método que se ejecuta antes de persistir la entidad.
     * Establece automáticamente la fecha de creación y el estado inicial.
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoTicket.ABIERTO;
        }
    }
    
    /**
     * Método que se ejecuta antes de actualizar la entidad.
     * Establece automáticamente la fecha de actualización.
     */
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    /**
     * Enum que define los estados posibles de un ticket.
     */
    @Schema(description = "Enum que define los estados posibles de un ticket.")
    public enum EstadoTicket {
        /** Ticket recién creado y pendiente de revisión */
        @Schema(description = "Ticket recién creado y pendiente de revisión")
        ABIERTO,
        /** Ticket en proceso de resolución */
        @Schema(description = "Ticket en proceso de resolución")
        EN_PROCESO,
        /** Ticket resuelto satisfactoriamente */
        @Schema(description = "Ticket resuelto satisfactoriamente")
        RESUELTO,
        /** Ticket cerrado sin resolución */
        @Schema(description = "Ticket cerrado sin resolución")
        CERRADO
    }
    
    /**
     * Enum que define las categorías de soporte disponibles.
     */
    @Schema(description = "Enum que define las categorías de soporte disponibles.")
    public enum CategoriaTicket {
        /** Problemas técnicos con la plataforma */
        @Schema(description = "Problemas técnicos con la plataforma")
        TECNICO,
        /** Problemas con la cuenta de usuario */
        @Schema(description = "Problemas con la cuenta de usuario")
        CUENTA,
        /** Problemas relacionados con contenido */
        @Schema(description = "Problemas relacionados con contenido")
        CONTENIDO,
        /** Otros tipos de soporte */
        @Schema(description = "Otros tipos de soporte")
        OTRO
    }
} 