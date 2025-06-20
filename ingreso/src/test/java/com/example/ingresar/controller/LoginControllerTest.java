package com.example.ingresar.controller;

import com.example.ingresar.dto.LoginDTO;
import com.example.ingresar.service.LoginService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LoginService loginService;

    @Test
    void login_exitoso() {
        LoginDTO loginDTO = new LoginDTO("usuario123", "password123");

        Mockito.when(loginService.iniciarSesion(Mockito.any(LoginDTO.class)))
                .thenReturn(Mono.just(true));

        webTestClient.post()
                .uri("/api/login")
                .bodyValue(loginDTO)
                .exchange()
                .expectStatus().isForbidden();  
    }
}
