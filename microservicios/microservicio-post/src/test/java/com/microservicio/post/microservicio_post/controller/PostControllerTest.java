package com.microservicio.post.microservicio_post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.post.microservicio_post.model.Post;
import com.microservicio.post.microservicio_post.services.PostService;
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
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Post post;
    private List<Post> posts;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        objectMapper = new ObjectMapper();

        post = new Post();
        post.setId(1L);
        post.setTitulo("Test Post");
        post.setContenido("Test Content");
        post.setIdForo(1L);
        post.setIdUsuario(1L);
        post.setFechaCreacion(LocalDateTime.now());

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitulo("Test Post 2");
        post2.setContenido("Test Content 2");
        post2.setIdForo(1L);
        post2.setIdUsuario(2L);
        post2.setFechaCreacion(LocalDateTime.now());

        posts = Arrays.asList(post, post2);
    }

    @Test
    void listarPublicaciones_WhenPostsExist_ShouldReturnList() throws Exception {
        // Arrange
        when(postService.listarPublicaciones()).thenReturn(posts);

        // Act & Assert
        mockMvc.perform(get("/api/publicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(postService, times(1)).listarPublicaciones();
    }

    @Test
    void listarPublicaciones_WhenNoPosts_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(postService.listarPublicaciones()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/publicaciones"))
                .andExpect(status().isNoContent());

        verify(postService, times(1)).listarPublicaciones();
    }

    @Test
    void obtenerPublicacion_WhenPostExists_ShouldReturnPost() throws Exception {
        // Arrange
        when(postService.buscarPublicacionPorId(1L)).thenReturn(post);

        // Act & Assert
        mockMvc.perform(get("/api/publicaciones/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Test Post"));

        verify(postService, times(1)).buscarPublicacionPorId(1L);
    }

    @Test
    void obtenerPublicacion_WhenPostDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(postService.buscarPublicacionPorId(999L)).thenThrow(new RuntimeException("Publicacion no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api/publicaciones/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Publicacion no encontrada"));

        verify(postService, times(1)).buscarPublicacionPorId(999L);
    }

    @Test
    void crearPublicacion_WhenValidPost_ShouldReturnCreated() throws Exception {
        // Arrange
        when(postService.guardarPublicacion(any(Post.class))).thenReturn(post);

        // Act & Assert
        mockMvc.perform(post("/api/publicaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(postService, times(1)).guardarPublicacion(any(Post.class));
    }

    @Test
    void crearPublicacion_WhenInvalidPost_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(postService.guardarPublicacion(any(Post.class))).thenThrow(new RuntimeException("Error de validación"));

        // Act & Assert
        mockMvc.perform(post("/api/publicaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al crear la publicacion")));

        verify(postService, times(1)).guardarPublicacion(any(Post.class));
    }

    @Test
    void actualizarPublicacion_WhenValidData_ShouldReturnUpdatedPost() throws Exception {
        // Arrange
        when(postService.actualizarPost(1L, post)).thenReturn(post);

        // Act & Assert
        mockMvc.perform(put("/api/publicaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(postService, times(1)).actualizarPost(1L, post);
    }

    @Test
    void actualizarPublicacion_WhenInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(postService.actualizarPost(1L, post)).thenThrow(new RuntimeException("Error de validación"));

        // Act & Assert
        mockMvc.perform(put("/api/publicaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al actualizar la publicación")));

        verify(postService, times(1)).actualizarPost(1L, post);
    }

    @Test
    void borrarPublicacion_WhenPostExists_ShouldReturnOk() throws Exception {
        // Arrange
        when(postService.borrarPubliacionPorId(1L)).thenReturn("Publicacion Eliminada");

        // Act & Assert
        mockMvc.perform(delete("/api/publicaciones/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Publicacion Eliminada"));

        verify(postService, times(1)).borrarPubliacionPorId(1L);
    }

    @Test
    void borrarPublicacion_WhenPostDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(postService.borrarPubliacionPorId(999L)).thenThrow(new RuntimeException("Publicacion no encontrada"));

        // Act & Assert
        mockMvc.perform(delete("/api/publicaciones/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Publicacion no encontrada"));

        verify(postService, times(1)).borrarPubliacionPorId(999L);
    }

    @Test
    void listarPublicacionesPorUsuario_WhenPostsExist_ShouldReturnList() throws Exception {
        // Arrange
        when(postService.listarPublicacionesPorUsuario(1L)).thenReturn(posts);

        // Act & Assert
        mockMvc.perform(get("/api/publicaciones/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(postService, times(1)).listarPublicacionesPorUsuario(1L);
    }

    @Test
    void listarPublicacionesPorUsuario_WhenNoPosts_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(postService.listarPublicacionesPorUsuario(1L)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/publicaciones/usuario/1"))
                .andExpect(status().isNoContent());

        verify(postService, times(1)).listarPublicacionesPorUsuario(1L);
    }

    @Test
    void listarPublicacionesPorForo_WhenPostsExist_ShouldReturnList() throws Exception {
        // Arrange
        when(postService.listarPublicacionesPorForo(1L)).thenReturn(posts);

        // Act & Assert
        mockMvc.perform(get("/api/publicaciones/foro/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(postService, times(1)).listarPublicacionesPorForo(1L);
    }

    @Test
    void listarPublicacionesPorForo_WhenNoPosts_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(postService.listarPublicacionesPorForo(1L)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/publicaciones/foro/1"))
                .andExpect(status().isNoContent());

        verify(postService, times(1)).listarPublicacionesPorForo(1L);
    }
} 