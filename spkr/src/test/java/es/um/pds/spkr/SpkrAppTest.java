package es.um.pds.spkr;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpkrAppTest {
    
    private SpkrApp app;
    
    @BeforeEach
    public void setUp() {
        app = new SpkrApp();
    }
    
    @Test
    public void testRegistrarUsuarioExitoso() {
        boolean resultado = app.registrarUsuario("juan", "juan@email.com", "1234");
        assertTrue(resultado);
    }
    
    @Test
    public void testRegistrarUsuarioNombreDuplicado() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        boolean resultado = app.registrarUsuario("juan", "otro@email.com", "5678");
        assertFalse(resultado);
    }
    
    @Test
    public void testRegistrarUsuarioEmailDuplicado() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        boolean resultado = app.registrarUsuario("pedro", "juan@email.com", "5678");
        assertFalse(resultado);
    }
    
    @Test
    public void testLoginExitoso() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        boolean resultado = app.login("juan", "1234");
        assertTrue(resultado);
        assertTrue(app.estaLogueado());
    }
    
    @Test
    public void testLoginPasswordIncorrecto() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        boolean resultado = app.login("juan", "wrongpass");
        assertFalse(resultado);
        assertFalse(app.estaLogueado());
    }
    
    @Test
    public void testLoginUsuarioNoExiste() {
        boolean resultado = app.login("noexiste", "1234");
        assertFalse(resultado);
        assertFalse(app.estaLogueado());
    }
    
    @Test
    public void testLogout() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");
        assertTrue(app.estaLogueado());
        
        app.logout();
        assertFalse(app.estaLogueado());
    }
    
    @Test
    public void testUsuarioTieneEstadisticas() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");

        assertNotNull(app.obtenerEstadisticasActuales());
    }

    @Test
    public void testUsuarioTieneBiblioteca() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");

        assertNotNull(app.getCursosBiblioteca());
    }

    @Test
    public void testGetNombreUsuarioActual() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");

        assertEquals("juan", app.getNombreUsuarioActual());
    }

    @Test
    public void testGetNombreUsuarioActualSinLogin() {
        assertEquals("", app.getNombreUsuarioActual());
    }

    @Test
    public void testIncrementarEjerciciosCompletados() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");

        assertEquals(0, app.obtenerEstadisticasActuales().getEjerciciosCompletados());
        app.incrementarEjerciciosCompletados();
        assertEquals(1, app.obtenerEstadisticasActuales().getEjerciciosCompletados());
    }

    @Test
    public void testIncrementarTiempoEstadisticas() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");

        assertEquals(0, app.obtenerEstadisticasActuales().getTiempoTotalUso());
        app.incrementarTiempoEstadisticas(120); // 2 minutos
        assertEquals(2, app.obtenerEstadisticasActuales().getTiempoTotalUso());
    }

    @Test
    public void testTieneErroresFrecuentes() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");

        assertFalse(app.tieneErroresFrecuentes());
    }

    @Test
    public void testContarErroresFrecuentes() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");

        assertEquals(0, app.contarErroresFrecuentes());
    }

    @Test
    public void testObtenerErroresFrecuentesActuales() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");

        assertNotNull(app.obtenerErroresFrecuentesActuales());
        assertTrue(app.obtenerErroresFrecuentesActuales().isEmpty());
    }

    @Test
    public void testCrearEstrategiaSecuencial() {
        assertNotNull(app.crearEstrategia("Secuencial"));
        assertEquals("Secuencial", app.crearEstrategia("Secuencial").getNombre());
    }

    @Test
    public void testCrearEstrategiaAleatoria() {
        assertNotNull(app.crearEstrategia("Aleatoria"));
        assertEquals("Aleatoria", app.crearEstrategia("Aleatoria").getNombre());
    }

    @Test
    public void testCrearEstrategiaRepeticionEspaciada() {
        assertNotNull(app.crearEstrategia("Repetición Espaciada"));
        assertEquals("Repetición Espaciada", app.crearEstrategia("Repetición Espaciada").getNombre());
    }

    @Test
    public void testCrearEstrategiaDesconocidaDevuelveSecuencial() {
        assertNotNull(app.crearEstrategia("Desconocida"));
        assertEquals("Secuencial", app.crearEstrategia("Desconocida").getNombre());
    }
}