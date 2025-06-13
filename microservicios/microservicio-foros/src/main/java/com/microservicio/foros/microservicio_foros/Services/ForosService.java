package com.microservicio.foros.microservicio_foros.Services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservicio.foros.microservicio_foros.Model.Foros;
import com.microservicio.foros.microservicio_foros.Repository.ForosRepository;
import com.microservicio.foros.microservicio_foros.client.CategoriaClient;

@Service
public class ForosService {

  @Autowired
  private ForosRepository forosRepository;

  @Autowired
  private CategoriaClient categoriaClient;

  public List<Foros> listarForos() {
    return forosRepository.findAll();
  }

  public Foros buscarForos(Long id) {
    return forosRepository.findById(id).orElseThrow(() -> new RuntimeException("Foro no encontrado en la base de datos."));
  }

  public Foros guardarForo(Foros foro) {
    
    Map<String, Object> categorias = categoriaClient.obtenerCategoriaPorId(foro.getIdCategoria());

    if (categorias == null || categorias.isEmpty()) {
      throw new RuntimeException("El id de la categoria no se ha encontrado. No se puede realizar el foro.");
    }

    return forosRepository.save(foro);
    
  }

  public String borrarForo(Long id) { 
    Foros foroAct = buscarForos(id);
    forosRepository.deleteById(foroAct.getId());
    return "Foro Eliminado correctamente";
  }

  public Foros actualizarForo(Long id, Foros foroActualizado) {
    Foros foroActual = buscarForos(id);

    if (foroActualizado.getIdCategoria() != null) {
        Map<String, Object> categorias = categoriaClient.obtenerCategoriaPorId(foroActualizado.getIdCategoria());
        
        if (categorias == null || categorias.isEmpty()) {
            throw new RuntimeException("El id de la categoria no se ha encontrado. No se puede actualizar el foro.");
        }
    }

    if (foroActualizado.getTitulo() != null) {
        foroActual.setTitulo(foroActualizado.getTitulo());
    }

    if (foroActualizado.getContenido() != null) {
        foroActual.setContenido(foroActualizado.getContenido());
    }

    if (foroActualizado.getIdCategoria() != null) {
        foroActual.setIdCategoria(foroActualizado.getIdCategoria());
    }

    return forosRepository.save(foroActual);
}

public List<Foros> buscarForosPorUsuario(Long idUsuario) {
    return forosRepository.findByIdUsuario(idUsuario);
}

public List<Foros> buscarForosPorCategoria(Long idCategoria) {
    return forosRepository.findByIdCategoria(idCategoria);
}

}

