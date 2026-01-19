package es.um.pds.spkr.modelo;

/**
 * Clase que encapsula el resultado de procesar una respuesta de ejercicio.
 * Forma parte del modelo y es devuelta por el controlador.
 */
public class ResultadoRespuesta {

    private boolean correcta;
    private boolean yaContada;
    private String respuestaCorrecta;
    private int aciertosActuales;
    private int erroresActuales;

    public ResultadoRespuesta(boolean correcta, boolean yaContada, String respuestaCorrecta,
                              int aciertosActuales, int erroresActuales) {
        this.correcta = correcta;
        this.yaContada = yaContada;
        this.respuestaCorrecta = respuestaCorrecta;
        this.aciertosActuales = aciertosActuales;
        this.erroresActuales = erroresActuales;
    }

    public boolean isCorrecta() {
        return correcta;
    }

    public boolean isYaContada() {
        return yaContada;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public int getAciertosActuales() {
        return aciertosActuales;
    }

    public int getErroresActuales() {
        return erroresActuales;
    }
}
