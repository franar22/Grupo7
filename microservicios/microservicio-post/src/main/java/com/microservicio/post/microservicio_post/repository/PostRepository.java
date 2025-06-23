package com.microservicio.post.microservicio_post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservicio.post.microservicio_post.model.Post;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByIdUsuario(Long idUsuario);
    List<Post> findByIdForo(Long idForo);
}
