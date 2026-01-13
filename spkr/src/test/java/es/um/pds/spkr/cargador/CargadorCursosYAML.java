package es.um.pds.spkr.cargador;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import es.um.pds.spkr.modelo.Curso;

public class CargadorCursosYAML implements CargadorCursos {
    
    private ObjectMapper mapper;
    
    public CargadorCursosYAML() {
        this.mapper = new ObjectMapper(new YAMLFactory());
    }
    
    @Override
    public Curso cargarCurso(String ruta) throws Exception {
        File archivo = new File(ruta);
        return mapper.readValue(archivo, Curso.class);
    }
    
    @Override
    public void exportarCurso(Curso curso, String ruta) throws Exception {
        File archivo = new File(ruta);
        mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, curso);
    }
}