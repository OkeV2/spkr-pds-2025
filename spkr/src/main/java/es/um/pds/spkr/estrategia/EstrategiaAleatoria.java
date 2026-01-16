package es.um.pds.spkr.estrategia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import es.um.pds.spkr.modelo.Pregunta;

public class EstrategiaAleatoria implements EstrategiaAprendizaje {
    
    private List<Integer> ordenAleatorio;
    private boolean inicializada;
    
    public EstrategiaAleatoria() {
        this.ordenAleatorio = new ArrayList<>();
        this.inicializada = false;
    }
    
    @Override
    public Pregunta siguientePregunta(List<Pregunta> preguntas, int preguntaActual) {
        if (!inicializada) {
            // Crear lista de índices y barajarla
            for (int i = 0; i < preguntas.size(); i++) {
                ordenAleatorio.add(i);
            }
            Collections.shuffle(ordenAleatorio);
            inicializada = true;
        }
        
        if (preguntaActual < preguntas.size()) {
            int indiceReal = ordenAleatorio.get(preguntaActual);
            return preguntas.get(indiceReal);
        }
        return null;
    }
    
    @Override
    public String getNombre() {
        return "Aleatoria";
    }
}