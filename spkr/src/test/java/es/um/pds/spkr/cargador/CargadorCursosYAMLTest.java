package es.um.pds.spkr.cargador;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.modelo.Leccion;
import es.um.pds.spkr.modelo.PreguntaTest;

public class CargadorCursosYAMLTest {

    private CargadorCursosYAML cargador;
    private String rutaTest = "curso_test.yaml";

    @BeforeEach
    public void setUp() {
        cargador = new CargadorCursosYAML();
    }

    @AfterEach
    public void tearDown() {
        File archivo = new File(rutaTest);
        if (archivo.exists()) {
            archivo.delete();
        }
    }

    @Test
    public void testExportarCurso() {
        Curso curso = new Curso("Francés Básico", "Curso de francés para principiantes", "Francés");

        assertDoesNotThrow(() -> {
            cargador.exportarCurso(curso, rutaTest);
        });

        File archivo = new File(rutaTest);
        assertTrue(archivo.exists());
    }

    @Test
    public void testCargarCurso() {
        Curso cursoOriginal = new Curso("Francés Básico", "Curso de francés para principiantes", "Francés");

        assertDoesNotThrow(() -> {
            cargador.exportarCurso(cursoOriginal, rutaTest);
            Curso cursoCargado = cargador.cargarCurso(rutaTest);

            assertEquals(cursoOriginal.getTitulo(), cursoCargado.getTitulo());
            assertEquals(cursoOriginal.getDescripcion(), cursoCargado.getDescripcion());
            assertEquals(cursoOriginal.getIdioma(), cursoCargado.getIdioma());
        });
    }

    @Test
    public void testCargarCursoConLecciones() {
        Curso cursoOriginal = new Curso("Alemán Básico", "Curso de alemán", "Alemán");
        Leccion leccion = new Leccion("Lección 1", "Saludos básicos", 1);
        cursoOriginal.addLeccion(leccion);

        assertDoesNotThrow(() -> {
            cargador.exportarCurso(cursoOriginal, rutaTest);
            Curso cursoCargado = cargador.cargarCurso(rutaTest);

            assertEquals(1, cursoCargado.getLecciones().size());
            assertEquals("Lección 1", cursoCargado.getLecciones().get(0).getNombre());
        });
    }

    @Test
    public void testCargarCursoConPreguntas() {
        Curso cursoOriginal = new Curso("Italiano Básico", "Curso de italiano", "Italiano");
        Leccion leccion = new Leccion("Lección 1", "Vocabulario", 1);
        PreguntaTest pregunta = new PreguntaTest("¿Cómo se dice hola?", 1, "ciao", "arrivederci", "grazie");
        leccion.addPregunta(pregunta);
        cursoOriginal.addLeccion(leccion);

        assertDoesNotThrow(() -> {
            cargador.exportarCurso(cursoOriginal, rutaTest);
            Curso cursoCargado = cargador.cargarCurso(rutaTest);

            assertEquals(1, cursoCargado.getLecciones().size());
            assertEquals(1, cursoCargado.getLecciones().get(0).getPreguntas().size());
        });
    }

    @Test
    public void testCargarCursoNoExistente() {
        assertThrows(Exception.class, () -> {
            cargador.cargarCurso("archivo_inexistente.yaml");
        });
    }

    @Test
    public void testExportarYCargarConExtensionYml() {
        String rutaYml = "curso_test.yml";
        Curso cursoOriginal = new Curso("Portugués Básico", "Curso de portugués", "Portugués");

        try {
            assertDoesNotThrow(() -> {
                cargador.exportarCurso(cursoOriginal, rutaYml);
                Curso cursoCargado = cargador.cargarCurso(rutaYml);

                assertEquals(cursoOriginal.getTitulo(), cursoCargado.getTitulo());
            });
        } finally {
            File archivo = new File(rutaYml);
            if (archivo.exists()) {
                archivo.delete();
            }
        }
    }
}
