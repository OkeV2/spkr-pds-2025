package es.um.pds.spkr.modelo;

public abstract class Pregunta {
    
    private Long id;
    private String enunciado;
    private int orden;
    
    public Pregunta() {
    }
    
    public Pregunta(String enunciado, int orden) {
        this.enunciado = enunciado;
        this.orden = orden;
    }
    
    public abstract boolean validarRespuesta(String respuesta);
    
    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEnunciado() {
        return enunciado;
    }
    
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }
    
    public int getOrden() {
        return orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
}