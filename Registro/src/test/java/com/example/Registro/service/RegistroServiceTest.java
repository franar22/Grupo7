package com.example.Registro.service;

import com.example.Registro.Service.RegistroService;
import com.example.Registro.clients.UsuarioClient;
import com.example.Registro.dto.UsuarioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistroServiceTest {

    private RegistroService registroService;
    private UsuarioClient usuarioClient;

    @BeforeEach
    void setUp() {
        usuarioClient = mock(UsuarioClient.class);
        registroService = new RegistroService();
        
        try {
            var field = RegistroService.class.getDeclaredField("usuarioClient");
            field.setAccessible(true);
            field.set(registroService, usuarioClient);
        } catch (Exception e) {
            throw new RuntimeException("Error inyectando dependencias", e);
        }
    }

    @Test
    void registrarUsuario_exito() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(1L, "juanito", "juan@example.com", "123456");

        when(usuarioClient.sincronizarUsuario(usuarioDTO)).thenReturn(Mono.just(true));

        UsuarioDTO resultado = registroService.registrarUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("juanito", resultado.getNombreUsuario());
        assertEquals("juan@example.com", resultado.getCorreo());
    }

}