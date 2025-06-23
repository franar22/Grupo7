package com.microservicio.anuncios.service;

import com.microservicio.anuncios.model.Anuncio;
import com.microservicio.anuncios.repository.AnuncioRepository;
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
class AnuncioServiceTest {

    @Mock
    private AnuncioRepository anuncioRepository;

    @InjectMocks
    private AnuncioService anuncioService;

    private Anuncio anuncio;
    private List<Anuncio> anuncios;

    @BeforeEach
    void setUp() {
        anuncio = new Anuncio();
        anuncio.setId(1L);
        anuncio.setMensaje("Test Announcement");
        anuncio.setModeradorId(1L);
        anuncio.setForoId(1L);
        anuncio.setPublicacionId(null);
        anuncio.setFechaCreacion(LocalDateTime.now());

        Anuncio anuncio2 = new Anuncio();
        anuncio2.setId(2L);
        anuncio2.setMensaje("Test Announcement 2");
        anuncio2.setModeradorId(1L);
        anuncio2.setForoId(1L);
        anuncio2.setPublicacionId(1L);
        anuncio2.setFechaCreacion(LocalDateTime.now());

        anuncios = Arrays.asList(anuncio, anuncio2);
    }

    @Test
    void findAll_ShouldReturnAllAnuncios() {
        // Arrange
        when(anuncioRepository.findAll()).thenReturn(anuncios);

        // Act
        List<Anuncio> result = anuncioService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(anuncioRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenAnuncioExists_ShouldReturnAnuncio() {
        // Arrange
        when(anuncioRepository.findById(1L)).thenReturn(Optional.of(anuncio));

        // Act
        Anuncio result = anuncioService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Announcement", result.getMensaje());
        verify(anuncioRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenAnuncioDoesNotExist_ShouldThrowException() {
        // Arrange
        when(anuncioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            anuncioService.findById(999L);
        });
        verify(anuncioRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedAnuncio() {
        // Arrange
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        // Act
        Anuncio result = anuncioService.save(anuncio);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(anuncioRepository, times(1)).save(anuncio);
    }

    @Test
    void deleteById_WhenAnuncioExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(anuncioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(anuncioRepository).deleteById(1L);

        // Act
        anuncioService.deleteById(1L);

        // Assert
        verify(anuncioRepository, times(1)).existsById(1L);
        verify(anuncioRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenAnuncioDoesNotExist_ShouldThrowException() {
        // Arrange
        when(anuncioRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            anuncioService.deleteById(999L);
        });
        verify(anuncioRepository, times(1)).existsById(999L);
        verify(anuncioRepository, never()).deleteById(any());
    }

    @Test
    void findByForoId_ShouldReturnAnuncios() {
        // Arrange
        when(anuncioRepository.findByForoId(1L)).thenReturn(anuncios);

        // Act
        List<Anuncio> result = anuncioService.findByForoId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(anuncioRepository, times(1)).findByForoId(1L);
    }

    @Test
    void findByPublicacionId_ShouldReturnAnuncios() {
        // Arrange
        Anuncio anuncio2 = anuncios.get(1); // Obtener el segundo anuncio que tiene publicacionId
        when(anuncioRepository.findByPublicacionId(1L)).thenReturn(Arrays.asList(anuncio2));

        // Act
        List<Anuncio> result = anuncioService.findByPublicacionId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getPublicacionId());
        verify(anuncioRepository, times(1)).findByPublicacionId(1L);
    }
} 