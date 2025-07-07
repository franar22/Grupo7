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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * Controlador REST para gestionar las publicaciones del sistema de foros.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar publicaciones,
 * así como para filtrar publicaciones por usuario y por foro.
 * 
 * @author Grupo7
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/publicaciones")
public class PostController {

  @Autowired
  private PostService postService;

  @Operation(summary = "Obtener todas las publicaciones", description = "Devuelve una lista con todas las publicaciones del sistema. Si no hay publicaciones, retorna 204 No Content.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Publicaciones encontradas", content = @Content(schema = @Schema(implementation = Post.class))),
    @ApiResponse(responseCode = "204", description = "No hay publicaciones", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
  @GetMapping()
  public ResponseEntity<List<Post>> listarPublicaciones() {
    List<Post> listaPost = postService.listarPublicaciones();
    if ( listaPost.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(listaPost);
  }

  @Operation(summary = "Obtener una publicación por ID", description = "Devuelve una publicación específica según su identificador único. Si no existe, retorna 404 Not Found.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Publicación encontrada", content = @Content(schema = @Schema(implementation = Post.class))),
    @ApiResponse(responseCode = "404", description = "Publicación no encontrada", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
  @GetMapping("/{id}")
  public ResponseEntity<?> obtenerPublicacion(@PathVariable Long id) {
    try {
      Post postEncontrado = postService.buscarPublicacionPorId(id);
      return ResponseEntity.ok(postEncontrado);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @Operation(summary = "Crear una nueva publicación", description = "Crea una nueva publicación en el sistema. Valida que el foro y usuario existan antes de crear la publicación.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Publicación creada correctamente", content = @Content(schema = @Schema(implementation = Post.class))),
    @ApiResponse(responseCode = "400", description = "Error en los datos de la publicación", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
  @PostMapping()
  public ResponseEntity<?> crearPublicacion(@RequestBody @Valid Post publicacion) {
    try {
      Post publicacionGuardada = postService.guardarPublicacion(publicacion);
      return ResponseEntity.status(HttpStatus.CREATED).body(publicacionGuardada);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("Error al crear la publicacion: " + e.getMessage());
    }
  }

  @Operation(summary = "Actualizar una publicación existente", description = "Actualiza el título y contenido de una publicación existente. No permite modificar el foro ni el usuario.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Publicación actualizada correctamente", content = @Content(schema = @Schema(implementation = Post.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar la publicación", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "404", description = "Publicación no encontrada", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
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

  @Operation(summary = "Eliminar una publicación por ID", description = "Elimina una publicación específica según su identificador único. Retorna un mensaje de confirmación o 404 Not Found si no existe.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Publicación eliminada correctamente", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "404", description = "Publicación no encontrada", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
  @DeleteMapping("/{id}")
  public ResponseEntity<?> borrarPublicacion(@PathVariable Long id) {
      try {
        String postActual = postService.borrarPubliacionPorId(id);
        return ResponseEntity.ok(postActual);
      } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    }

  @Operation(summary = "Obtener publicaciones por usuario", description = "Devuelve una lista de publicaciones creadas por un usuario específico. Si no tiene publicaciones, retorna 204 No Content.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Publicaciones encontradas", content = @Content(schema = @Schema(implementation = Post.class))),
    @ApiResponse(responseCode = "204", description = "El usuario no tiene publicaciones", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
  @GetMapping("/usuario/{idUsuario}")
  public ResponseEntity<List<Post>> listarPublicacionesPorUsuario(@PathVariable Long idUsuario) {
    List<Post> listaPost = postService.listarPublicacionesPorUsuario(idUsuario);
    if (listaPost.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(listaPost);
  }

  @Operation(summary = "Obtener publicaciones por foro", description = "Devuelve una lista de publicaciones asociadas a un foro específico. Si no tiene publicaciones, retorna 204 No Content.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Publicaciones encontradas", content = @Content(schema = @Schema(implementation = Post.class))),
    @ApiResponse(responseCode = "204", description = "El foro no tiene publicaciones", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
})
  @GetMapping("/foro/{idForo}")
  public ResponseEntity<List<Post>> listarPublicacionesPorForo(@PathVariable Long idForo) {
    List<Post> listaPost = postService.listarPublicacionesPorForo(idForo);
    if (listaPost.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(listaPost);
  }
}
