package es.um.pds.spkr.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CursoTest {

    private Curso curso;

    @BeforeEach
    public void setUp() {
        curso = new Curso("Inglés Básico", "Curso para principiantes", "Inglés");
    }

    @Test
    public void testCursoCreacion() {
        assertEquals("Inglés Básico", curso.getTitulo());
        assertEquals("Curso para principiantes", curso.getDescripcion());
        assertEquals("Inglés", curso.getIdioma());
    }

    @Test
    public void testCursoTieneFechaCreacion() {
        assertNotNull(curso.getFechaCreacion());
    }

    @Test
    public void testCursoLeccionesInicial() {
        assertNotNull(curso.getLecciones());
        assertTrue(curso.getLecciones().isEmpty());
    }

    @Test
    public void testAddLeccion() {
        Leccion leccion = new Leccion("Lección 1", "Primera lección", 1);
        curso.addLeccion(leccion);

        assertEquals(1, curso.getLecciones().size());
        assertEquals(leccion, curso.getLecciones().get(0));
    }

    @Test
    public void testAddMultiplesLecciones() {
        Leccion leccion1 = new Leccion("Lección 1", "Primera lección", 1);
        Leccion leccion2 = new Leccion("Lección 2", "Segunda lección", 2);

        curso.addLeccion(leccion1);
        curso.addLeccion(leccion2);

        assertEquals(2, curso.getLecciones().size());
    }

    @Test
    public void testRemoveLeccion() {
        Leccion leccion = new Leccion("Lección 1", "Primera lección", 1);
        curso.addLeccion(leccion);
        curso.removeLeccion(leccion);

        assertTrue(curso.getLecciones().isEmpty());
    }

    @Test
    public void testGetLeccionesDevuelveListaInmodificable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            curso.getLecciones().add(new Leccion("Nueva", "Nueva", 1));
        });
    }

    @Test
    public void testFechaCreacionDefensiveCopy() {
        java.util.Date fecha = curso.getFechaCreacion();
        long tiempoOriginal = fecha.getTime();

        fecha.setTime(0);

        assertEquals(tiempoOriginal, curso.getFechaCreacion().getTime());
    }

    @Test
    public void testSetTitulo() {
        curso.setTitulo("Nuevo Título");
        assertEquals("Nuevo Título", curso.getTitulo());
    }

    @Test
    public void testSetDescripcion() {
        curso.setDescripcion("Nueva descripción");
        assertEquals("Nueva descripción", curso.getDescripcion());
    }

    @Test
    public void testSetIdioma() {
        curso.setIdioma("Francés");
        assertEquals("Francés", curso.getIdioma());
    }

    @Test
    public void testCursoVacioConstructor() {
        Curso cursoVacio = new Curso();

        assertNull(cursoVacio.getTitulo());
        assertNotNull(cursoVacio.getLecciones());
        assertTrue(cursoVacio.getLecciones().isEmpty());
    }
}
