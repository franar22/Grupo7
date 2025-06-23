package com.microservicio.post.microservicio_post.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microservicio.post.microservicio_post.model.Post;
import com.microservicio.post.microservicio_post.repository.PostRepository;

@Configuration
public class CargarDatos {

    @Bean
    CommandLineRunner initDataBase(PostRepository postRepository) {
        return args -> {
            // Solo se ejecuta si no hay publicaciones en la base de datos
            if (postRepository.count() == 0) {
                // Insertamos las publicaciones con los datos proporcionados
                postRepository.save(new Post(null, "Introducción a React", 
                        "En este post, exploramos los fundamentos de React, un framework popular para la construcción de interfaces de usuario.", 
                        1L, 1L, null));
                postRepository.save(new Post(null, "Introducción a GraphQL", 
                        "GraphQL es una alternativa moderna a las REST APIs. En este post, aprenderemos cómo implementarlo.",
                        2L, 1L, null));
                postRepository.save(new Post(null, "Desarrollo con Flutter", 
                        "Flutter permite crear aplicaciones multiplataforma con una única base de código. Aquí veremos los principios básicos.",
                        3L, 2L, null));
                postRepository.save(new Post(null, "Testing con JUnit 5", 
                        "JUnit 5 es una poderosa herramienta para realizar pruebas en Java. Este post cubre las mejores prácticas.",
                        4L, 2L, null));
                postRepository.save(new Post(null, "Figma Tips y Trucos", 
                        "Figma es una herramienta de diseño colaborativo. En este post, te enseñaremos algunos tips útiles.",
                        5L, 3L, null));

                System.out.println("Datos de publicaciones cargados correctamente.");
            }
        };
    }
}