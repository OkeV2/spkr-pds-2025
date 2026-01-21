package es.um.pds.spkr;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpkrAppTest {

    private SpkrApp app;
    private String testUserId;

    @BeforeEach
    public void setUp() {
        app = new SpkrApp();
        // Generar ID único para evitar colisiones con datos persistentes
        testUserId = UUID.randomUUID().toString().substring(0, 8);
    }
    
    @Test
    public void testRegistrarUsuarioExitoso() {
        String usuario = "user_" + testUserId;
        boolean resultado = app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        assertTrue(resultado);
    }

    @Test
    public void testRegistrarUsuarioNombreDuplicado() {
        String usuario = "dup_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        boolean resultado = app.registrarUsuario(usuario, "otro_" + testUserId + "@email.com", "5678");
        assertFalse(resultado);
    }

    @Test
    public void testRegistrarUsuarioEmailDuplicado() {
        String email = "email_" + testUserId + "@email.com";
        app.registrarUsuario("userA_" + testUserId, email, "1234");
        boolean resultado = app.registrarUsuario("userB_" + testUserId, email, "5678");
        assertFalse(resultado);
    }

    @Test
    public void testLoginExitoso() {
        String usuario = "login_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        boolean resultado = app.login(usuario, "1234");
        assertTrue(resultado);
        assertTrue(app.estaLogueado());
    }

    @Test
    public void testLoginPasswordIncorrecto() {
        String usuario = "wrongpw_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        boolean resultado = app.login(usuario, "wrongpass");
        assertFalse(resultado);
        assertFalse(app.estaLogueado());
    }

    @Test
    public void testLoginUsuarioNoExiste() {
        boolean resultado = app.login("noexiste_" + testUserId, "1234");
        assertFalse(resultado);
        assertFalse(app.estaLogueado());
    }

    @Test
    public void testLogout() {
        String usuario = "logout_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");
        assertTrue(app.estaLogueado());

        app.logout();
        assertFalse(app.estaLogueado());
    }

    @Test
    public void testUsuarioTieneEstadisticas() {
        String usuario = "stats_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        assertNotNull(app.obtenerEstadisticasActuales());
    }

    @Test
    public void testUsuarioTieneBiblioteca() {
        String usuario = "biblio_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        assertNotNull(app.getCursosBiblioteca());
    }

    @Test
    public void testGetNombreUsuarioActual() {
        String usuario = "nombre_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        assertEquals(usuario, app.getNombreUsuarioActual());
    }

    @Test
    public void testGetNombreUsuarioActualSinLogin() {
        assertEquals("", app.getNombreUsuarioActual());
    }

    @Test
    public void testIncrementarEjerciciosCompletados() {
        String usuario = "ejerc_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        // Usar métodos encapsulados del controlador (MVC)
        assertEquals(0, app.obtenerEjerciciosCompletadosTotal());
        app.incrementarEjerciciosCompletados();
        assertEquals(1, app.obtenerEjerciciosCompletadosTotal());
    }

    @Test
    public void testIncrementarTiempoEstadisticas() {
        String usuario = "tiempo_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        // Usar métodos encapsulados del controlador (MVC)
        assertEquals(0, app.obtenerTiempoTotalUso());
        app.incrementarTiempoEstadisticas(120); // 2 minutos
        assertEquals(2, app.obtenerTiempoTotalUso());
    }

    @Test
    public void testTieneErroresFrecuentes() {
        String usuario = "errfrec_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        assertFalse(app.tieneErroresFrecuentes());
    }

    @Test
    public void testContarErroresFrecuentes() {
        String usuario = "conterr_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        assertEquals(0, app.contarErroresFrecuentes());
    }

    @Test
    public void testObtenerErroresFrecuentesActuales() {
        String usuario = "obterr_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

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

    // =====================================================
    // Tests para métodos encapsulados de estadísticas (MVC)
    // =====================================================

    @Test
    public void testObtenerRachaActual() {
        String usuario = "racha_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        // La racha se actualiza al hacer login
        assertTrue(app.obtenerRachaActual() >= 0);
    }

    @Test
    public void testObtenerMejorRacha() {
        String usuario = "mejor_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        assertTrue(app.obtenerMejorRacha() >= 0);
    }

    @Test
    public void testObtenerRachaActualSinLogin() {
        assertEquals(0, app.obtenerRachaActual());
    }

    @Test
    public void testObtenerMejorRachaSinLogin() {
        assertEquals(0, app.obtenerMejorRacha());
    }

    @Test
    public void testObtenerTiempoTotalUsoSinLogin() {
        assertEquals(0, app.obtenerTiempoTotalUso());
    }

    @Test
    public void testObtenerEjerciciosCompletadosTotalSinLogin() {
        assertEquals(0, app.obtenerEjerciciosCompletadosTotal());
    }

    // =====================================================
    // Tests para callback de ventana principal (MVC)
    // =====================================================

    @Test
    public void testSetCallbackActualizarVentanaPrincipal() {
        final boolean[] callbackEjecutado = {false};

        app.setCallbackActualizarVentanaPrincipal(() -> {
            callbackEjecutado[0] = true;
        });

        app.notificarActualizacionVentanaPrincipal();

        assertTrue(callbackEjecutado[0]);
    }

    @Test
    public void testNotificarActualizacionVentanaPrincipalSinCallback() {
        // No debe lanzar excepción si no hay callback registrado
        assertDoesNotThrow(() -> app.notificarActualizacionVentanaPrincipal());
    }

    // =====================================================
    // Tests para formatear tiempo
    // =====================================================

    @Test
    public void testFormatearTiempoSegundos() {
        assertEquals("00:30", app.formatearTiempo(30));
    }

    @Test
    public void testFormatearTiempoMinutos() {
        assertEquals("02:30", app.formatearTiempo(150));
    }

    @Test
    public void testFormatearTiempoHoras() {
        assertEquals("1:30:00", app.formatearTiempo(5400));
    }

    // =====================================================
    // Tests para cálculo de porcentajes
    // =====================================================

    @Test
    public void testCalcularPorcentajeAciertosSinLogin() {
        assertEquals(0, app.calcularPorcentajeAciertos());
    }

    @Test
    public void testCalcularPorcentajeAciertosConLogin() {
        String usuario = "porcent_" + testUserId;
        app.registrarUsuario(usuario, usuario + "@email.com", "1234");
        app.login(usuario, "1234");

        // Sin ejercicios completados, el porcentaje es 0
        assertEquals(0, app.calcularPorcentajeAciertos());
    }

    @Test
    public void testObtenerAciertosEstadisticasSinLogin() {
        assertEquals(0, app.obtenerAciertosEstadisticas());
    }
}