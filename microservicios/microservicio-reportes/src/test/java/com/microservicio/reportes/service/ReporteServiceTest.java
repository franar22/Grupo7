package com.microservicio.reportes.service;

import com.microservicio.reportes.model.Reporte;
import com.microservicio.reportes.repository.ReporteRepository;
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
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reporte;
    private List<Reporte> reportes;

    @BeforeEach
    void setUp() {
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
    void findAll_ShouldReturnAllReportes() {
        // Arrange
        when(reporteRepository.findAll()).thenReturn(reportes);

        // Act
        List<Reporte> result = reporteService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reporteRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenReporteExists_ShouldReturnReporte() {
        // Arrange
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));

        // Act
        Reporte result = reporteService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Report", result.getTitulo());
        verify(reporteRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenReporteDoesNotExist_ShouldThrowException() {
        // Arrange
        when(reporteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            reporteService.findById(999L);
        });
        verify(reporteRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedReporte() {
        // Arrange
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        // Act
        Reporte result = reporteService.save(reporte);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reporteRepository, times(1)).save(reporte);
    }

    @Test
    void deleteById_ShouldDeleteSuccessfully() {
        // Arrange
        doNothing().when(reporteRepository).deleteById(1L);

        // Act
        reporteService.deleteById(1L);

        // Assert
        verify(reporteRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByEstado_ShouldReturnReportes() {
        // Arrange
        when(reporteRepository.findByEstado("PENDIENTE")).thenReturn(Arrays.asList(reporte));

        // Act
        List<Reporte> result = reporteService.findByEstado("PENDIENTE");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getEstado());
        verify(reporteRepository, times(1)).findByEstado("PENDIENTE");
    }

    @Test
    void findByIdUsuarioReportante_ShouldReturnReportes() {
        // Arrange
        when(reporteRepository.findByIdUsuarioReportante(1L)).thenReturn(Arrays.asList(reporte));

        // Act
        List<Reporte> result = reporteService.findByIdUsuarioReportante(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdUsuarioReportante());
        verify(reporteRepository, times(1)).findByIdUsuarioReportante(1L);
    }

    @Test
    void findByIdUsuarioReportado_ShouldReturnReportes() {
        // Arrange
        when(reporteRepository.findByIdUsuarioReportado(2L)).thenReturn(Arrays.asList(reporte));

        // Act
        List<Reporte> result = reporteService.findByIdUsuarioReportado(2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getIdUsuarioReportado());
        verify(reporteRepository, times(1)).findByIdUsuarioReportado(2L);
    }

    @Test
    void findByIdForo_ShouldReturnReportes() {
        // Arrange
        when(reporteRepository.findByIdForo(1L)).thenReturn(reportes);

        // Act
        List<Reporte> result = reporteService.findByIdForo(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reporteRepository, times(1)).findByIdForo(1L);
    }

    @Test
    void findByIdCategoria_ShouldReturnReportes() {
        // Arrange
        when(reporteRepository.findByIdCategoria(1L)).thenReturn(reportes);

        // Act
        List<Reporte> result = reporteService.findByIdCategoria(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reporteRepository, times(1)).findByIdCategoria(1L);
    }

    @Test
    void findByTipoReporte_ShouldReturnReportes() {
        // Arrange
        when(reporteRepository.findByTipoReporte("SPAM")).thenReturn(Arrays.asList(reporte));

        // Act
        List<Reporte> result = reporteService.findByTipoReporte("SPAM");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SPAM", result.get(0).getTipoReporte());
        verify(reporteRepository, times(1)).findByTipoReporte("SPAM");
    }

    @Test
    void updateEstado_ShouldReturnUpdatedReporte() {
        // Arrange
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        // Act
        Reporte result = reporteService.updateEstado(1L, "RESUELTO");

        // Assert
        assertNotNull(result);
        assertEquals("RESUELTO", result.getEstado());
        verify(reporteRepository, times(1)).findById(1L);
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void updateEstado_WhenReporteDoesNotExist_ShouldThrowException() {
        // Arrange
        when(reporteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            reporteService.updateEstado(999L, "RESUELTO");
        });
        verify(reporteRepository, times(1)).findById(999L);
        verify(reporteRepository, never()).save(any());
    }
} 