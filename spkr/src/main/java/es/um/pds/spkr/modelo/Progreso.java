package es.um.pds.spkr.modelo;

import java.util.Date;

public class Progreso {
    
    private Long id;
    private Curso curso;
    private int preguntaActual;
    private int aciertos;
    private int errores;
    private Date fechaUltimoAcceso;
    
    public Progreso() {
        this.preguntaActual = 0;
        this.aciertos = 0;
        this.errores = 0;
        this.fechaUltimoAcceso = new Date();
    }
    
    public Progreso(Curso curso) {
        this.curso = curso;
        this.preguntaActual = 0;
        this.aciertos = 0;
        this.errores = 0;
        this.fechaUltimoAcceso = new Date();
    }
    
    public void registrarAcierto() {
        this.aciertos++;
        this.preguntaActual++;
        this.fechaUltimoAcceso = new Date();
    }
    
    public void registrarError() {
        this.errores++;
        this.preguntaActual++;
        this.fechaUltimoAcceso = new Date();
    }
    
    public void reiniciar() {
        this.preguntaActual = 0;
        this.aciertos = 0;
        this.errores = 0;
        this.fechaUltimoAcceso = new Date();
    }
    
    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Curso getCurso() {
        return curso;
    }
    
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public int getPreguntaActual() {
        return preguntaActual;
    }
    
    public void setPreguntaActual(int preguntaActual) {
        this.preguntaActual = preguntaActual;
    }
    
    public int getAciertos() {
        return aciertos;
    }
    
    public void setAciertos(int aciertos) {
        this.aciertos = aciertos;
    }
    
    public int getErrores() {
        return errores;
    }
    
    public void setErrores(int errores) {
        this.errores = errores;
    }
    
    public Date getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }
    
    public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }
}