package com.microservicio.interacciones.repository;

import com.microservicio.interacciones.model.Interaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteraccionRepository extends JpaRepository<Interaccion, Long> {
    long countByTipoAndPublicacionId(Interaccion.TipoInteraccion tipo, Long publicacionId);
    long countByTipoAndComentarioId(Interaccion.TipoInteraccion tipo, Long comentarioId);
    List<Interaccion> findByPublicacionId(Long publicacionId);
    List<Interaccion> findByComentarioId(Long comentarioId);
    List<Interaccion> findByUsuarioId(Long usuarioId);
} 