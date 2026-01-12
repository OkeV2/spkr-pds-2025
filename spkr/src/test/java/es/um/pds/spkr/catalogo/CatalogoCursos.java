package es.um.pds.spkr.catalogo;

import java.util.ArrayList;
import java.util.List;

import es.um.pds.spkr.modelo.Curso;

public class CatalogoCursos {
    
    private List<Curso> cursos;
    
    public CatalogoCursos() {
        this.cursos = new ArrayList<>();
    }
    
    public void addCurso(Curso curso) {
        this.cursos.add(curso);
    }
    
    public void removeCurso(Curso curso) {
        this.cursos.remove(curso);
    }
    
    public Curso getCurso(String titulo) {
        for (Curso c : cursos) {
            if (c.getTitulo().equals(titulo)) {
                return c;
            }
        }
        return null;
    }
    
    public boolean existeCurso(String titulo) {
        return getCurso(titulo) != null;
    }
    
    public List<Curso> getCursos() {
        return cursos;
    }
}