package es.um.pds.spkr.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ErrorFrecuenteTest {
    
    @Test
    public void testCrearErrorFrecuente() {
        PreguntaTest pregunta = new PreguntaTest("¿Cómo se dice 'perro'?", 1, "dog", "cat", "bird");
        ErrorFrecuente error = new ErrorFrecuente(pregunta);
        
        assertEquals(1, error.getVecesFallada());
        assertEquals(pregunta, error.getPregunta());
    }
    
    @Test
    public void testIncrementarErrores() {
        PreguntaTest pregunta = new PreguntaTest("¿Cómo se dice 'perro'?", 1, "dog", "cat", "bird");
        ErrorFrecuente error = new ErrorFrecuente(pregunta);
        
        error.incrementarErrores();
        assertEquals(2, error.getVecesFallada());
        
        error.incrementarErrores();
        assertEquals(3, error.getVecesFallada());
    }
    
    @Test
    public void testReducirErrores() {
        PreguntaTest pregunta = new PreguntaTest("¿Cómo se dice 'perro'?", 1, "dog", "cat", "bird");
        ErrorFrecuente error = new ErrorFrecuente(pregunta);
        error.incrementarErrores();
        error.incrementarErrores();
        
        assertEquals(3, error.getVecesFallada());
        
        error.reducirErrores();
        assertEquals(2, error.getVecesFallada());
    }
    
    @Test
    public void testReducirErroresNoNegativo() {
        PreguntaTest pregunta = new PreguntaTest("¿Cómo se dice 'perro'?", 1, "dog", "cat", "bird");
        ErrorFrecuente error = new ErrorFrecuente(pregunta);
        
        error.reducirErrores();
        assertEquals(0, error.getVecesFallada());
        
        error.reducirErrores();
        assertEquals(0, error.getVecesFallada());
    }
    
    @Test
    public void testEstaDominada() {
        PreguntaTest pregunta = new PreguntaTest("¿Cómo se dice 'perro'?", 1, "dog", "cat", "bird");
        ErrorFrecuente error = new ErrorFrecuente(pregunta);
        
        assertFalse(error.estaDominada());
        
        error.reducirErrores();
        assertTrue(error.estaDominada());
    }
}