package es.um.pds.spkr.estrategia;

import java.util.ArrayList;
import java.util.List;

import es.um.pds.spkr.modelo.Pregunta;

public class EstrategiaRepeticionEspaciada implements EstrategiaAprendizaje {
    
    private List<Pregunta> preguntasFalladas;
    private List<Pregunta> preguntasYaContadas;
    private int indiceFalladas;
    
    public EstrategiaRepeticionEspaciada() {
        this.preguntasFalladas = new ArrayList<>();
        this.preguntasYaContadas = new ArrayList<>();
        this.indiceFalladas = 0;
    }
    
    @Override
    public Pregunta siguientePregunta(List<Pregunta> preguntas, int indiceActual) {
        // Primero recorremos las preguntas normales
        if (indiceActual < preguntas.size()) {
            return preguntas.get(indiceActual);
        }
        
        // Luego repasamos las falladas
        if (indiceFalladas < preguntasFalladas.size()) {
            Pregunta pregunta = preguntasFalladas.get(indiceFalladas);
            indiceFalladas++;
            return pregunta;
        }
        
        return null;
    }
    
    public void registrarFallo(Pregunta pregunta) {
        if (!preguntasFalladas.contains(pregunta)) {
            preguntasFalladas.add(pregunta);
        }
    }
    
    public void registrarAcierto(Pregunta pregunta) {
        preguntasFalladas.remove(pregunta);
    }
    
    public boolean esPreguntaYaContada(Pregunta pregunta) {
        return preguntasYaContadas.contains(pregunta);
    }
    
    public void marcarComoContada(Pregunta pregunta) {
        if (!preguntasYaContadas.contains(pregunta)) {
            preguntasYaContadas.add(pregunta);
        }
    }
    
    @Override
    public String getNombre() {
        return "Repetición Espaciada";
    }
}