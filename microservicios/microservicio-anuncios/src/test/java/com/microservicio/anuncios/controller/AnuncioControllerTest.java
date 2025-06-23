package com.microservicio.anuncios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.anuncios.model.Anuncio;
import com.microservicio.anuncios.service.AnuncioService;
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
class AnuncioControllerTest {

    @Mock
    private AnuncioService anuncioService;

    @InjectMocks
    private AnuncioController anuncioController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Anuncio anuncio;
    private List<Anuncio> anuncios;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(anuncioController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); 

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
    void getAll_WhenAnunciosExist_ShouldReturnList() throws Exception {
        // Arrange
        when(anuncioService.findAll()).thenReturn(anuncios);

        // Act & Assert
        mockMvc.perform(get("/api/anuncios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(anuncioService, times(1)).findAll();
    }

    @Test
    void getAll_WhenNoAnuncios_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(anuncioService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/anuncios"))
                .andExpect(status().isNoContent());

        verify(anuncioService, times(1)).findAll();
    }

    @Test
    void getById_WhenAnuncioExists_ShouldReturnAnuncio() throws Exception {
        // Arrange
        when(anuncioService.findById(1L)).thenReturn(anuncio);

        // Act & Assert
        mockMvc.perform(get("/api/anuncios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.mensaje").value("Test Announcement"));

        verify(anuncioService, times(1)).findById(1L);
    }

    @Test
    void getById_WhenAnuncioDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(anuncioService.findById(999L)).thenThrow(new RuntimeException("Anuncio no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/anuncios/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Anuncio no encontrado"));

        verify(anuncioService, times(1)).findById(999L);
    }

    @Test
void create_WhenValidAnuncio_ShouldReturnCreated() throws Exception {
    // Arrange: usamos objeto válido
    when(anuncioService.save(any(Anuncio.class))).thenReturn(anuncio);

    // Act & Assert
    mockMvc.perform(post("/api/anuncios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(anuncio)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1));

    verify(anuncioService, times(1)).save(any(Anuncio.class));
}

    @Test
    void delete_WhenAnuncioExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(anuncioService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/anuncios/1"))
                .andExpect(status().isNoContent());

        verify(anuncioService, times(1)).deleteById(1L);
    }

    @Test
    void delete_WhenAnuncioDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Anuncio no encontrado")).when(anuncioService).deleteById(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/anuncios/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Anuncio no encontrado"));

        verify(anuncioService, times(1)).deleteById(999L);
    }

    @Test
    void getByForo_ShouldReturnAnuncios() throws Exception {
        // Arrange
        when(anuncioService.findByForoId(1L)).thenReturn(anuncios);

        // Act & Assert
        mockMvc.perform(get("/api/anuncios/foro/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].foroId").value(1))
                .andExpect(jsonPath("$[1].foroId").value(1));

        verify(anuncioService, times(1)).findByForoId(1L);
    }

    @Test
    void getByPublicacion_ShouldReturnAnuncios() throws Exception {
        // Arrange
        Anuncio anuncio2 = anuncios.get(1);
        when(anuncioService.findByPublicacionId(1L)).thenReturn(Arrays.asList(anuncio2));

        // Act & Assert
        mockMvc.perform(get("/api/anuncios/publicacion/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].publicacionId").value(1));

        verify(anuncioService, times(1)).findByPublicacionId(1L);
    }
} 