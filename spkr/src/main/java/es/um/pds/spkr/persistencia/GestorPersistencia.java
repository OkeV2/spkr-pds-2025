package es.um.pds.spkr.persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class GestorPersistencia {
    
    private static final String PERSISTENCE_UNIT = "spkrPU";
    private static GestorPersistencia instancia;
    private EntityManagerFactory emf;
    private EntityManager em;
    
    private GestorPersistencia() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        em = emf.createEntityManager();
    }
    
    public static GestorPersistencia getInstancia() {
        if (instancia == null) {
            instancia = new GestorPersistencia();
        }
        return instancia;
    }
    
    public EntityManager getEntityManager() {
        return em;
    }
    
    public void iniciarTransaccion() {
        em.getTransaction().begin();
    }
    
    public void confirmarTransaccion() {
        em.getTransaction().commit();
    }
    
    public void cancelarTransaccion() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
    
    public void guardar(Object entidad) {
        iniciarTransaccion();
        em.persist(entidad);
        confirmarTransaccion();
    }
    
    public void actualizar(Object entidad) {
        iniciarTransaccion();
        em.merge(entidad);
        confirmarTransaccion();
    }
    
    public void eliminar(Object entidad) {
        iniciarTransaccion();
        em.remove(em.merge(entidad));
        confirmarTransaccion();
    }
    
    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}