package es.um.pds.spkr.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "bibliotecas")
public class Biblioteca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Curso> cursos;
    
    public Biblioteca() {
        this.cursos = new ArrayList<>();
    }
    
    public void addCurso(Curso curso) {
        this.cursos.add(curso);
    }
    
    public void removeCurso(Curso curso) {
        this.cursos.remove(curso);
    }
    
    public List<Curso> getCursos() {
        return cursos;
    }
    
    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
}