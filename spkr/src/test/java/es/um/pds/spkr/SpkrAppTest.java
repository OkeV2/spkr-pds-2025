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
        
        assertNotNull(app.getUsuarioActual().getEstadisticas());
    }
    
    @Test
    public void testUsuarioTieneBiblioteca() {
        app.registrarUsuario("juan", "juan@email.com", "1234");
        app.login("juan", "1234");
        
        assertNotNull(app.getUsuarioActual().getBiblioteca());
    }
}