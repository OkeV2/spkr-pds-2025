package es.um.pds.spkr.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeccionTest {

    private Leccion leccion;

    @BeforeEach
    public void setUp() {
        leccion = new Leccion("Lección 1", "Vocabulario básico", 1);
    }

    @Test
    public void testLeccionCreacion() {
        assertEquals("Lección 1", leccion.getNombre());
        assertEquals("Vocabulario básico", leccion.getDescripcion());
        assertEquals(1, leccion.getOrden());
    }

    @Test
    public void testLeccionPreguntasInicial() {
        assertNotNull(leccion.getPreguntas());
        assertTrue(leccion.getPreguntas().isEmpty());
    }

    @Test
    public void testAddPregunta() {
        PreguntaTest pregunta = new PreguntaTest("¿Cómo se dice perro?", 1, "dog", "cat", "bird");
        leccion.addPregunta(pregunta);

        assertEquals(1, leccion.getPreguntas().size());
        assertEquals(pregunta, leccion.getPreguntas().get(0));
    }

    @Test
    public void testAddMultiplesPreguntas() {
        PreguntaTest pregunta1 = new PreguntaTest("Pregunta 1", 1, "a", "b", "c");
        PreguntaTraduccion pregunta2 = new PreguntaTraduccion("Traduce: Hello", 2, "Hola");
        PreguntaHuecos pregunta3 = new PreguntaHuecos("I ___ a student", 3, "I am a student", "am");

        leccion.addPregunta(pregunta1);
        leccion.addPregunta(pregunta2);
        leccion.addPregunta(pregunta3);

        assertEquals(3, leccion.getPreguntas().size());
    }

    @Test
    public void testRemovePregunta() {
        PreguntaTest pregunta = new PreguntaTest("¿Cómo se dice perro?", 1, "dog", "cat", "bird");
        leccion.addPregunta(pregunta);
        leccion.removePregunta(pregunta);

        assertTrue(leccion.getPreguntas().isEmpty());
    }

    @Test
    public void testGetPreguntasDevuelveListaInmodificable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            leccion.getPreguntas().add(new PreguntaTest("Nueva", 1, "a", "b", "c"));
        });
    }

    @Test
    public void testSetNombre() {
        leccion.setNombre("Nuevo nombre");
        assertEquals("Nuevo nombre", leccion.getNombre());
    }

    @Test
    public void testSetDescripcion() {
        leccion.setDescripcion("Nueva descripción");
        assertEquals("Nueva descripción", leccion.getDescripcion());
    }

    @Test
    public void testSetOrden() {
        leccion.setOrden(5);
        assertEquals(5, leccion.getOrden());
    }

    @Test
    public void testLeccionVaciaConstructor() {
        Leccion leccionVacia = new Leccion();

        assertNull(leccionVacia.getNombre());
        assertNotNull(leccionVacia.getPreguntas());
        assertTrue(leccionVacia.getPreguntas().isEmpty());
    }
}
