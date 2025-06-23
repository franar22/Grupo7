package com.microservicio.comentarios.microservicio_comentarios.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;

import com.microservicio.comentarios.microservicio_comentarios.clients.PostClient;
import com.microservicio.comentarios.microservicio_comentarios.clients.UsuarioClient;
import com.microservicio.comentarios.microservicio_comentarios.model.Comentarios;
import com.microservicio.comentarios.microservicio_comentarios.repository.ComentariosRepository;
import com.microservicio.comentarios.microservicio_comentarios.services.ComentariosService;

@ExtendWith(MockitoExtension.class)
public class ComentariosServiceTest {

    @Mock
    ComentariosRepository repository;

    @InjectMocks
    ComentariosService service;

    @Mock
    private PostClient postClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Test
    void findAll_returnsListFromRepository(){

        List<Comentarios> mocklist = Arrays.asList(new Comentarios(1L, "Primer test", null, null, null));


        when(repository.findAll()).thenReturn(mocklist);

        List<Comentarios> result = service.listarComentarios();


         assertThat(result).isEqualTo(mocklist);


    }

    @Test
void findById_returnsComentarioFromRepository() {
    Comentarios nuevoComentario = new Comentarios(1L, null, null, null, null);

    when(repository.findById(1L)).thenReturn(Optional.of(nuevoComentario));

    Comentarios result = service.buscarComentarioPorId(1L);

    assertThat(result).isEqualTo(nuevoComentario);
}

   @Test
void borrarComentarioPorIdRetrunsnada() {
    Comentarios comentario = new Comentarios(1L, null, null, null, null);


    when(repository.findById(1L)).thenReturn(Optional.of(comentario));

    
    doNothing().when(repository).deleteById(1L);

    String resultado = service.borrarComentarioPorId(1L);

    assertThat(resultado).isEqualTo("Comentario Eliminado con existo");
}

  @Test
  void guardarComentarioreturnsave() {
    Comentarios comentario = new Comentarios(1L, "contenido", 1L, 2L, null);

    
    when(postClient.obtenerPostPorId(1L)).thenReturn(Map.of("id", 1));
    when(usuarioClient.obtenerUsuarioPorId(2L)).thenReturn(Map.of("id", 2));
    when(repository.save(comentario)).thenReturn(comentario);

    Comentarios result = service.guardarComentario(comentario);

    assertThat(result).isEqualTo(comentario);
}


  @Test
void actualizarComentario_modificaContenidoYRetornaComentarioActualizado() {
    
    Comentarios comentarioExistente = new Comentarios(1L, "Contenido original", null, null, null);

    
    Comentarios comentarioActualizado = new Comentarios(1L, "Nuevo contenido", null, null, null);

   
    when(repository.findById(1L)).thenReturn(Optional.of(comentarioExistente));

    when(repository.save(any(Comentarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Comentarios result = service.actualizarComentario(1L, comentarioActualizado);

    assertThat(result.getContenido()).isEqualTo("Nuevo contenido");
}

  @Test
  void obtenerComentariosPorUsuarioall() {
    Long idUsuario = 1L;
    List<Comentarios> mockList = Arrays.asList(new Comentarios(1L, null, null, idUsuario, null));

    when(repository.findByIdUsuario(idUsuario)).thenReturn(mockList);

    List<Comentarios> result = service.obtenerComentariosPorUsuario(idUsuario);

    assertThat(result).isEqualTo(mockList);
}



    
  

  @Test
void obtenerComentariosPorPostall() {
    Long idPost = 1L;
    List<Comentarios> mockList = Arrays.asList(new Comentarios(1L, null, idPost, null, null));

    when(repository.findByIdPost(idPost)).thenReturn(mockList);

    List<Comentarios> result = service.obtenerComentariosPorPost(idPost);

    assertThat(result).isEqualTo(mockList);
}

  
  





   }


//public List<Comentarios> obtenerComentariosPorUsuario(Long idUsuario) {
    //return comentariosRepository.findByIdUsuario(idUsuario);
//}







