package com.microservicio.comentarios.microservicio_comentarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservicio.comentarios.microservicio_comentarios.model.Comentarios;

@Repository
public interface ComentariosRepository extends JpaRepository<Comentarios, Long> {
    List<Comentarios> findByIdUsuario(Long idUsuario);
    
    List<Comentarios> findByIdPost(Long idPost);

    

}
