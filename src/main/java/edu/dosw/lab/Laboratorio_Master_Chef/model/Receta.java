package edu.dosw.lab.Laboratorio_Master_Chef.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recetas")
public class Receta {

    @Id
    private String id;
    private String titulo;
    private List<String> ingredientes;
    private List<String> pasos;
    private String autorNombre;
    private TipoAutor tipoAutor;
    private int temporada;
    private LocalDate fechaRegistro;

    public Receta() {
        this.fechaRegistro = LocalDate.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public List<String> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<String> ingredientes) { this.ingredientes = ingredientes; }

    public List<String> getPasos() { return pasos; }
    public void setPasos(List<String> pasos) { this.pasos = pasos; }

    public String getAutorNombre() { return autorNombre; }
    public void setAutorNombre(String autorNombre) { this.autorNombre = autorNombre; }

    public TipoAutor getTipoAutor() { return tipoAutor; }
    public void setTipoAutor(TipoAutor tipoAutor) { this.tipoAutor = tipoAutor; }

    public Integer getTemporada() { return temporada; }
    public void setTemporada(Integer temporada) { this.temporada = temporada; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

}