package es.um.pds.spkr.cargador;

import es.um.pds.spkr.modelo.Curso;

public interface CargadorCursos {
    
    Curso cargarCurso(String ruta) throws Exception;
    
    void exportarCurso(Curso curso, String ruta) throws Exception;
}