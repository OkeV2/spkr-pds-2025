package es.um.pds.spkr.cargador;

import java.io.File;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.um.pds.spkr.modelo.Curso;

public class CargadorCursosJSON implements CargadorCursos {

    private ObjectMapper mapper;

    public CargadorCursosJSON() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public Curso cargarCurso(String ruta) throws Exception {
        File archivo = new File(ruta);
        return mapper.readValue(archivo, Curso.class);
    }

    public Curso cargarCursoDesdeRecurso(String recurso) throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(recurso);
        if (is == null) {
            throw new IllegalArgumentException("Recurso no encontrado: " + recurso);
        }
        return mapper.readValue(is, Curso.class);
    }

    @Override
    public void exportarCurso(Curso curso, String ruta) throws Exception {
        File archivo = new File(ruta);
        mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, curso);
    }
}
