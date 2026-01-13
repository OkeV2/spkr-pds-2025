package es.um.pds.spkr.modelo;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "errores_frecuentes")
public class ErrorFrecuente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Pregunta pregunta;
    
    private int vecesFallada;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimoError;
    
    public ErrorFrecuente() {
        this.vecesFallada = 0;
        this.fechaUltimoError = new Date();
    }
    
    public ErrorFrecuente(Pregunta pregunta) {
        this.pregunta = pregunta;
        this.vecesFallada = 1;
        this.fechaUltimoError = new Date();
    }
    
    public void incrementarErrores() {
        this.vecesFallada++;
        this.fechaUltimoError = new Date();
    }
    
    public void reducirErrores() {
        if (this.vecesFallada > 0) {
            this.vecesFallada--;
        }
    }
    
    public boolean estaDominada() {
        return this.vecesFallada == 0;
    }
    
    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Pregunta getPregunta() {
        return pregunta;
    }
    
    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
    
    public int getVecesFallada() {
        return vecesFallada;
    }
    
    public void setVecesFallada(int vecesFallada) {
        this.vecesFallada = vecesFallada;
    }
    
    public Date getFechaUltimoError() {
        return fechaUltimoError;
    }
    
    public void setFechaUltimoError(Date fechaUltimoError) {
        this.fechaUltimoError = fechaUltimoError;
    }
}