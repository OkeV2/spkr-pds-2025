package es.um.pds.spkr.modelo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PreguntaTest.class, name = "test"),
    @JsonSubTypes.Type(value = PreguntaTraduccion.class, name = "traduccion"),
    @JsonSubTypes.Type(value = PreguntaHuecos.class, name = "huecos")
})
public abstract class Pregunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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