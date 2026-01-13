package es.um.pds.spkr.estrategia;

import java.util.List;

import es.um.pds.spkr.modelo.Pregunta;

public interface EstrategiaAprendizaje {
    
    Pregunta siguientePregunta(List<Pregunta> preguntas, int preguntaActual);
    
    String getNombre();
}