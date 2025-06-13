package com.microservicio.foros.microservicio_foros.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservicio.foros.microservicio_foros.Model.Foros;

@Repository
public interface ForosRepository extends JpaRepository<Foros, Long>{

  @Query("SELECT f FROM Foros f WHERE f.idCategoria = :idCategoria")
  List<Foros> encontrarForosPorIdCategoria(Long idCategoria);

  List<Foros> findByIdUsuario(Long idUsuario);
  List<Foros> findByIdCategoria(Long idCategoria);


}
