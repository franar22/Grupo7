package com.microservicio.rol.microservicio_rol.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservicio.rol.microservicio_rol.clients.UsuarioClient;
import com.microservicio.rol.microservicio_rol.model.Rol;
import com.microservicio.rol.microservicio_rol.model.Rol.TipoRol;
import com.microservicio.rol.microservicio_rol.repository.RolRepository;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    @Mock
    RolRepository repository;

    @InjectMocks
    RolService service;

    @Mock
    private UsuarioClient usuarioClient;

    @Test
    void returnsAllRoles() {
        List<Rol> mocklist = Arrays.asList(new Rol(1L, TipoRol.ADMIN));
        when(repository.findAll()).thenReturn(mocklist);

        List<Rol> result = service.listarRoles();

        assertThat(result).isEqualTo(mocklist);
    }

    @Test
    void returnsRolFromRepository() {
        Rol nuevorol = new Rol(1L, TipoRol.MODERADOR);
        when(repository.findById(1L)).thenReturn(Optional.of(nuevorol));

        Rol result = service.obtenerRolPorId(1L);

        assertThat(result).isEqualTo(nuevorol);
    }

    @Test
    void borrarRolPorIdreturnsmensaje() {
    doNothing().when(repository).deleteById(1L);

    String resultado = service.borrarRol(1L);

    assertThat(resultado).isEqualTo("Rol eliminado correctamente");
}

    @Test
    void crearRolNuevo() {
        Rol nuevoRol = new Rol(null, TipoRol.SOPORTE);
        Rol rolGuardado = new Rol(1L, TipoRol.SOPORTE);

        when(repository.save(nuevoRol)).thenReturn(rolGuardado);

        Rol resultado = service.guardarRol(nuevoRol);

        assertThat(resultado).isEqualTo(rolGuardado);
    }

    @Test
    void modificarRol() {
        Rol rolExistente = new Rol(1L, TipoRol.USUARIO);
        Rol nuevaInfo = new Rol(null, TipoRol.ADMIN);
        Rol actualizado = new Rol(1L, TipoRol.ADMIN);

        when(repository.findById(1L)).thenReturn(Optional.of(rolExistente));
        when(repository.save(any(Rol.class))).thenReturn(actualizado);

        Rol resultado = service.actualizarRol(1L, nuevaInfo);

        assertThat(resultado.getTipoRol()).isEqualTo(TipoRol.ADMIN);
    }

    @Test
    void asignarRolAUsuario() {
        Rol rolEsperado = new Rol(null, TipoRol.GESTOR_ANUNCIOS);
        when(repository.save(any(Rol.class))).thenReturn(rolEsperado);

        Rol resultado = service.asignarRolAUsuario(5L, TipoRol.GESTOR_ANUNCIOS);

        assertThat(resultado.getTipoRol()).isEqualTo(TipoRol.GESTOR_ANUNCIOS);
    }
}

