package com.microservicio.foros.microservicio_foros.service;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservicio.foros.microservicio_foros.Model.Foros;
import com.microservicio.foros.microservicio_foros.Repository.ForosRepository;
import com.microservicio.foros.microservicio_foros.Services.ForosService;
import com.microservicio.foros.microservicio_foros.client.CategoriaClient;

@ExtendWith(MockitoExtension.class)
public class ForosServiceTest {
    @Mock
     ForosRepository repository;

    @InjectMocks
    ForosService service;

    @Mock
    private CategoriaClient categoriaClient; 

    @Test
    void buscarfororpodid(){
        Foros nuevoforos = new Foros(1L, null, null, null, null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(nuevoforos));
        Foros result = service.buscarForos(1L);
        assertThat(result).isEqualTo(nuevoforos);

    }

    @Test
    void guardarfortest(){
            Foros nuevoforo = new Foros(1L, 1L, null, null, null, null);
            when(repository.save(nuevoforo)).thenReturn(nuevoforo);
            Foros result = service.guardarForo(nuevoforo);
            assertThat(result).isEqualTo(nuevoforo);

    }





}
