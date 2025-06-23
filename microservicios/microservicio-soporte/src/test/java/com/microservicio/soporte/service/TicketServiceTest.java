package com.microservicio.soporte.service;

import com.microservicio.soporte.client.UsuarioClient;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UsuarioClient usuarioClient;

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
        when(ticketRepository.findAll()).thenReturn(tickets);

        List<Ticket> result = ticketService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenTicketExists_ShouldReturnTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Ticket", result.getTitulo());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenTicketDoesNotExist_ShouldThrowException() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.findById(999L));

        verify(ticketRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.save(ticket);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void deleteById_ShouldDeleteSuccessfully() {
        when(ticketRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ticketRepository).deleteById(1L);

        ticketService.deleteById(1L);

        verify(ticketRepository, times(1)).existsById(1L);
        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenTicketDoesNotExist_ShouldThrowException() {
        when(ticketRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ticketService.deleteById(999L));

        verify(ticketRepository, times(1)).existsById(999L);
        verify(ticketRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByUsuarioId_ShouldReturnTickets() {
        when(ticketRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(ticket));

        List<Ticket> result = ticketService.findByUsuarioId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        verify(ticketRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    void findByModeradorId_ShouldReturnTickets() {
        when(ticketRepository.findByModeradorId(1L)).thenReturn(Arrays.asList(tickets.get(1)));

        List<Ticket> result = ticketService.findByModeradorId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getModeradorId());
        verify(ticketRepository, times(1)).findByModeradorId(1L);
    }

    @Test
    void findByEstado_ShouldReturnTickets() {
        when(ticketRepository.findByEstado(Ticket.EstadoTicket.ABIERTO)).thenReturn(Arrays.asList(ticket));

        List<Ticket> result = ticketService.findByEstado(Ticket.EstadoTicket.ABIERTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Ticket.EstadoTicket.ABIERTO, result.get(0).getEstado());
        verify(ticketRepository, times(1)).findByEstado(Ticket.EstadoTicket.ABIERTO);
    }

    @Test
    void findByCategoria_ShouldReturnTickets() {
        when(ticketRepository.findByCategoria(Ticket.CategoriaTicket.TECNICO)).thenReturn(Arrays.asList(ticket));

        List<Ticket> result = ticketService.findByCategoria(Ticket.CategoriaTicket.TECNICO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Ticket.CategoriaTicket.TECNICO, result.get(0).getCategoria());
        verify(ticketRepository, times(1)).findByCategoria(Ticket.CategoriaTicket.TECNICO);
    }

    @Test
    void update_ShouldReturnUpdatedTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket ticketActualizado = new Ticket();
        ticketActualizado.setTitulo("Updated Title");
        ticketActualizado.setDescripcion("Updated Description");
        ticketActualizado.setCategoria(Ticket.CategoriaTicket.TECNICO);
        ticketActualizado.setEstado(Ticket.EstadoTicket.ABIERTO);

        Ticket result = ticketService.update(1L, ticketActualizado);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitulo());
        assertEquals("Updated Description", result.getDescripcion());
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void update_WhenTicketDoesNotExist_ShouldThrowException() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.update(999L, ticket));

        verify(ticketRepository, times(1)).findById(999L);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void asignarModerador_ShouldReturnUpdatedTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.asignarModerador(1L, 2L);

        assertNotNull(result);
        assertEquals(2L, result.getModeradorId());
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void asignarModerador_WhenTicketDoesNotExist_ShouldThrowException() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.asignarModerador(999L, 2L));

        verify(ticketRepository, times(1)).findById(999L);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void cambiarEstado_ShouldReturnUpdatedTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.cambiarEstado(1L, Ticket.EstadoTicket.EN_PROCESO);

        assertNotNull(result);
        assertEquals(Ticket.EstadoTicket.EN_PROCESO, result.getEstado());
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void cambiarEstado_WhenTicketDoesNotExist_ShouldThrowException() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.cambiarEstado(999L, Ticket.EstadoTicket.EN_PROCESO));

        verify(ticketRepository, times(1)).findById(999L);
        verify(ticketRepository, never()).save(any());
    }
}
