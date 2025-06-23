package com.microservicio.soporte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservicio.soporte.model.Ticket;
import com.microservicio.soporte.service.TicketService;
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
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper; 
    private Ticket ticket;
    private List<Ticket> tickets;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 

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
    void createTicket_WhenValidTicket_ShouldReturnCreated() throws Exception {
        // Arrange
        when(ticketService.save(any(Ticket.class))).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(ticketService, times(1)).save(any(Ticket.class));
    }

    @Test
    void getTicketById_WhenTicketExists_ShouldReturnTicket() throws Exception {
        // Arrange
        when(ticketService.findById(1L)).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Test Ticket"));

        verify(ticketService, times(1)).findById(1L);
    }

    @Test
    void getAllTickets_ShouldReturnList() throws Exception {
        // Arrange
        when(ticketService.findAll()).thenReturn(tickets);

        // Act & Assert
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(ticketService, times(1)).findAll();
    }

    @Test
    void getTicketsByUsuarioId_ShouldReturnTickets() throws Exception {
        // Arrange
        when(ticketService.findByUsuarioId(1L)).thenReturn(Arrays.asList(ticket));

        // Act & Assert
        mockMvc.perform(get("/api/tickets/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].usuarioId").value(1));

        verify(ticketService, times(1)).findByUsuarioId(1L);
    }

    @Test
    void getTicketsByModeradorId_ShouldReturnTickets() throws Exception {
        // Arrange
        Ticket ticket2 = tickets.get(1);
        when(ticketService.findByModeradorId(1L)).thenReturn(Arrays.asList(ticket2));

        // Act & Assert
        mockMvc.perform(get("/api/tickets/moderador/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].moderadorId").value(1));

        verify(ticketService, times(1)).findByModeradorId(1L);
    }

    @Test
    void getTicketsByEstado_ShouldReturnTickets() throws Exception {
        // Arrange
        when(ticketService.findByEstado(Ticket.EstadoTicket.ABIERTO)).thenReturn(Arrays.asList(ticket));

        // Act & Assert
        mockMvc.perform(get("/api/tickets/estado/ABIERTO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].estado").value("ABIERTO"));

        verify(ticketService, times(1)).findByEstado(Ticket.EstadoTicket.ABIERTO);
    }

    @Test
    void getTicketsByCategoria_ShouldReturnTickets() throws Exception {
        // Arrange
        when(ticketService.findByCategoria(Ticket.CategoriaTicket.TECNICO)).thenReturn(Arrays.asList(ticket));

        // Act & Assert
        mockMvc.perform(get("/api/tickets/categoria/TECNICO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].categoria").value("TECNICO"));

        verify(ticketService, times(1)).findByCategoria(Ticket.CategoriaTicket.TECNICO);
    }

    @Test
    void updateTicket_WhenValidData_ShouldReturnUpdatedTicket() throws Exception {
        // Arrange
        when(ticketService.update(1L, ticket)).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(put("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(ticketService, times(1)).update(1L, ticket);
    }

    @Test
    void deleteTicket_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(ticketService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());

        verify(ticketService, times(1)).deleteById(1L);
    }

    @Test
    void asignarModerador_ShouldReturnUpdatedTicket() throws Exception {
        // Arrange
        when(ticketService.asignarModerador(1L, 2L)).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(put("/api/tickets/1/asignar/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(ticketService, times(1)).asignarModerador(1L, 2L);
    }

    @Test
    void cambiarEstado_ShouldReturnUpdatedTicket() throws Exception {
        // Arrange
        when(ticketService.cambiarEstado(1L, Ticket.EstadoTicket.EN_PROCESO)).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(put("/api/tickets/1/estado/EN_PROCESO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(ticketService, times(1)).cambiarEstado(1L, Ticket.EstadoTicket.EN_PROCESO);
    }
} 