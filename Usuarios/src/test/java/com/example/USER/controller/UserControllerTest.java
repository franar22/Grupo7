package com.example.USER.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.USER.config.SecurityConfig;
import com.example.USER.dto.LoginDTO;
import com.example.USER.dto.UsuarioDTO;
import com.example.USER.model.Usuarios;
import com.example.USER.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)  
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void listaDeUsuarios_retornaLista() throws Exception {
        List<Usuarios> usuarios = Arrays.asList(new Usuarios(1L, 2L, "usuario", "correo@mail.com", "clave", null, null, null));
        when(userService.listarUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void buscarUsuarioPorId_retornaUsuario() throws Exception {
        Usuarios usuario = new Usuarios(1L, 2L, "usuario", "correo@mail.com", "clave", null, null, null);
        when(userService.obtenerUsuarioPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void crearUsuarioDesdeDTO_retornaCreado() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO(null, "usuario", "correo@mail.com", "clave");
        Usuarios usuarioGuardado = new Usuarios(1L, null, "usuario", "correo@mail.com", "clave", null, null, null);

        when(userService.guardarUsuario(any(Usuarios.class))).thenReturn(usuarioGuardado);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombreUsuario").value("usuario"))
            .andExpect(jsonPath("$.correo").value("correo@mail.com"));
    }

    @Test
    void loginUsuario_exitoso() throws Exception {
        LoginDTO loginDTO = new LoginDTO("correo@mail.com", "clave");
        Usuarios usuario = new Usuarios(1L, null, "usuario", "correo@mail.com", "clave_encriptada", null, null, null);

        when(userService.obtenerUsuarioPorCorreo("correo@mail.com")).thenReturn(usuario);
        when(passwordEncoder.matches("clave", "clave_encriptada")).thenReturn(true);

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string("Login exitoso"));
    }

    @Test
    void actualizarInformacionUsuario_ok() throws Exception {
        Usuarios usuarioActualizado = new Usuarios();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setIdRol(1L);
        usuarioActualizado.setNombreUsuario("usuarioNuevo");
        usuarioActualizado.setCorreo("nuevo@mail.com");
        usuarioActualizado.setPassword("clave123");
        usuarioActualizado.setNombre("Juan");
        usuarioActualizado.setApellidos("Pérez");

        when(userService.actualizarUsuario(eq(1L), any(Usuarios.class))).thenReturn(usuarioActualizado);

        String json = objectMapper.writeValueAsString(usuarioActualizado);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andDo(result -> {
                if (result.getResponse().getStatus() != 200) {
                    System.out.println("Error en respuesta:");
                    System.out.println(result.getResponse().getContentAsString());
                }
            })
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombreUsuario").value("usuarioNuevo"))
            .andExpect(jsonPath("$.correo").value("nuevo@mail.com"));
    }

    @Test
    void borrarUsuarioPorId_ok() throws Exception {
        when(userService.borrarUsuario(1L)).thenReturn("Eliminado");

        mockMvc.perform(delete("/api/usuarios/1"))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }
}
