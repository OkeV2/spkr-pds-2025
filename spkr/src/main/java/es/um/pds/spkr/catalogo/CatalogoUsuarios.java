package es.um.pds.spkr.catalogo;

import java.util.List;

import es.um.pds.spkr.modelo.Usuario;
import es.um.pds.spkr.persistencia.GestorPersistencia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class CatalogoUsuarios {
    
    private GestorPersistencia gestor;
    
    public CatalogoUsuarios() {
        this.gestor = GestorPersistencia.getInstancia();
    }
    
    public void addUsuario(Usuario usuario) {
        gestor.guardar(usuario);
    }
    
    public Usuario getUsuario(String nombreUsuario) {
        EntityManager em = gestor.getEntityManager();
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombre", Usuario.class);
        query.setParameter("nombre", nombreUsuario);
        
        List<Usuario> resultados = query.getResultList();
        if (resultados.isEmpty()) {
            return null;
        }
        return resultados.get(0);
    }
    
    public boolean existeUsuario(String nombreUsuario) {
        return getUsuario(nombreUsuario) != null;
    }
    
    public boolean existeEmail(String email) {
        EntityManager em = gestor.getEntityManager();
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
        query.setParameter("email", email);
        
        return !query.getResultList().isEmpty();
    }
    
    public boolean validarCredenciales(String nombreUsuario, String password) {
        Usuario u = getUsuario(nombreUsuario);
        if (u != null) {
            return u.getPassword().equals(password);
        }
        return false;
    }
    
    public List<Usuario> getUsuarios() {
        EntityManager em = gestor.getEntityManager();
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u", Usuario.class);
        return query.getResultList();
    }
    
    public void actualizarUsuario(Usuario usuario) {
        gestor.actualizar(usuario);
    }
}