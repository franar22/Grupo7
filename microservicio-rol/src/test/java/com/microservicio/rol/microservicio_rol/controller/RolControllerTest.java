package com.microservicio.rol.microservicio_rol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.rol.microservicio_rol.clients.UsuarioClient;
import com.microservicio.rol.microservicio_rol.model.Rol;
import com.microservicio.rol.microservicio_rol.model.Usuarios;
import com.microservicio.rol.microservicio_rol.service.RolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

@WebMvcTest(RolController.class)
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioClient usuarioClient;

    @Test
    void getallrols() throws Exception {
        List<Rol> listaroles = Arrays.asList(new Rol(1L, null));

        when(rolService.listarRoles()).thenReturn(listaroles);

        mockMvc.perform(get("/api/roles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].idRol").value(1));

    }

    @Test
    void getrolesporid() throws Exception {
    Rol rol = new Rol(1L, null); 

    when(rolService.obtenerRolPorId(1L)).thenReturn(rol); 

    mockMvc.perform(get("/api/roles/1")) 
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.idRol").value(1)); 
}

   @Test
void crearols() throws Exception {
    Rol nuevoRol = new Rol(null, Rol.TipoRol.USUARIO);
    Rol rolGuardado = new Rol(1L, Rol.TipoRol.USUARIO);

    when(rolService.guardarRol(any(Rol.class))).thenReturn(rolGuardado);

    mockMvc.perform(post("/api/roles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nuevoRol)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.idRol").value(1))
        .andExpect(jsonPath("$.tipoRol").value("USUARIO"));
}


   @Test
   void actualizarrols() throws Exception {
    Rol nuevaInfo = new Rol(null, Rol.TipoRol.ADMIN);
    Rol actualizado = new Rol(1L, Rol.TipoRol.ADMIN);

    when(rolService.actualizarRol(eq(1L), any(Rol.class))).thenReturn(actualizado);

    mockMvc.perform(put("/api/roles/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nuevaInfo)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.idRol").value(1))
        .andExpect(jsonPath("$.tipoRol").value("ADMIN"));
}


 @Test
  void borrarrols() throws Exception {
    when(rolService.borrarRol(1L)).thenReturn("Rol eliminado correctamente");

    mockMvc.perform(delete("/api/roles/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("Rol eliminado correctamente"));
}

  @Test
void asignarRolAUsuario() throws Exception {
    Long idUsuario = 1L;
    String tipoRol = "ADMIN";
    Rol.TipoRol tipoRolEnum = Rol.TipoRol.ADMIN;

    
    Usuarios mockUsuario = new Usuarios(); 

    
    Rol rolAsignado = new Rol(1L, tipoRolEnum);

   
    when(usuarioClient.obtenerUsuarioPorId(idUsuario)).thenReturn(mockUsuario);
    when(rolService.asignarRolAUsuario(idUsuario, tipoRolEnum)).thenReturn(rolAsignado);

    mockMvc.perform(post("/api/roles/asignar/{idUsuario}", idUsuario)
            .param("tipoRol", tipoRol)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.idRol").value(1))
        .andExpect(jsonPath("$.tipoRol").value("ADMIN"));
}





}



