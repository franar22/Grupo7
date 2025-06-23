package com.microservicio.interacciones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.interacciones.model.Interaccion;
import com.microservicio.interacciones.service.InteraccionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InteraccionControllerTest {

    @Mock
    private InteraccionService interaccionService;

    @InjectMocks
    private InteraccionController interaccionController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Interaccion interaccion;
    private List<Interaccion> interacciones;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(interaccionController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); 

        interaccion = new Interaccion();
        interaccion.setId(1L);
        interaccion.setTipo(Interaccion.TipoInteraccion.LIKE);
        interaccion.setUsuarioId(1L);
        interaccion.setPublicacionId(1L);
        interaccion.setComentarioId(null);
        interaccion.setFechaCreacion(LocalDateTime.now());

        Interaccion interaccion2 = new Interaccion();
        interaccion2.setId(2L);
        interaccion2.setTipo(Interaccion.TipoInteraccion.DISLIKE);
        interaccion2.setUsuarioId(2L);
        interaccion2.setPublicacionId(1L);
        interaccion2.setComentarioId(null);
        interaccion2.setFechaCreacion(LocalDateTime.now());

        interacciones = Arrays.asList(interaccion, interaccion2);
    }

    @Test
    void getAll_WhenInteraccionesExist_ShouldReturnList() throws Exception {
        // Arrange
        when(interaccionService.findAll()).thenReturn(interacciones);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(interaccionService, times(1)).findAll();
    }

    @Test
    void getAll_WhenNoInteracciones_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(interaccionService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/interacciones"))
                .andExpect(status().isNoContent());

        verify(interaccionService, times(1)).findAll();
    }

    @Test
    void getById_WhenInteraccionExists_ShouldReturnInteraccion() throws Exception {
        // Arrange
        when(interaccionService.findById(1L)).thenReturn(interaccion);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("LIKE"));

        verify(interaccionService, times(1)).findById(1L);
    }

    @Test
    void getById_WhenInteraccionDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(interaccionService.findById(999L)).thenThrow(new RuntimeException("Interaccion no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Interaccion no encontrada"));

        verify(interaccionService, times(1)).findById(999L);
    }

    @Test
    void create_WhenValidInteraccion_ShouldReturnCreated() throws Exception {
        // Arrange
        when(interaccionService.save(any(Interaccion.class))).thenReturn(interaccion);

        // Act & Assert
        mockMvc.perform(post("/api/interacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(interaccion)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(interaccionService, times(1)).save(any(Interaccion.class));
    }


    @Test
    void delete_WhenInteraccionExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(interaccionService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/interacciones/1"))
                .andExpect(status().isNoContent());

        verify(interaccionService, times(1)).deleteById(1L);
    }

    @Test
    void delete_WhenInteraccionDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Interaccion no encontrada")).when(interaccionService).deleteById(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/interacciones/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Interaccion no encontrada"));

        verify(interaccionService, times(1)).deleteById(999L);
    }

    @Test
    void countLikesByPublicacion_ShouldReturnCount() throws Exception {
        // Arrange
        when(interaccionService.countLikesByPublicacion(1L)).thenReturn(5L);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/publicacion/1/likes"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(interaccionService, times(1)).countLikesByPublicacion(1L);
    }

    @Test
    void countDislikesByPublicacion_ShouldReturnCount() throws Exception {
        // Arrange
        when(interaccionService.countDislikesByPublicacion(1L)).thenReturn(2L);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/publicacion/1/dislikes"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(interaccionService, times(1)).countDislikesByPublicacion(1L);
    }

    @Test
    void countLikesByComentario_ShouldReturnCount() throws Exception {
        // Arrange
        when(interaccionService.countLikesByComentario(1L)).thenReturn(3L);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/comentario/1/likes"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(interaccionService, times(1)).countLikesByComentario(1L);
    }

    @Test
    void countDislikesByComentario_ShouldReturnCount() throws Exception {
        // Arrange
        when(interaccionService.countDislikesByComentario(1L)).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/comentario/1/dislikes"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(interaccionService, times(1)).countDislikesByComentario(1L);
    }

    @Test
    void getByPublicacion_ShouldReturnInteracciones() throws Exception {
        // Arrange
        when(interaccionService.findByPublicacionId(1L)).thenReturn(interacciones);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/publicacion/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(interaccionService, times(1)).findByPublicacionId(1L);
    }

    @Test
    void getByComentario_ShouldReturnInteracciones() throws Exception {
        // Arrange
        when(interaccionService.findByComentarioId(1L)).thenReturn(interacciones);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/comentario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(interaccionService, times(1)).findByComentarioId(1L);
    }

    @Test
    void getByUsuario_WhenInteraccionesExist_ShouldReturnList() throws Exception {
        // Arrange
        when(interaccionService.findByUsuarioId(1L)).thenReturn(interacciones);

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(interaccionService, times(1)).findByUsuarioId(1L);
    }

    @Test
    void getByUsuario_WhenNoInteracciones_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(interaccionService.findByUsuarioId(1L)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/interacciones/usuario/1"))
                .andExpect(status().isNoContent());

        verify(interaccionService, times(1)).findByUsuarioId(1L);
    }
} 