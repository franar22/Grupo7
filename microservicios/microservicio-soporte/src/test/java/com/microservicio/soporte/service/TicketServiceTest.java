package com.microservicio.soporte.service;

import com.microservicio.soporte.model.Ticket;
import com.microservicio.soporte.repository.TicketRepository;
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
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private List<Ticket> tickets;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitulo("Test Ticket");
        ticket.setDescripcion("Test Description");
        ticket.setUsuarioId(1L);
        ticket.setModeradorId(null);
        ticket.setEstado(Ticket.EstadoTicket.ABIERTO);
        ticket.setCategoria(Ticket.CategoriaTicket.TECNICO);
        ticket.setFechaCreacion(LocalDateTime.now());

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setTitulo("Test Ticket 2");
        ticket2.setDescripcion("Test Description 2");
        ticket2.setUsuarioId(2L);
        ticket2.setModeradorId(1L);
        ticket2.setEstado(Ticket.EstadoTicket.EN_PROCESO);
        ticket2.setCategoria(Ticket.CategoriaTicket.CUENTA);
        ticket2.setFechaCreacion(LocalDateTime.now());

        tickets = Arrays.asList(ticket, ticket2);
    }

    @Test
    void findAll_ShouldReturnAllTickets() {
        // Arrange
        when(ticketRepository.findAll()).thenReturn(tickets);

        // Act
        List<Ticket> result = ticketService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenTicketExists_ShouldReturnTicket() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = ticketService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Ticket", result.getTitulo());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenTicketDoesNotExist_ShouldThrowException() {
        // Arrange
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ticketService.findById(999L);
        });
        verify(ticketRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedTicket() {
        // Arrange
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        Ticket result = ticketService.save(ticket);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void deleteById_ShouldDeleteSuccessfully() {
        // Arrange
        doNothing().when(ticketRepository).deleteById(1L);

        // Act
        ticketService.deleteById(1L);

        // Assert
        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByUsuarioId_ShouldReturnTickets() {
        // Arrange
        when(ticketRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(ticket));

        // Act
        List<Ticket> result = ticketService.findByUsuarioId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        verify(ticketRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    void findByModeradorId_ShouldReturnTickets() {
        // Arrange
        Ticket ticket2 = tickets.get(1); // Obtener el segundo ticket que tiene moderadorId
        when(ticketRepository.findByModeradorId(1L)).thenReturn(Arrays.asList(ticket2));

        // Act
        List<Ticket> result = ticketService.findByModeradorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getModeradorId());
        verify(ticketRepository, times(1)).findByModeradorId(1L);
    }

    @Test
    void findByEstado_ShouldReturnTickets() {
        // Arrange
        when(ticketRepository.findByEstado(Ticket.EstadoTicket.ABIERTO)).thenReturn(Arrays.asList(ticket));

        // Act
        List<Ticket> result = ticketService.findByEstado(Ticket.EstadoTicket.ABIERTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Ticket.EstadoTicket.ABIERTO, result.get(0).getEstado());
        verify(ticketRepository, times(1)).findByEstado(Ticket.EstadoTicket.ABIERTO);
    }

    @Test
    void findByCategoria_ShouldReturnTickets() {
        // Arrange
        when(ticketRepository.findByCategoria(Ticket.CategoriaTicket.TECNICO)).thenReturn(Arrays.asList(ticket));

        // Act
        List<Ticket> result = ticketService.findByCategoria(Ticket.CategoriaTicket.TECNICO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Ticket.CategoriaTicket.TECNICO, result.get(0).getCategoria());
        verify(ticketRepository, times(1)).findByCategoria(Ticket.CategoriaTicket.TECNICO);
    }

    @Test
    void update_ShouldReturnUpdatedTicket() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket ticketActualizado = new Ticket();
        ticketActualizado.setTitulo("Updated Title");
        ticketActualizado.setDescripcion("Updated Description");

        // Act
        Ticket result = ticketService.update(1L, ticketActualizado);

        // Assert
        assertNotNull(result);
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void update_WhenTicketDoesNotExist_ShouldThrowException() {
        // Arrange
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ticketService.update(999L, ticket);
        });
        verify(ticketRepository, times(1)).findById(999L);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void asignarModerador_ShouldReturnUpdatedTicket() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        Ticket result = ticketService.asignarModerador(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getModeradorId());
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void asignarModerador_WhenTicketDoesNotExist_ShouldThrowException() {
        // Arrange
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ticketService.asignarModerador(999L, 2L);
        });
        verify(ticketRepository, times(1)).findById(999L);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void cambiarEstado_ShouldReturnUpdatedTicket() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        Ticket result = ticketService.cambiarEstado(1L, Ticket.EstadoTicket.EN_PROCESO);

        // Assert
        assertNotNull(result);
        assertEquals(Ticket.EstadoTicket.EN_PROCESO, result.getEstado());
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void cambiarEstado_WhenTicketDoesNotExist_ShouldThrowException() {
        // Arrange
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ticketService.cambiarEstado(999L, Ticket.EstadoTicket.EN_PROCESO);
        });
        verify(ticketRepository, times(1)).findById(999L);
        verify(ticketRepository, never()).save(any());
    }
} 