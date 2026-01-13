package es.um.pds.spkr.modelo;

public class PreguntaHuecos extends Pregunta {
    
    private String fraseCompleta;
    private String palabraOculta;
    
    public PreguntaHuecos() {
        super();
    }
    
    public PreguntaHuecos(String enunciado, int orden, String fraseCompleta, String palabraOculta) {
        super(enunciado, orden);
        this.fraseCompleta = fraseCompleta;
        this.palabraOculta = palabraOculta;
    }
    
    @Override
    public boolean validarRespuesta(String respuesta) {
        return palabraOculta.equalsIgnoreCase(respuesta.trim());
    }
    
    // Getters y Setters
    
    public String getFraseCompleta() {
        return fraseCompleta;
    }
    
    public void setFraseCompleta(String fraseCompleta) {
        this.fraseCompleta = fraseCompleta;
    }
    
    public String getPalabraOculta() {
        return palabraOculta;
    }
    
    public void setPalabraOculta(String palabraOculta) {
        this.palabraOculta = palabraOculta;
    }
}