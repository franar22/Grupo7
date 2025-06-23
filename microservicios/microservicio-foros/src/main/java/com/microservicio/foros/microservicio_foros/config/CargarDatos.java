package com.microservicio.foros.microservicio_foros.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microservicio.foros.microservicio_foros.Model.Foros;
import com.microservicio.foros.microservicio_foros.Repository.ForosRepository;


@Configuration
public class CargarDatos {

    @Bean
    CommandLineRunner initDataBase(ForosRepository forosRepository) {
        return args -> {
            // Solo se ejecuta si no hay foros en la base de datos
            if (forosRepository.count() == 0) {
                // Insertamos los foros con los datos proporcionados
                forosRepository.save(new Foros(null, 101L, 1L, "React Hooks", "Todo sobre los Hooks de React", null));
                forosRepository.save(new Foros(null, 102L, 2L, "GraphQL", "Alternativa a REST APIs", null));
                forosRepository.save(new Foros(null, 103L, 3L, "Flutter", "Desarrollo multiplataforma", null));
                forosRepository.save(new Foros(null, 104L, 4L, "JUnit 5", "Testing en Java", null));
                forosRepository.save(new Foros(null, 105L, 5L, "Figma Tips", "Diseño de interfaces colaborativo", null));


                System.out.println("Datos de foros cargados correctamente.");
            }
        };
    }
}