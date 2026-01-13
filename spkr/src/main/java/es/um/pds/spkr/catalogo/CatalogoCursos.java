package es.um.pds.spkr.catalogo;

import java.util.List;

import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.persistencia.GestorPersistencia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class CatalogoCursos {
    
    private GestorPersistencia gestor;
    
    public CatalogoCursos() {
        this.gestor = GestorPersistencia.getInstancia();
    }
    
    public void addCurso(Curso curso) {
        gestor.guardar(curso);
    }
    
    public void removeCurso(Curso curso) {
        gestor.eliminar(curso);
    }
    
    public Curso getCurso(String titulo) {
        EntityManager em = gestor.getEntityManager();
        TypedQuery<Curso> query = em.createQuery(
            "SELECT c FROM Curso c WHERE c.titulo = :titulo", Curso.class);
        query.setParameter("titulo", titulo);
        
        List<Curso> resultados = query.getResultList();
        if (resultados.isEmpty()) {
            return null;
        }
        return resultados.get(0);
    }
    
    public boolean existeCurso(String titulo) {
        return getCurso(titulo) != null;
    }
    
    public List<Curso> getCursos() {
        EntityManager em = gestor.getEntityManager();
        TypedQuery<Curso> query = em.createQuery(
            "SELECT c FROM Curso c", Curso.class);
        return query.getResultList();
    }
    
    public void actualizarCurso(Curso curso) {
        gestor.actualizar(curso);
    }
}