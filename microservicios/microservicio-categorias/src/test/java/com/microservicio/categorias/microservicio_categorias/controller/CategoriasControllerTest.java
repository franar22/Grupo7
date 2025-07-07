package com.microservicio.categorias.microservicio_categorias.controller;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.categorias.microservicio_categorias.model.Categorias;
import com.microservicio.categorias.microservicio_categorias.services.CategoriasService;

@WebMvcTest(CategoriasController.class)
public class CategoriasControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CategoriasService categoriasService;

        @Test
    void getallcategorias() throws Exception {
        List<Categorias> listacategorias = Arrays.asList(
            Categorias.builder()
                .id(1L)
                .titulo("Categoría de prueba")
                .descripcion("Descripción de prueba")
                .build()
        );

        when(categoriasService.listarCategorias()).thenReturn(listacategorias);

        mockMvc.perform(get("/api/categorias"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
    }

    
    @Test
    void eliminarcateriastest() throws Exception{
        when(categoriasService.borrarCategoria(1L)).thenReturn("Categoria eleminida");
        mockMvc.perform(delete("/api/categorias/1"))
           .andExpect(status().isOk());
    

    }

    



}
