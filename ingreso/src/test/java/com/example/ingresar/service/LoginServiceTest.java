package com.example.ingresar.service;

import com.example.ingresar.clients.UsuarioClient;
import com.example.ingresar.dto.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class LoginServiceTest {

    private LoginService loginService;
    private UsuarioClient usuarioClient;

    @BeforeEach
    void setUp() throws Exception {
        usuarioClient = mock(UsuarioClient.class);
        loginService = new LoginService();

        
        Field field = LoginService.class.getDeclaredField("usuarioClient");
        field.setAccessible(true);
        field.set(loginService, usuarioClient);
    }

    @Test
    void iniciarSesion_Exitoso() {
        LoginDTO loginDTO = new LoginDTO("usuario@example.com", "password123");

        when(usuarioClient.verificarCredenciales(loginDTO)).thenReturn(Mono.just(true));

        StepVerifier.create(loginService.iniciarSesion(loginDTO))
                .expectNext(true)
                .verifyComplete();

        verify(usuarioClient, times(1)).verificarCredenciales(loginDTO);
    }

 }
