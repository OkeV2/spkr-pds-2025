package es.um.pds.spkr.estrategia;

import java.util.List;
import java.util.Random;

import es.um.pds.spkr.modelo.Pregunta;

public class EstrategiaAleatoria implements EstrategiaAprendizaje {
    
    private Random random;
    
    public EstrategiaAleatoria() {
        this.random = new Random();
    }
    
    @Override
    public Pregunta siguientePregunta(List<Pregunta> preguntas, int preguntaActual) {
        if (preguntaActual < preguntas.size()) {
            int indiceAleatorio = random.nextInt(preguntas.size());
            return preguntas.get(indiceAleatorio);
        }
        return null;
    }
    
    @Override
    public String getNombre() {
        return "Aleatoria";
    }
}