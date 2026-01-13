package es.um.pds.spkr.estrategia;

import java.util.ArrayList;
import java.util.List;

import es.um.pds.spkr.modelo.Pregunta;

public class EstrategiaRepeticionEspaciada implements EstrategiaAprendizaje {
    
    private List<Pregunta> preguntasFalladas;
    private int contadorParaRepetir;
    private static final int INTERVALO_REPETICION = 3;
    
    public EstrategiaRepeticionEspaciada() {
        this.preguntasFalladas = new ArrayList<>();
        this.contadorParaRepetir = 0;
    }
    
    @Override
    public Pregunta siguientePregunta(List<Pregunta> preguntas, int preguntaActual) {
        // Si hay preguntas falladas y toca repetir
        if (!preguntasFalladas.isEmpty() && contadorParaRepetir >= INTERVALO_REPETICION) {
            contadorParaRepetir = 0;
            return preguntasFalladas.remove(0);
        }
        
        // Si no, seguir con la siguiente pregunta normal
        if (preguntaActual < preguntas.size()) {
            contadorParaRepetir++;
            return preguntas.get(preguntaActual);
        }
        
        // Si ya no hay preguntas normales, mostrar las falladas restantes
        if (!preguntasFalladas.isEmpty()) {
            return preguntasFalladas.remove(0);
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
    
    @Override
    public String getNombre() {
        return "Repetición Espaciada";
    }
}