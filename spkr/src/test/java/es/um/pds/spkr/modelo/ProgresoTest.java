package es.um.pds.spkr.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProgresoTest {

    private Curso curso;
    private Progreso progreso;

    @BeforeEach
    public void setUp() {
        curso = new Curso("Inglés Básico", "Curso de prueba", "Inglés");
        progreso = new Progreso(curso);
    }

    @Test
    public void testProgresoInicial() {
        assertEquals(0, progreso.getPreguntaActual());
        assertEquals(0, progreso.getAciertos());
        assertEquals(0, progreso.getErrores());
        assertEquals(0, progreso.getTiempoSegundos());
        assertFalse(progreso.isCompletado());
        assertNotNull(progreso.getFechaUltimoAcceso());
    }

    @Test
    public void testProgresoConCurso() {
        assertEquals(curso, progreso.getCurso());
    }

    @Test
    public void testRegistrarAcierto() {
        progreso.registrarAcierto();

        assertEquals(1, progreso.getAciertos());
        assertEquals(1, progreso.getPreguntaActual());
        assertEquals(0, progreso.getErrores());
    }

    @Test
    public void testRegistrarMultiplesAciertos() {
        progreso.registrarAcierto();
        progreso.registrarAcierto();
        progreso.registrarAcierto();

        assertEquals(3, progreso.getAciertos());
        assertEquals(3, progreso.getPreguntaActual());
    }

    @Test
    public void testRegistrarError() {
        progreso.registrarError();

        assertEquals(1, progreso.getErrores());
        assertEquals(1, progreso.getPreguntaActual());
        assertEquals(0, progreso.getAciertos());
    }

    @Test
    public void testRegistrarMultiplesErrores() {
        progreso.registrarError();
        progreso.registrarError();

        assertEquals(2, progreso.getErrores());
        assertEquals(2, progreso.getPreguntaActual());
    }

    @Test
    public void testRegistrarAciertosYErrores() {
        progreso.registrarAcierto();
        progreso.registrarError();
        progreso.registrarAcierto();

        assertEquals(2, progreso.getAciertos());
        assertEquals(1, progreso.getErrores());
        assertEquals(3, progreso.getPreguntaActual());
    }

    @Test
    public void testReiniciar() {
        progreso.registrarAcierto();
        progreso.registrarError();
        progreso.setTiempoSegundos(300);
        progreso.setEstrategia("Secuencial");
        progreso.setCompletado(true);

        progreso.reiniciar();

        assertEquals(0, progreso.getPreguntaActual());
        assertEquals(0, progreso.getAciertos());
        assertEquals(0, progreso.getErrores());
        assertEquals(0, progreso.getTiempoSegundos());
        assertNull(progreso.getEstrategia());
        assertFalse(progreso.isCompletado());
    }

    @Test
    public void testSetTiempoSegundos() {
        progreso.setTiempoSegundos(120);
        assertEquals(120, progreso.getTiempoSegundos());
    }

    @Test
    public void testSetEstrategia() {
        progreso.setEstrategia("Aleatoria");
        assertEquals("Aleatoria", progreso.getEstrategia());
    }

    @Test
    public void testSetCompletado() {
        progreso.setCompletado(true);
        assertTrue(progreso.isCompletado());
    }

    @Test
    public void testFechaUltimoAccesoDefensiveCopy() {
        java.util.Date fecha = progreso.getFechaUltimoAcceso();
        long tiempoOriginal = fecha.getTime();

        fecha.setTime(0);

        assertEquals(tiempoOriginal, progreso.getFechaUltimoAcceso().getTime());
    }

    @Test
    public void testProgresoSinCurso() {
        Progreso progresoVacio = new Progreso();

        assertNull(progresoVacio.getCurso());
        assertEquals(0, progresoVacio.getPreguntaActual());
    }
}
