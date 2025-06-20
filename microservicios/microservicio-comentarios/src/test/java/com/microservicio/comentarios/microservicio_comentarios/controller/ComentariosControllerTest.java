package com.microservicio.comentarios.microservicio_comentarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.comentarios.microservicio_comentarios.model.Comentarios;
import com.microservicio.comentarios.microservicio_comentarios.services.ComentariosService;

@WebMvcTest(ComentariosController.class)
public class ComentariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private ComentariosService comentariosService;

    @Test
    void getComentariosAlls() throws Exception {
    List<Comentarios> listaComentarios = Arrays.asList(new Comentarios(1L, null, null, null, null));

    when(comentariosService.listarComentarios()).thenReturn(listaComentarios);

    mockMvc.perform(get("/api/comentarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
}


   @Test
    void getComentarioPorIds() throws Exception {
    Comentarios comentario = new Comentarios(1L, "Contenido de prueba", null, null, null);

    when(comentariosService.buscarComentarioPorId(1L)).thenReturn(comentario);

    mockMvc.perform(get("/api/comentarios/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.contenido").value("Contenido de prueba"));
}
  
  @Test
  void eliminarComentarioPorIds() throws Exception {
    when(comentariosService.borrarComentarioPorId(1L))
        .thenReturn("Comentario Eliminado con existo");

    mockMvc.perform(delete("/api/comentarios/1"))
           .andExpect(status().isNoContent());
} 
  
 @Test
void crearNuevoComentarios() throws Exception {

    Comentarios nuevoComentario = new Comentarios(1L, "contenido", null, null, null);

    when(comentariosService.guardarComentario(any(Comentarios.class))).thenReturn(nuevoComentario);
    String json = objectMapper.writeValueAsString(nuevoComentario);
    mockMvc.perform(post("/api/comentarios")
            .contentType("application/json")
            .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.contenido").value("contenido"));
}


 @Test
void actualizarComentarios() throws Exception {
   
    Comentarios actucomentarios = new Comentarios(1L, "contenido actualizado", null, null, null);

    
    when(comentariosService.actualizarComentario(eq(1L), any(Comentarios.class)))
            .thenReturn(actucomentarios);
    String json = objectMapper.writeValueAsString(actucomentarios);
    mockMvc.perform(put("/api/comentarios/1")
            .contentType("application/json")
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.contenido").value("contenido actualizado"));
}
  
  @Test
void getAllComentariosPorUsuario() throws Exception {
    
    List<Comentarios> comentsPorUsuario = Arrays.asList(new Comentarios(1L, "comentario test", null, null, null));

    Long idUsuario = 5L;
    when(comentariosService.obtenerComentariosPorUsuario(idUsuario)).thenReturn(comentsPorUsuario);
    mockMvc.perform(get("/api/comentarios/usuario/{idUsuario}", idUsuario))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].contenido").value("comentario test"));
}

  @Test
void allComentariosPorPost() throws Exception {
    
    List<Comentarios> comentsPorPost = Arrays.asList(new Comentarios(1L, "comentario de prueba", 10L, 20L, null));

    Long idPost = 10L;
    when(comentariosService.obtenerComentariosPorPost(idPost)).thenReturn(comentsPorPost);
    mockMvc.perform(get("/api/comentarios/post/{idPost}", idPost))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].contenido").value("comentario de prueba"))
        .andExpect(jsonPath("$[0].idPost").value(10));
}








    


    }

    




