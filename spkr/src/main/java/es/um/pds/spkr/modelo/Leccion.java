package es.um.pds.spkr.modelo;

import java.util.ArrayList;
import java.util.List;

public class Leccion {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private int orden;
    private List<Pregunta> preguntas;
    
    public Leccion() {
        this.preguntas = new ArrayList<>();
    }
    
    public Leccion(String nombre, String descripcion, int orden) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
        this.preguntas = new ArrayList<>();
    }
    
    public void addPregunta(Pregunta pregunta) {
        this.preguntas.add(pregunta);
    }
    
    public void removePregunta(Pregunta pregunta) {
        this.preguntas.remove(pregunta);
    }
    
    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getOrden() {
        return orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    public List<Pregunta> getPreguntas() {
        return preguntas;
    }
    
    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}