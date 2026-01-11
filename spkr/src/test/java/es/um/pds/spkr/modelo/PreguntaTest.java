package es.um.pds.spkr.modelo;

public class PreguntaTest extends Pregunta {
    
    private String opcionCorrecta;
    private String opcionIncorrecta1;
    private String opcionIncorrecta2;
    
    public PreguntaTest() {
        super();
    }
    
    public PreguntaTest(String enunciado, int orden, String opcionCorrecta, 
                        String opcionIncorrecta1, String opcionIncorrecta2) {
        super(enunciado, orden);
        this.opcionCorrecta = opcionCorrecta;
        this.opcionIncorrecta1 = opcionIncorrecta1;
        this.opcionIncorrecta2 = opcionIncorrecta2;
    }
    
    @Override
    public boolean validarRespuesta(String respuesta) {
        return opcionCorrecta.equalsIgnoreCase(respuesta.trim());
    }
    
    // Getters y Setters
    
    public String getOpcionCorrecta() {
        return opcionCorrecta;
    }
    
    public void setOpcionCorrecta(String opcionCorrecta) {
        this.opcionCorrecta = opcionCorrecta;
    }
    
    public String getOpcionIncorrecta1() {
        return opcionIncorrecta1;
    }
    
    public void setOpcionIncorrecta1(String opcionIncorrecta1) {
        this.opcionIncorrecta1 = opcionIncorrecta1;
    }
    
    public String getOpcionIncorrecta2() {
        return opcionIncorrecta2;
    }
    
    public void setOpcionIncorrecta2(String opcionIncorrecta2) {
        this.opcionIncorrecta2 = opcionIncorrecta2;
    }
