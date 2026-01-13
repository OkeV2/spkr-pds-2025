package es.um.pds.spkr.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PreguntasTest {
    
    // Tests para PreguntaTest (tipo test)
    
    @Test
    public void testPreguntaTestRespuestaCorrecta() {
        PreguntaTest pregunta = new PreguntaTest(
            "¿Cómo se dice 'coche' en inglés?", 
            1, 
            "car", 
            "house", 
            "dog"
        );
        
        assertTrue(pregunta.validarRespuesta("car"));
    }
    
    @Test
    public void testPreguntaTestRespuestaCorrectaIgnoraCase() {
        PreguntaTest pregunta = new PreguntaTest(
            "¿Cómo se dice 'coche' en inglés?", 
            1, 
            "car", 
            "house", 
            "dog"
        );
        
        assertTrue(pregunta.validarRespuesta("CAR"));
        assertTrue(pregunta.validarRespuesta("Car"));
    }
    
    @Test
    public void testPreguntaTestRespuestaIncorrecta() {
        PreguntaTest pregunta = new PreguntaTest(
            "¿Cómo se dice 'coche' en inglés?", 
            1, 
            "car", 
            "house", 
            "dog"
        );
        
        assertFalse(pregunta.validarRespuesta("house"));
        assertFalse(pregunta.validarRespuesta("dog"));
    }
    
    // Tests para PreguntaTraduccion
    
    @Test
    public void testPreguntaTraduccionRespuestaCorrecta() {
        PreguntaTraduccion pregunta = new PreguntaTraduccion(
            "Traduce: 'Hello'", 
            1, 
            "Hola"
        );
        
        assertTrue(pregunta.validarRespuesta("Hola"));
    }
    
    @Test
    public void testPreguntaTraduccionIgnoraCase() {
        PreguntaTraduccion pregunta = new PreguntaTraduccion(
            "Traduce: 'Hello'", 
            1, 
            "Hola"
        );
        
        assertTrue(pregunta.validarRespuesta("hola"));
        assertTrue(pregunta.validarRespuesta("HOLA"));
    }
    
    @Test
    public void testPreguntaTraduccionRespuestaIncorrecta() {
        PreguntaTraduccion pregunta = new PreguntaTraduccion(
            "Traduce: 'Hello'", 
            1, 
            "Hola"
        );
        
        assertFalse(pregunta.validarRespuesta("Adios"));
    }
    
    // Tests para PreguntaHuecos
    
    @Test
    public void testPreguntaHuecosRespuestaCorrecta() {
        PreguntaHuecos pregunta = new PreguntaHuecos(
            "Completa: I ___ a student", 
            1, 
            "I am a student", 
            "am"
        );
        
        assertTrue(pregunta.validarRespuesta("am"));
    }
    
    @Test
    public void testPreguntaHuecosIgnoraCase() {
        PreguntaHuecos pregunta = new PreguntaHuecos(
            "Completa: I ___ a student", 
            1, 
            "I am a student", 
            "am"
        );
        
        assertTrue(pregunta.validarRespuesta("AM"));
        assertTrue(pregunta.validarRespuesta("Am"));
    }
    
    @Test
    public void testPreguntaHuecosRespuestaIncorrecta() {
        PreguntaHuecos pregunta = new PreguntaHuecos(
            "Completa: I ___ a student", 
            1, 
            "I am a student", 
            "am"
        );
        
        assertFalse(pregunta.validarRespuesta("is"));
    }
}