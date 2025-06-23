package com.microservicio.foros.microservicio_foros.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CategoriaClient {

  private final WebClient webClient;

  public CategoriaClient(@Value("${categorias-service.url}") String categoriasServiceUrl) {
    this.webClient = WebClient.builder().baseUrl(categoriasServiceUrl).build();
  }

  public Map<String, Object> obtenerCategoriaPorId(Long id) {
        return this.webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), response -> response.bodyToMono(String.class).map(body -> new RuntimeException("Categoria no encontrado")))
            .bodyToMono(Map.class).block();
            }
}

