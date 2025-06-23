package com.microservicio.interacciones.service;

import com.microservicio.interacciones.exception.ResourceNotFoundException;
import com.microservicio.interacciones.model.Interaccion;
import com.microservicio.interacciones.repository.InteraccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InteraccionService {
    @Autowired
    private InteraccionRepository interaccionRepository;

    public List<Interaccion> findAll() {
        return interaccionRepository.findAll();
    }

    public Interaccion findById(Long id) {
        return interaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interaccion", "id", id));
    }

    public Interaccion save(Interaccion interaccion) {
        return interaccionRepository.save(interaccion);
    }

    public void deleteById(Long id) {
        if (!interaccionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Interaccion", "id", id);
        }
        interaccionRepository.deleteById(id);
    }

    public long countLikesByPublicacion(Long publicacionId) {
        return interaccionRepository.countByTipoAndPublicacionId(Interaccion.TipoInteraccion.LIKE, publicacionId);
    }

    public long countDislikesByPublicacion(Long publicacionId) {
        return interaccionRepository.countByTipoAndPublicacionId(Interaccion.TipoInteraccion.DISLIKE, publicacionId);
    }

    public long countLikesByComentario(Long comentarioId) {
        return interaccionRepository.countByTipoAndComentarioId(Interaccion.TipoInteraccion.LIKE, comentarioId);
    }

    public long countDislikesByComentario(Long comentarioId) {
        return interaccionRepository.countByTipoAndComentarioId(Interaccion.TipoInteraccion.DISLIKE, comentarioId);
    }

    public List<Interaccion> findByPublicacionId(Long publicacionId) {
        return interaccionRepository.findByPublicacionId(publicacionId);
    }

    public List<Interaccion> findByComentarioId(Long comentarioId) {
        return interaccionRepository.findByComentarioId(comentarioId);
    }

    public List<Interaccion> findByUsuarioId(Long usuarioId) {
        return interaccionRepository.findByUsuarioId(usuarioId);
    }
} 