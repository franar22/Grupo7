package com.example.USER.service;

import com.example.USER.client.RolClient;
import com.example.USER.model.Usuarios;
import com.example.USER.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServuceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RolClient rolClient;

    @InjectMocks
    private UserService userService;

    @Test
    void listarUsuarios_deberiaRetornarLista() {
        List<Usuarios> mockUsuarios = List.of(new Usuarios(1L, null, null, null, null, null, null, null));
        when(usuarioRepository.findAll()).thenReturn(mockUsuarios);

        List<Usuarios> result = userService.listarUsuarios();

        assertThat(result).isEqualTo(mockUsuarios);
    }

    @Test
    void obtenerUsuarioPorId_existente() {
        Usuarios usuario = new Usuarios(1L, null, null, null, null, null, null, null);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuarios result = userService.obtenerUsuarioPorId(1L);

        assertThat(result).isEqualTo(usuario);
    }

    @Test
    void obtenerUsuarioPorCorreo_existente() {
        Usuarios usuario = new Usuarios(1L, null, null, null, null, null, null, null);
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(Optional.of(usuario));

        Usuarios result = userService.obtenerUsuarioPorCorreo("correo@test.com");

        assertThat(result).isEqualTo(usuario);
    }

    @Test
    void borrarUsuario_deberiaEliminarUsuario() {
        Usuarios usuario = new Usuarios(1L, null, null, null, null, null, null, null);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(1L);

        String result = userService.borrarUsuario(1L);

        assertThat(result).isEqualTo("Se ha eliminado el usuario correctamente.");
    }

    @Test
    void guardarUsuario_valido() {
    Usuarios nuevoUsuario = new Usuarios(    1L,2L, "usuario", "correo@mail.com", "clave",null,null,null);
    when(rolClient.obtenerRolPorId(2L)).thenReturn(Map.of("id", 2));
    when(passwordEncoder.encode("clave")).thenReturn("clave_encriptada");
    Usuarios usuarioGuardado = new Usuarios( 1L, 2L,"usuario", "correo@mail.com", "clave_encriptada", null, null, null);

    when(usuarioRepository.save(any(Usuarios.class))).thenReturn(usuarioGuardado);

    Usuarios result = userService.guardarUsuario(nuevoUsuario);

    assertThat(result).isEqualTo(usuarioGuardado);
}


    @Test
    void actualizarUsuario_modificaCampos() {
    
    Usuarios usuarioExistente = new Usuarios(1L,2L,"viejoUsuario","viejo@mail.com","vieja_pass",null,"Viejo","Apellido"
    );

    
    Usuarios actualizacion = new Usuarios(1L,3L, "nuevo", "nuevo@mail.com", "nueva_pass", null,null,null
    );

    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
    when(rolClient.obtenerRolPorId(3L)).thenReturn(Map.of("id", 3));
    when(passwordEncoder.encode("nueva_pass")).thenReturn("nueva_pass_encriptada");
    when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Usuarios result = userService.actualizarUsuario(1L, actualizacion);

    assertThat(result.getNombreUsuario()).isEqualTo("nuevo");
    assertThat(result.getPassword()).isEqualTo("nueva_pass_encriptada");
    assertThat(result.getCorreo()).isEqualTo("nuevo@mail.com");
    assertThat(result.getIdRol()).isEqualTo(3L);
}

}

