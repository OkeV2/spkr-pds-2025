package es.um.pds.spkr.modelo;

public class PreguntaTraduccion extends Pregunta {
    
    private String respuestaCorrecta;
    
    public PreguntaTraduccion() {
        super();
    }
    
    public PreguntaTraduccion(String enunciado, int orden, String respuestaCorrecta) {
        super(enunciado, orden);
        this.respuestaCorrecta = respuestaCorrecta;
    }
    
    @Override
    public boolean validarRespuesta(String respuesta) {
        return respuestaCorrecta.equalsIgnoreCase(respuesta.trim());
    }
    
    // Getters y Setters
    
    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
    
    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }
}