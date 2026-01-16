package es.um.pds.spkr.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EstadisticasTest {
    
    private Estadisticas estadisticas;
    
    @BeforeEach
    public void setUp() {
        estadisticas = new Estadisticas();
    }
    
    @Test
    public void testEstadisticasIniciales() {
        assertEquals(0, estadisticas.getTiempoTotalUso());
        assertEquals(0, estadisticas.getRachaActual());
        assertEquals(0, estadisticas.getMejorRacha());
        assertEquals(0, estadisticas.getEjerciciosCompletados());
    }
    
    @Test
    public void testIncrementarTiempo() {
        estadisticas.incrementarTiempo(30);
        assertEquals(30, estadisticas.getTiempoTotalUso());
        
        estadisticas.incrementarTiempo(15);
        assertEquals(45, estadisticas.getTiempoTotalUso());
    }
    
    @Test
    public void testIncrementarEjercicios() {
        estadisticas.incrementarEjercicios();
        assertEquals(1, estadisticas.getEjerciciosCompletados());
        
        estadisticas.incrementarEjercicios();
        estadisticas.incrementarEjercicios();
        assertEquals(3, estadisticas.getEjerciciosCompletados());
    }
    
    @Test
    public void testActualizarRacha() {
        // Primera llamada - inicializa la racha
        estadisticas.setUltimoAcceso(null);
        estadisticas.actualizarRacha();
        assertEquals(1, estadisticas.getRachaActual());
        assertEquals(1, estadisticas.getMejorRacha());
    }
}