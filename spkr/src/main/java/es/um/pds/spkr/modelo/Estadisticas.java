package es.um.pds.spkr.modelo;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "estadisticas")
public class Estadisticas {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int tiempoTotalUso;
    private int rachaActual;
    private int mejorRacha;
    private int ejerciciosCompletados;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimoAcceso;
    
    public Estadisticas() {
        this.tiempoTotalUso = 0;
        this.rachaActual = 0;
        this.mejorRacha = 0;
        this.ejerciciosCompletados = 0;
        this.ultimoAcceso = new Date();
    }
    
    public void actualizarRacha() {
        Date hoy = new Date();
        
        if (ultimoAcceso == null) {
            rachaActual = 1;
            ultimoAcceso = hoy;
            if (rachaActual > mejorRacha) {
                mejorRacha = rachaActual;
            }
            return;
        }
        
        // Calcular diferencia en días
        long diffMs = hoy.getTime() - ultimoAcceso.getTime();
        long diffDias = diffMs / (1000 * 60 * 60 * 24);
        
        if (diffDias == 0) {
            // Mismo día, no hacer nada
        } else if (diffDias == 1) {
            // Día consecutivo
            rachaActual++;
            if (rachaActual > mejorRacha) {
                mejorRacha = rachaActual;
            }
        } else {
            // Se rompió la racha
            rachaActual = 1;
        }
        
        ultimoAcceso = hoy;
    }
    
    public void incrementarTiempo(int minutos) {
        this.tiempoTotalUso += minutos;
    }
    
    public void incrementarEjercicios() {
        this.ejerciciosCompletados++;
    }
    
    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getTiempoTotalUso() {
        return tiempoTotalUso;
    }
    
    public int getRachaActual() {
        return rachaActual;
    }

    public int getMejorRacha() {
        return mejorRacha;
    }

    public int getEjerciciosCompletados() {
        return ejerciciosCompletados;
    }

    public Date getUltimoAcceso() {
        return ultimoAcceso != null ? new Date(ultimoAcceso.getTime()) : null;
    }
}