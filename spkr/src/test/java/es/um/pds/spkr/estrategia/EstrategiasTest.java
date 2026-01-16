package es.um.pds.spkr.estrategia;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.um.pds.spkr.modelo.Pregunta;
import es.um.pds.spkr.modelo.PreguntaTest;

public class EstrategiasTest {
    
    private List<Pregunta> preguntas;
    
    @BeforeEach
    public void setUp() {
        preguntas = new ArrayList<>();
        preguntas.add(new PreguntaTest("Pregunta 1", 1, "a", "b", "c"));
        preguntas.add(new PreguntaTest("Pregunta 2", 2, "a", "b", "c"));
        preguntas.add(new PreguntaTest("Pregunta 3", 3, "a", "b", "c"));
        preguntas.add(new PreguntaTest("Pregunta 4", 4, "a", "b", "c"));
    }
    
    // Tests EstrategiaSecuencial
    
    @Test
    public void testSecuencialOrden() {
        EstrategiaSecuencial estrategia = new EstrategiaSecuencial();
        
        assertEquals(preguntas.get(0), estrategia.siguientePregunta(preguntas, 0));
        assertEquals(preguntas.get(1), estrategia.siguientePregunta(preguntas, 1));
        assertEquals(preguntas.get(2), estrategia.siguientePregunta(preguntas, 2));
        assertEquals(preguntas.get(3), estrategia.siguientePregunta(preguntas, 3));
    }
    
    @Test
    public void testSecuencialFinPreguntas() {
        EstrategiaSecuencial estrategia = new EstrategiaSecuencial();
        
        assertNull(estrategia.siguientePregunta(preguntas, 4));
    }
    
    @Test
    public void testSecuencialNombre() {
        EstrategiaSecuencial estrategia = new EstrategiaSecuencial();
        assertEquals("Secuencial", estrategia.getNombre());
    }
    
    // Tests EstrategiaAleatoria
    
    @Test
    public void testAleatoriaDevuelvePregunta() {
        EstrategiaAleatoria estrategia = new EstrategiaAleatoria();
        
        Pregunta pregunta = estrategia.siguientePregunta(preguntas, 0);
        assertNotNull(pregunta);
        assertTrue(preguntas.contains(pregunta));
    }
    
    @Test
    public void testAleatoriaFinPreguntas() {
        EstrategiaAleatoria estrategia = new EstrategiaAleatoria();
        
        assertNull(estrategia.siguientePregunta(preguntas, 4));
    }
    
    @Test
    public void testAleatoriaNombre() {
        EstrategiaAleatoria estrategia = new EstrategiaAleatoria();
        assertEquals("Aleatoria", estrategia.getNombre());
    }
    
    // Tests EstrategiaRepeticionEspaciada
    
    @Test
    public void testRepeticionEspaciadaOrdenNormal() {
        EstrategiaRepeticionEspaciada estrategia = new EstrategiaRepeticionEspaciada();
        
        assertEquals(preguntas.get(0), estrategia.siguientePregunta(preguntas, 0));
        assertEquals(preguntas.get(1), estrategia.siguientePregunta(preguntas, 1));
    }
    
    @Test
    public void testRepeticionEspaciadaRepiteFalladas() {
        EstrategiaRepeticionEspaciada estrategia = new EstrategiaRepeticionEspaciada();
        
        // Recorrer todas las preguntas normales
        Pregunta pregunta1 = estrategia.siguientePregunta(preguntas, 0);
        estrategia.registrarFallo(pregunta1);
        
        estrategia.siguientePregunta(preguntas, 1);
        estrategia.siguientePregunta(preguntas, 2);
        estrategia.siguientePregunta(preguntas, 3);
        
        // Después de todas las preguntas (índice 4), debe repetir la fallada
        Pregunta repetida = estrategia.siguientePregunta(preguntas, 4);
        assertEquals(pregunta1, repetida);
    }
    
    @Test
    public void testRepeticionEspaciadaNombre() {
        EstrategiaRepeticionEspaciada estrategia = new EstrategiaRepeticionEspaciada();
        assertEquals("Repetición Espaciada", estrategia.getNombre());
    }
}