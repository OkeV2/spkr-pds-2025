package es.um.pds.spkr.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BibliotecaTest {

    private Biblioteca biblioteca;

    @BeforeEach
    public void setUp() {
        biblioteca = new Biblioteca();
    }

    @Test
    public void testBibliotecaInicial() {
        assertNotNull(biblioteca.getCursos());
        assertTrue(biblioteca.getCursos().isEmpty());
    }

    @Test
    public void testAddCurso() {
        Curso curso = new Curso("Inglés Básico", "Descripción", "Inglés");
        biblioteca.addCurso(curso);

        assertEquals(1, biblioteca.getCursos().size());
        assertEquals(curso, biblioteca.getCursos().get(0));
    }

    @Test
    public void testAddMultiplesCursos() {
        Curso curso1 = new Curso("Inglés Básico", "Descripción 1", "Inglés");
        Curso curso2 = new Curso("Francés Básico", "Descripción 2", "Francés");
        Curso curso3 = new Curso("Alemán Básico", "Descripción 3", "Alemán");

        biblioteca.addCurso(curso1);
        biblioteca.addCurso(curso2);
        biblioteca.addCurso(curso3);

        assertEquals(3, biblioteca.getCursos().size());
    }

    @Test
    public void testRemoveCurso() {
        Curso curso = new Curso("Inglés Básico", "Descripción", "Inglés");
        biblioteca.addCurso(curso);
        biblioteca.removeCurso(curso);

        assertTrue(biblioteca.getCursos().isEmpty());
    }

    @Test
    public void testRemoveCursoEspecifico() {
        Curso curso1 = new Curso("Inglés Básico", "Descripción 1", "Inglés");
        Curso curso2 = new Curso("Francés Básico", "Descripción 2", "Francés");

        biblioteca.addCurso(curso1);
        biblioteca.addCurso(curso2);
        biblioteca.removeCurso(curso1);

        assertEquals(1, biblioteca.getCursos().size());
        assertEquals(curso2, biblioteca.getCursos().get(0));
    }

    @Test
    public void testGetCursosDevuelveListaInmodificable() {
        Curso curso = new Curso("Inglés Básico", "Descripción", "Inglés");
        biblioteca.addCurso(curso);

        assertThrows(UnsupportedOperationException.class, () -> {
            biblioteca.getCursos().add(new Curso("Otro", "Otro", "Otro"));
        });
    }

    @Test
    public void testRemoveCursoNoExistente() {
        Curso curso1 = new Curso("Inglés Básico", "Descripción 1", "Inglés");
        Curso curso2 = new Curso("Francés Básico", "Descripción 2", "Francés");

        biblioteca.addCurso(curso1);
        biblioteca.removeCurso(curso2);

        assertEquals(1, biblioteca.getCursos().size());
    }
}
