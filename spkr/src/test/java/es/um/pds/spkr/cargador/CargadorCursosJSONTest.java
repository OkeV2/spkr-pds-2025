package es.um.pds.spkr.cargador;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.modelo.Leccion;
import es.um.pds.spkr.modelo.PreguntaTest;

public class CargadorCursosJSONTest {
    
    private CargadorCursosJSON cargador;
    private String rutaTest = "curso_test.json";
    
    @BeforeEach
    public void setUp() {
        cargador = new CargadorCursosJSON();
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
        Curso curso = new Curso("Inglés Básico", "Curso de inglés para principiantes", "Inglés");
        
        assertDoesNotThrow(() -> {
            cargador.exportarCurso(curso, rutaTest);
        });
        
        File archivo = new File(rutaTest);
        assertTrue(archivo.exists());
    }
    
    @Test
    public void testCargarCurso() {
        Curso cursoOriginal = new Curso("Inglés Básico", "Curso de inglés para principiantes", "Inglés");
        
        assertDoesNotThrow(() -> {
            cargador.exportarCurso(cursoOriginal, rutaTest);
            Curso cursoCargado = cargador.cargarCurso(rutaTest);
            
            assertEquals(cursoOriginal.getTitulo(), cursoCargado.getTitulo());
            assertEquals(cursoOriginal.getDescripcion(), cursoCargado.getDescripcion());
            assertEquals(cursoOriginal.getIdioma(), cursoCargado.getIdioma());
        });
    }
}