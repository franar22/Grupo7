package com.microservicio.interacciones.service;

import com.microservicio.interacciones.exception.ResourceNotFoundException;
import com.microservicio.interacciones.model.Interaccion;
import com.microservicio.interacciones.repository.InteraccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteraccionServiceTest {

    @Mock
    private InteraccionRepository interaccionRepository;

    @InjectMocks
    private InteraccionService interaccionService;

    private Interaccion interaccion;
    private List<Interaccion> interacciones;

    @BeforeEach
    void setUp() {
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
    void findAll_ShouldReturnAllInteracciones() {
        // Arrange
        when(interaccionRepository.findAll()).thenReturn(interacciones);

        // Act
        List<Interaccion> result = interaccionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(interaccionRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenInteraccionExists_ShouldReturnInteraccion() {
        // Arrange
        when(interaccionRepository.findById(1L)).thenReturn(Optional.of(interaccion));

        // Act
        Interaccion result = interaccionService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Interaccion.TipoInteraccion.LIKE, result.getTipo());
        verify(interaccionRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenInteraccionDoesNotExist_ShouldThrowException() {
        // Arrange
        when(interaccionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            interaccionService.findById(999L);
        });
        verify(interaccionRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedInteraccion() {
        // Arrange
        when(interaccionRepository.save(any(Interaccion.class))).thenReturn(interaccion);

        // Act
        Interaccion result = interaccionService.save(interaccion);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(interaccionRepository, times(1)).save(interaccion);
    }

    @Test
    void deleteById_WhenInteraccionExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(interaccionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(interaccionRepository).deleteById(1L);

        // Act
        interaccionService.deleteById(1L);

        // Assert
        verify(interaccionRepository, times(1)).existsById(1L);
        verify(interaccionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenInteraccionDoesNotExist_ShouldThrowException() {
        // Arrange
        when(interaccionRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            interaccionService.deleteById(999L);
        });
        verify(interaccionRepository, times(1)).existsById(999L);
        verify(interaccionRepository, never()).deleteById(any());
    }

    @Test
    void countLikesByPublicacion_ShouldReturnCorrectCount() {
        // Arrange
        when(interaccionRepository.countByTipoAndPublicacionId(Interaccion.TipoInteraccion.LIKE, 1L)).thenReturn(5L);

        // Act
        long result = interaccionService.countLikesByPublicacion(1L);

        // Assert
        assertEquals(5L, result);
        verify(interaccionRepository, times(1)).countByTipoAndPublicacionId(Interaccion.TipoInteraccion.LIKE, 1L);
    }

    @Test
    void countDislikesByPublicacion_ShouldReturnCorrectCount() {
        // Arrange
        when(interaccionRepository.countByTipoAndPublicacionId(Interaccion.TipoInteraccion.DISLIKE, 1L)).thenReturn(2L);

        // Act
        long result = interaccionService.countDislikesByPublicacion(1L);

        // Assert
        assertEquals(2L, result);
        verify(interaccionRepository, times(1)).countByTipoAndPublicacionId(Interaccion.TipoInteraccion.DISLIKE, 1L);
    }

    @Test
    void countLikesByComentario_ShouldReturnCorrectCount() {
        // Arrange
        when(interaccionRepository.countByTipoAndComentarioId(Interaccion.TipoInteraccion.LIKE, 1L)).thenReturn(3L);

        // Act
        long result = interaccionService.countLikesByComentario(1L);

        // Assert
        assertEquals(3L, result);
        verify(interaccionRepository, times(1)).countByTipoAndComentarioId(Interaccion.TipoInteraccion.LIKE, 1L);
    }

    @Test
    void countDislikesByComentario_ShouldReturnCorrectCount() {
        // Arrange
        when(interaccionRepository.countByTipoAndComentarioId(Interaccion.TipoInteraccion.DISLIKE, 1L)).thenReturn(1L);

        // Act
        long result = interaccionService.countDislikesByComentario(1L);

        // Assert
        assertEquals(1L, result);
        verify(interaccionRepository, times(1)).countByTipoAndComentarioId(Interaccion.TipoInteraccion.DISLIKE, 1L);
    }

    @Test
    void findByPublicacionId_ShouldReturnInteracciones() {
        // Arrange
        when(interaccionRepository.findByPublicacionId(1L)).thenReturn(interacciones);

        // Act
        List<Interaccion> result = interaccionService.findByPublicacionId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(interaccionRepository, times(1)).findByPublicacionId(1L);
    }

    @Test
    void findByComentarioId_ShouldReturnInteracciones() {
        // Arrange
        when(interaccionRepository.findByComentarioId(1L)).thenReturn(interacciones);

        // Act
        List<Interaccion> result = interaccionService.findByComentarioId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(interaccionRepository, times(1)).findByComentarioId(1L);
    }

    @Test
    void findByUsuarioId_ShouldReturnInteracciones() {
        // Arrange
        when(interaccionRepository.findByUsuarioId(1L)).thenReturn(interacciones);

        // Act
        List<Interaccion> result = interaccionService.findByUsuarioId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(interaccionRepository, times(1)).findByUsuarioId(1L);
    }
} 