package edu.dosw.lab.Laboratorio_Master_Chef.repository;

import edu.dosw.lab.Laboratorio_Master_Chef.model.Receta;
import edu.dosw.lab.Laboratorio_Master_Chef.model.TipoAutor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends MongoRepository<Receta, String> {
    List<Receta> findByTipoAutor(TipoAutor tipoAutor);
    List<Receta> findByTemporada(Integer temporada);
    List<Receta> findByIngredientesContaining(String ingrediente);
}