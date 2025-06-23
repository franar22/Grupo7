package com.microservicio.comentarios.microservicio_comentarios.controller;

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

import com.microservicio.comentarios.microservicio_comentarios.model.Comentarios;
import com.microservicio.comentarios.microservicio_comentarios.services.ComentariosService;

import io.swagger.v3.oas.annotations.Operation;
//http://localhost:8090/swagger-ui/index.html para ver la documentacion
@RestController
@RequestMapping("/api/comentarios")
public class ComentariosController {

  @Autowired
  private ComentariosService comentariosService;

  @Operation(summary = "Listar todos los comentarios",description = "Devuelve una lista con todos los comentarios del sistema. Si no hay comentarios, retorna 204 No Content.")
  @GetMapping()
  public ResponseEntity<List<Comentarios>> listaDeComentarios() {
    List<Comentarios> comentariosExistentes = comentariosService.listarComentarios();
    if (comentariosExistentes.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(comentariosExistentes);
  }

  
  @Operation(summary = "Obtener comentario por ID",description = "Devuelve los datos de un comentario específico a partir de su ID. Si no existe, retorna 404.")
  @GetMapping("/{id}")
  public ResponseEntity<?> buscarComentarioPorId(@PathVariable Long id) {
    try {
      Comentarios comentarioAct = comentariosService.buscarComentarioPorId(id);
      return ResponseEntity.ok(comentarioAct);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
  
  @Operation(summary = "Eliminar comentario por ID",description = "Elimina un comentario del sistema utilizando su ID. Si no se encuentra, retorna 404.")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminarComentarioPorId(@PathVariable Long id) {
    try {
      String comentarioAct = comentariosService.borrarComentarioPorId(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @Operation(summary = "Crear un nuevo comentario",description = "Permite crear un nuevo comentario con los datos enviados en el cuerpo de la solicitud.")
  @PostMapping()
  public ResponseEntity<?> crearNuevoComentario(@RequestBody Comentarios comentarioNuevo) {
    try {
      Comentarios comentario = comentariosService.guardarComentario(comentarioNuevo);
      return ResponseEntity.status(HttpStatus.CREATED).body(comentario);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No es posible crear el comentario: " + e.getMessage());
    }
  }

  @Operation(summary = "Actualizar un comentario",description = "Actualiza la información de un comentario existente identificado por su ID.")
  @PutMapping("/{id}")
  public ResponseEntity<?> actualizarComentarioPorId(
    @PathVariable Long id,
    @RequestBody Comentarios comentarioActualizado
    ) {
      try {
        Comentarios comentario = comentariosService.actualizarComentario(id, comentarioActualizado);
        return ResponseEntity.ok(comentario);
      } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body("Error al actualizar el comentario: " + e.getMessage());
      }
    }

    @Operation(summary = "Listar comentarios por ID de usuario",description = "Devuelve todos los comentarios asociados a un usuario específico. Si no hay, retorna 204 No Content.")
    @GetMapping("/usuario/{idUsuario}")
public ResponseEntity<List<Comentarios>> comentariosPorUsuario(@PathVariable Long idUsuario) {
    List<Comentarios> comentarios = comentariosService.obtenerComentariosPorUsuario(idUsuario);
    return comentarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(comentarios);
}

@Operation(summary = "Listar comentarios por ID de publicación",description = "Devuelve todos los comentarios asociados a una publicación específica. Si no hay, retorna 204 No Content.")
@GetMapping("/post/{idPost}")
public ResponseEntity<List<Comentarios>> comentariosPorPost(@PathVariable Long idPost) {
    List<Comentarios> comentarios = comentariosService.obtenerComentariosPorPost(idPost);
    return comentarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(comentarios);
}

}
