package com.microservicio.reportes.service;

import com.microservicio.reportes.client.CategoriaClient;
import com.microservicio.reportes.client.ForoClient;
import com.microservicio.reportes.exception.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ForoClient foroClient;

    @Mock
    private CategoriaClient categoriaClient;

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
        when(reporteRepository.findAll()).thenReturn(reportes);

        List<Reporte> result = reporteService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reporteRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenReporteExists_ShouldReturnReporte() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));

        Reporte result = reporteService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Report", result.getTitulo());
        verify(reporteRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenReporteDoesNotExist_ShouldThrowException() {
        when(reporteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            reporteService.findById(999L);
        });
        verify(reporteRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedReporte() {
        when(foroClient.getForoById(anyLong())).thenReturn(new Object());
        when(categoriaClient.getCategoriaById(anyLong())).thenReturn(new Object());
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        Reporte result = reporteService.save(reporte);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reporteRepository, times(1)).save(reporte);
    }

    @Test
    void deleteById_ShouldDeleteSuccessfully() {
        when(reporteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reporteRepository).deleteById(1L);

        reporteService.deleteById(1L);

        verify(reporteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenReporteDoesNotExist_ShouldThrowException() {
        when(reporteRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            reporteService.deleteById(999L);
        });

        verify(reporteRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByEstado_ShouldReturnReportes() {
        when(reporteRepository.findByEstado("PENDIENTE")).thenReturn(Arrays.asList(reporte));

        List<Reporte> result = reporteService.findByEstado("PENDIENTE");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getEstado());
        verify(reporteRepository, times(1)).findByEstado("PENDIENTE");
    }

    @Test
    void findByIdUsuarioReportante_ShouldReturnReportes() {
        when(reporteRepository.findByIdUsuarioReportante(1L)).thenReturn(Arrays.asList(reporte));

        List<Reporte> result = reporteService.findByIdUsuarioReportante(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdUsuarioReportante());
        verify(reporteRepository, times(1)).findByIdUsuarioReportante(1L);
    }

    @Test
    void findByIdUsuarioReportado_ShouldReturnReportes() {
        when(reporteRepository.findByIdUsuarioReportado(2L)).thenReturn(Arrays.asList(reporte));

        List<Reporte> result = reporteService.findByIdUsuarioReportado(2L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getIdUsuarioReportado());
        verify(reporteRepository, times(1)).findByIdUsuarioReportado(2L);
    }

    @Test
    void findByIdForo_ShouldReturnReportes() {
        when(reporteRepository.findByIdForo(1L)).thenReturn(reportes);

        List<Reporte> result = reporteService.findByIdForo(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reporteRepository, times(1)).findByIdForo(1L);
    }

    @Test
    void findByIdCategoria_ShouldReturnReportes() {
        when(reporteRepository.findByIdCategoria(1L)).thenReturn(reportes);

        List<Reporte> result = reporteService.findByIdCategoria(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reporteRepository, times(1)).findByIdCategoria(1L);
    }

    @Test
    void findByTipoReporte_ShouldReturnReportes() {
        when(reporteRepository.findByTipoReporte("SPAM")).thenReturn(Arrays.asList(reporte));

        List<Reporte> result = reporteService.findByTipoReporte("SPAM");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SPAM", result.get(0).getTipoReporte());
        verify(reporteRepository, times(1)).findByTipoReporte("SPAM");
    }

    @Test
    void updateEstado_ShouldReturnUpdatedReporte() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        Reporte result = reporteService.updateEstado(1L, "RESUELTO");

        assertNotNull(result);
        assertEquals("RESUELTO", result.getEstado());
        verify(reporteRepository, times(1)).findById(1L);
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void updateEstado_WhenReporteDoesNotExist_ShouldThrowException() {
        when(reporteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            reporteService.updateEstado(999L, "RESUELTO");
        });

        verify(reporteRepository, times(1)).findById(999L);
        verify(reporteRepository, never()).save(any());
    }
}
