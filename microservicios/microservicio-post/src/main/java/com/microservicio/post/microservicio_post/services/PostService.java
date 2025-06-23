package com.microservicio.post.microservicio_post.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.microservicio.post.microservicio_post.client.ForoClient;
import com.microservicio.post.microservicio_post.client.UsuarioClient;
import com.microservicio.post.microservicio_post.model.Post;
import com.microservicio.post.microservicio_post.repository.PostRepository;

@Service
public class PostService {

  @Autowired
  private PostRepository postRepository;

  @Autowired 
  private ForoClient foroClient;

  @Autowired
  private UsuarioClient usuarioClient;

  public List<Post> listarPublicaciones() {
    return postRepository.findAll();
  }

  public Post buscarPublicacionPorId(Long id) {
    return postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicacion no encontrada en la base de datos con ese ID."));
  }

  public String borrarPubliacionPorId(Long id) {
    Post postActual = buscarPublicacionPorId(id);
    postRepository.deleteById(postActual.getId());
    return "Publicacion Eliminada";
  }

  public Post guardarPublicacion(Post publicacion) {
    Map<String, Object> verificarForo = foroClient.obtenerForoPorId(publicacion.getIdForo());
    Map<String, Object> verificarUsuario = usuarioClient.obtenerUsuarioPorId(publicacion.getIdUsuario());

    if (verificarForo == null || verificarForo.isEmpty()) {
      throw new RuntimeException("El id del Foro no se ha encontrado. No se puede crear un comentario.");
    }

    if (verificarUsuario == null || verificarUsuario.isEmpty()) {
      throw new RuntimeException("El id del usuario no existe. No es psible realizar un comentario");
    }

    return postRepository.save(publicacion);
    
  }


  public Post actualizarPost(Long id, Post postActualizado) {
    Post postActual = buscarPublicacionPorId(id);

    //LOS ID ESTAN PROTEGIDOS (ver modelo updatble) POR LO QUE NO SE MODIFICAN CASO CONTRARIO DESCOMENTAR ESTO
    // if (postActualizado.getIdForo() != null) {
    //   Map<String, Object> verificarForo = foroClient.obtenerForoPorId(postActualizado.getIdForo());

    //   if (verificarForo == null || verificarForo.isEmpty()) {
    //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El id del Foro no se ha encontrado. No se puede crear un comentario.");
    //   }

    //   // NO MODIFICABLES 
    //   // postActual.setIdForo(postActualizado.getIdForo());
    // }

    // if (postActualizado.getIdUsuario() != null) {
    //   Map<String, Object> verificarUsuario = usuarioClient.obtenerUsuarioPorId(postActualizado.getIdUsuario());

    //   if (verificarUsuario == null || verificarUsuario.isEmpty()) {
    //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del usuario no existe. No es psible realizar un comentario");
    //   }

    //   //NO MODIFICABLE
    //   // postActual.setIdUsuario(postActualizado.getIdUsuario());
    // }

    if (postActualizado.getTitulo() != null) {
      postActual.setTitulo(postActualizado.getTitulo());
    }

    if (postActualizado.getContenido() != null) {
      postActual.setContenido(postActualizado.getContenido());
    }

    return postRepository.save(postActual);

  }

  public List<Post> listarPublicacionesPorUsuario(Long idUsuario) {
    return postRepository.findByIdUsuario(idUsuario);
  }

  public List<Post> listarPublicacionesPorForo(Long idForo) {
    return postRepository.findByIdForo(idForo);
  }

}
