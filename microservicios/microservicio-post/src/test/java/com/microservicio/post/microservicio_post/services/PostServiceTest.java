package com.microservicio.post.microservicio_post.services;

import com.microservicio.post.microservicio_post.client.ForoClient;
import com.microservicio.post.microservicio_post.client.UsuarioClient;
import com.microservicio.post.microservicio_post.model.Post;
import com.microservicio.post.microservicio_post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ForoClient foroClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private PostService postService;

    private Post post;
    private List<Post> posts;
    private Map<String, Object> foroResponse;
    private Map<String, Object> usuarioResponse;

    @BeforeEach
    void setUp() {
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

        foroResponse = new HashMap<>();
        foroResponse.put("id", 1L);
        foroResponse.put("nombre", "Test Forum");

        usuarioResponse = new HashMap<>();
        usuarioResponse.put("id", 1L);
        usuarioResponse.put("nombre", "Test User");
    }

    @Test
    void listarPublicaciones_ShouldReturnAllPosts() {
        // Arrange
        when(postRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> result = postService.listarPublicaciones();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void buscarPublicacionPorId_WhenPostExists_ShouldReturnPost() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act
        Post result = postService.buscarPublicacionPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Post", result.getTitulo());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPublicacionPorId_WhenPostDoesNotExist_ShouldThrowException() {
        // Arrange
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            postService.buscarPublicacionPorId(999L);
        });
        verify(postRepository, times(1)).findById(999L);
    }

    @Test
    void borrarPublicacionPorId_WhenPostExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(1L);

        // Act
        String result = postService.borrarPubliacionPorId(1L);

        // Assert
        assertEquals("Publicacion Eliminada", result);
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    void borrarPublicacionPorId_WhenPostDoesNotExist_ShouldThrowException() {
        // Arrange
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            postService.borrarPubliacionPorId(999L);
        });
        verify(postRepository, times(1)).findById(999L);
        verify(postRepository, never()).deleteById(any());
    }

    @Test
    void guardarPublicacion_WhenValidData_ShouldReturnSavedPost() {
        // Arrange
        when(foroClient.obtenerForoPorId(1L)).thenReturn(foroResponse);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Act
        Post result = postService.guardarPublicacion(post);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(foroClient, times(1)).obtenerForoPorId(1L);
        verify(usuarioClient, times(1)).obtenerUsuarioPorId(1L);
        verify(postRepository, times(1)).save(post);
    }
    
    public Post guardarPublicacion(Post post) {
    if (foroClient.obtenerForoPorId(post.getIdForo()) == null) {
        throw new RuntimeException("El id del Foro no se ha encontrado. No se puede crear un comentario.");
    }

    if (usuarioClient.obtenerUsuarioPorId(post.getIdUsuario()) == null) {
        throw new RuntimeException("El id del Usuario no se ha encontrado. No se puede crear un comentario.");
    }

    return postRepository.save(post);
}


    @Test
    void guardarPublicacion_WhenUsuarioDoesNotExist_ShouldThrowException() {
        // Arrange
        when(foroClient.obtenerForoPorId(1L)).thenReturn(foroResponse);
        when(usuarioClient.obtenerUsuarioPorId(999L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            post.setIdUsuario(999L);
            postService.guardarPublicacion(post);
        });
        assertEquals("El id del usuario no existe. No es psible realizar un comentario", exception.getMessage());
        verify(foroClient, times(1)).obtenerForoPorId(1L);
        verify(usuarioClient, times(1)).obtenerUsuarioPorId(999L);
        verify(postRepository, never()).save(any());
    }

    @Test
    void actualizarPost_WhenValidData_ShouldReturnUpdatedPost() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post postActualizado = new Post();
        postActualizado.setTitulo("Updated Title");
        postActualizado.setContenido("Updated Content");

        // Act
        Post result = postService.actualizarPost(1L, postActualizado);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitulo());
        assertEquals("Updated Content", result.getContenido());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void actualizarPost_WhenPostDoesNotExist_ShouldThrowException() {
        // Arrange
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            postService.actualizarPost(999L, post);
        });
        verify(postRepository, times(1)).findById(999L);
        verify(postRepository, never()).save(any());
    }

    @Test
    void listarPublicacionesPorUsuario_ShouldReturnUserPosts() {
        // Arrange
        when(postRepository.findByIdUsuario(1L)).thenReturn(posts);

        // Act
        List<Post> result = postService.listarPublicacionesPorUsuario(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(postRepository, times(1)).findByIdUsuario(1L);
    }

    @Test
    void listarPublicacionesPorForo_ShouldReturnForumPosts() {
        // Arrange
        when(postRepository.findByIdForo(1L)).thenReturn(posts);

        // Act
        List<Post> result = postService.listarPublicacionesPorForo(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(postRepository, times(1)).findByIdForo(1L);
    }
} 