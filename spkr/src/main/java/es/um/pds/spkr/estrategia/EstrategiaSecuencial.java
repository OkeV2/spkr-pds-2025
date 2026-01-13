package es.um.pds.spkr.estrategia;

import java.util.List;

import es.um.pds.spkr.modelo.Pregunta;

public class EstrategiaSecuencial implements EstrategiaAprendizaje {
    
    @Override
    public Pregunta siguientePregunta(List<Pregunta> preguntas, int preguntaActual) {
        if (preguntaActual < preguntas.size()) {
            return preguntas.get(preguntaActual);
        }
        return null;
    }
    
    @Override
    public String getNombre() {
        return "Secuencial";
    }
}