package com.microservicio.post.microservicio_post.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.post.microservicio_post.model.Post;
import com.microservicio.post.microservicio_post.services.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/publicaciones")
public class PostController {

  @Autowired
  private PostService postService;

  @GetMapping()
  public ResponseEntity<List<Post>> listarPublicaciones() {
    List<Post> listaPost = postService.listarPublicaciones();
    if ( listaPost.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(listaPost);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> obtenerPublicacion(@PathVariable Long id) {
    try {
      Post postEncontrado = postService.buscarPublicacionPorId(id);
      return ResponseEntity.ok(postEncontrado);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PostMapping()
  public ResponseEntity<?> crearPublicacion(@RequestBody @Valid Post publicacion) {
    try {
      Post publicacionGuardada = postService.guardarPublicacion(publicacion);
      return ResponseEntity.status(HttpStatus.CREATED).body(publicacionGuardada);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("Error al crear la publicacion: " + e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> actualizarPublicacion(
    @PathVariable Long id,
    @RequestBody @Valid Post publicacion) {
      try {
        Post postActual = postService.actualizarPost(id, publicacion);
        return ResponseEntity.ok(postActual);
      } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body("Error al actualizar la publicación: " + e.getMessage());
      }
    }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> borrarPublicacion(@PathVariable Long id) {
      try {
        String postActual = postService.borrarPubliacionPorId(id);
        return ResponseEntity.ok(postActual);

      } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Post>> obtenerPostsPorUsuario(@PathVariable Long idUsuario) {
        List<Post> posts = postService.obtenerPostsPorUsuario(idUsuario);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/foro/{idForo}")
    public ResponseEntity<List<Post>> obtenerPostsPorForo(@PathVariable Long idForo) {
        List<Post> posts = postService.obtenerPostsPorForo(idForo);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }
}