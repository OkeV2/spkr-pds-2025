package es.um.pds.spkr.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Curso {
    
    private Long id;
    private String titulo;
    private String descripcion;
    private String idioma;
    private Date fechaCreacion;
    private List<Leccion> lecciones;
    
    public Curso() {
        this.lecciones = new ArrayList<>();
        this.fechaCreacion = new Date();
    }
    
    public Curso(String titulo, String descripcion, String idioma) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idioma = idioma;
        this.fechaCreacion = new Date();
        this.lecciones = new ArrayList<>();
    }
    
    public void addLeccion(Leccion leccion) {
        this.lecciones.add(leccion);
    }
    
    public void removeLeccion(Leccion leccion) {
        this.lecciones.remove(leccion);
    }
    
    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getIdioma() {
        return idioma;
    }
    
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<Leccion> getLecciones() {
        return lecciones;
    }
    
    public void setLecciones(List<Leccion> lecciones) {
        this.lecciones = lecciones;
    }
}