package com.microservicio.reportes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservicio.reportes.model.Reporte;
import com.microservicio.reportes.service.ReporteService;
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
class ReporteControllerTest {

    @Mock
    private ReporteService reporteService;

    @InjectMocks
    private ReporteController reporteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Reporte reporte;
    private List<Reporte> reportes;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reporteController).build();
        objectMapper = new ObjectMapper();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTitulo("Test Report");
        reporte.setDescripcion("Test Description");
        reporte.setTipoReporte("SPAM");
        reporte.setIdForo(1L);
        reporte.setIdCategoria(1L);
        reporte.setIdUsuarioReportante(1L);
        reporte.setIdUsuarioReportado(2L);
        reporte.setEstado("PENDIENTE");
        reporte.setFechaCreacion(LocalDateTime.now());

        Reporte reporte2 = new Reporte();
        reporte2.setId(2L);
        reporte2.setTitulo("Test Report 2");
        reporte2.setDescripcion("Test Description 2");
        reporte2.setTipoReporte("INAPPROPRIATE");
        reporte2.setIdForo(1L);
        reporte2.setIdCategoria(1L);
        reporte2.setIdUsuarioReportante(2L);
        reporte2.setIdUsuarioReportado(3L);
        reporte2.setEstado("RESUELTO");
        reporte2.setFechaCreacion(LocalDateTime.now());

        reportes = Arrays.asList(reporte, reporte2);
    }

    @Test
    void getAllReportes_ShouldReturnList() throws Exception {
        // Arrange
        when(reporteService.findAll()).thenReturn(reportes);

        // Act & Assert
        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(reporteService, times(1)).findAll();
    }

    @Test
    void getReporteById_WhenReporteExists_ShouldReturnReporte() throws Exception {
        // Arrange
        when(reporteService.findById(1L)).thenReturn(reporte);

        // Act & Assert
        mockMvc.perform(get("/api/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Test Report"));

        verify(reporteService, times(1)).findById(1L);
    }

    @Test
    void createReporte_WhenValidReporte_ShouldReturnCreated() throws Exception {
        // Arrange
        when(reporteService.save(any(Reporte.class))).thenReturn(reporte);

        // Act & Assert
        mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reporte)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(reporteService, times(1)).save(any(Reporte.class));
    }

    @Test
    void updateReporte_WhenValidData_ShouldReturnUpdatedReporte() throws Exception {
        // Arrange
        when(reporteService.save(any(Reporte.class))).thenReturn(reporte);

        // Act & Assert
        mockMvc.perform(put("/api/reportes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reporte)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(reporteService, times(1)).save(any(Reporte.class));
    }

    @Test
    void deleteReporte_ShouldReturnOk() throws Exception {
        // Arrange
        doNothing().when(reporteService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isOk());

        verify(reporteService, times(1)).deleteById(1L);
    }

    @Test
    void getReportesByEstado_ShouldReturnReportes() throws Exception {
        // Arrange
        when(reporteService.findByEstado("PENDIENTE")).thenReturn(Arrays.asList(reporte));

        // Act & Assert
        mockMvc.perform(get("/api/reportes/estado/PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));

        verify(reporteService, times(1)).findByEstado("PENDIENTE");
    }

    @Test
    void getReportesByUsuarioReportante_ShouldReturnReportes() throws Exception {
        // Arrange
        when(reporteService.findByIdUsuarioReportante(1L)).thenReturn(Arrays.asList(reporte));

        // Act & Assert
        mockMvc.perform(get("/api/reportes/usuario-reportante/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idUsuarioReportante").value(1));

        verify(reporteService, times(1)).findByIdUsuarioReportante(1L);
    }

    @Test
    void getReportesByUsuarioReportado_ShouldReturnReportes() throws Exception {
        // Arrange
        when(reporteService.findByIdUsuarioReportado(2L)).thenReturn(Arrays.asList(reporte));

        // Act & Assert
        mockMvc.perform(get("/api/reportes/usuario-reportado/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idUsuarioReportado").value(2));

        verify(reporteService, times(1)).findByIdUsuarioReportado(2L);
    }

    @Test
    void getReportesByForo_ShouldReturnReportes() throws Exception {
        // Arrange
        when(reporteService.findByIdForo(1L)).thenReturn(reportes);

        // Act & Assert
        mockMvc.perform(get("/api/reportes/foro/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idForo").value(1))
                .andExpect(jsonPath("$[1].idForo").value(1));

        verify(reporteService, times(1)).findByIdForo(1L);
    }

    @Test
    void getReportesByCategoria_ShouldReturnReportes() throws Exception {
        // Arrange
        when(reporteService.findByIdCategoria(1L)).thenReturn(reportes);

        // Act & Assert
        mockMvc.perform(get("/api/reportes/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idCategoria").value(1))
                .andExpect(jsonPath("$[1].idCategoria").value(1));

        verify(reporteService, times(1)).findByIdCategoria(1L);
    }

    @Test
    void getReportesByTipo_ShouldReturnReportes() throws Exception {
        // Arrange
        when(reporteService.findByTipoReporte("SPAM")).thenReturn(Arrays.asList(reporte));

        // Act & Assert
        mockMvc.perform(get("/api/reportes/tipo/SPAM"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tipoReporte").value("SPAM"));

        verify(reporteService, times(1)).findByTipoReporte("SPAM");
    }

    @Test
    void updateEstado_ShouldReturnUpdatedReporte() throws Exception {
        // Arrange
        when(reporteService.updateEstado(1L, "RESUELTO")).thenReturn(reporte);

        // Act & Assert
        mockMvc.perform(patch("/api/reportes/1/estado")
                        .param("nuevoEstado", "RESUELTO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(reporteService, times(1)).updateEstado(1L, "RESUELTO");
    }
} 