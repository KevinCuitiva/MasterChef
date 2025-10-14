package edu.dosw.lab.Laboratorio_Master_Chef.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dosw.lab.Laboratorio_Master_Chef.model.Receta;
import edu.dosw.lab.Laboratorio_Master_Chef.model.TipoAutor;
import edu.dosw.lab.Laboratorio_Master_Chef.repository.RecetaRepository;

@RestController
@RequestMapping("/api/recetas")
public class ControllerReceta {

    private final RecetaRepository repo;

    public ControllerReceta(RecetaRepository repo) {
        this.repo = repo;
    }

    // Registrar una receta
    @PostMapping
    public ResponseEntity<Receta> crearReceta(@RequestBody Receta receta) {
        // Establecer fecha de registro
        receta.setFechaRegistro(LocalDate.now());
        Receta saved = repo.save(receta);
        return ResponseEntity.ok(saved);
    }

    // Devolver todas las recetas
    @GetMapping
    public List<Receta> todas() {
        return repo.findAll();
    }

    // Devolver una receta por su id
    @GetMapping("/{id}")
    public ResponseEntity<Receta> porId(@PathVariable String id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Devolver recetas por tipo de autor
    @GetMapping("/autor/{tipo}")
    public List<Receta> porTipoAutor(@PathVariable String tipo) {
        String normalized = tipo.toUpperCase();
        try {
            TipoAutor t = TipoAutor.valueOf(normalized);
            return repo.findByTipoAutor(t);
        } catch (IllegalArgumentException e) {
            // Try partial matches (chef, part..., tele...)
            if (normalized.startsWith("CHEF")) {
                return repo.findByTipoAutor(TipoAutor.CHEF);
            }
            if (normalized.startsWith("PART")) {
                return repo.findByTipoAutor(TipoAutor.PARTICIPANTE);
            }
            if (normalized.startsWith("TELE")) {
                return repo.findByTipoAutor(TipoAutor.TELEVIDENTE);
            }
            return List.of();
        }
    }

    // Devolver recetas por temporada
    @GetMapping("/temporada/{temporada}")
    public List<Receta> porTemporada(@PathVariable int temporada) { 
        return repo.findByTemporada(temporada);
    }

    // Buscar recetas por ingrediente
    @GetMapping("/buscar/ingrediente")
    public List<Receta> porIngrediente(@RequestParam String ingrediente) { 
        return repo.findByIngredientesContaining(ingrediente);
    }

    // Eliminar una receta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar una receta
    @PutMapping("/{id}")
    public ResponseEntity<Receta> actualizar(@PathVariable String id, @RequestBody Receta recetaActualizada) {
        Optional<Receta> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        
        Receta receta = opt.get();
        // Mantener el id y fecha de registro original
        recetaActualizada.setId(receta.getId());
        if (recetaActualizada.getFechaRegistro() == null) {
            recetaActualizada.setFechaRegistro(receta.getFechaRegistro());
        }
        
        repo.save(recetaActualizada);
        return ResponseEntity.ok(recetaActualizada);
    }

}