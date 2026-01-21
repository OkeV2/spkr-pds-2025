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
    public void testActualizarRachaMismoDia() {
        // El constructor ya inicializa ultimoAcceso a la fecha actual
        // Llamar actualizarRacha el mismo día no debe cambiar la racha
        int rachaInicial = estadisticas.getRachaActual();
        estadisticas.actualizarRacha();
        // El mismo día no incrementa la racha
        assertEquals(rachaInicial, estadisticas.getRachaActual());
    }

    @Test
    public void testGetUltimoAccesoDefensiveCopy() {
        java.util.Date fecha = estadisticas.getUltimoAcceso();
        assertNotNull(fecha);
        long tiempoOriginal = fecha.getTime();

        // Modificar la fecha retornada no debe afectar al objeto interno
        fecha.setTime(0);

        assertEquals(tiempoOriginal, estadisticas.getUltimoAcceso().getTime());
    }

    @Test
    public void testGetId() {
        // El ID es null hasta que se persiste
        assertNull(estadisticas.getId());
    }
}