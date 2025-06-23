package com.example.Registro.controller;

import com.example.Registro.Controller.RegistroController;
import com.example.Registro.Service.RegistroService;
import com.example.Registro.dto.UsuarioDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = RegistroController.class)
@AutoConfigureMockMvc(addFilters = false) 
public class RegistroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistroService registroService;

    @Test
    void registrarUsuario_OK() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO(1L, "juan123", "juan@example.com", "123456");

        Mockito.when(registroService.registrarUsuario(any(UsuarioDTO.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreUsuario").value("juan123"))
                .andExpect(jsonPath("$.correo").value("juan@example.com"));
    }
}

