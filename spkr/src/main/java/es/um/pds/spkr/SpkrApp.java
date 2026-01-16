package es.um.pds.spkr;

import es.um.pds.spkr.catalogo.CatalogoCursos;
import es.um.pds.spkr.catalogo.CatalogoUsuarios;
import es.um.pds.spkr.modelo.Usuario;
import es.um.pds.spkr.modelo.Estadisticas;
import es.um.pds.spkr.modelo.Biblioteca;
import es.um.pds.spkr.modelo.ErrorFrecuente;
import es.um.pds.spkr.persistencia.GestorPersistencia;

public class SpkrApp {
    
    private CatalogoUsuarios catalogoUsuarios;
    private CatalogoCursos catalogoCursos;
    private Usuario usuarioActual;
    
    public SpkrApp() {
        this.catalogoUsuarios = new CatalogoUsuarios();
        this.catalogoCursos = new CatalogoCursos();
        this.usuarioActual = null;
    }
    
    public boolean registrarUsuario(String nombreUsuario, String email, String password) {
        if (catalogoUsuarios.existeUsuario(nombreUsuario)) {
            return false;
        }
        if (catalogoUsuarios.existeEmail(email)) {
            return false;
        }
        
        Usuario nuevoUsuario = new Usuario(nombreUsuario, email, password);
        nuevoUsuario.setEstadisticas(new Estadisticas());
        nuevoUsuario.setBiblioteca(new Biblioteca());
        catalogoUsuarios.addUsuario(nuevoUsuario);
        return true;
    }
    
    public boolean login(String nombreUsuario, String password) {
        if (catalogoUsuarios.validarCredenciales(nombreUsuario, password)) {
            this.usuarioActual = catalogoUsuarios.getUsuario(nombreUsuario);
            usuarioActual.getEstadisticas().actualizarRacha();
            catalogoUsuarios.actualizarUsuario(usuarioActual);
            return true;
        }
        return false;
    }
    
    public void logout() {
        if (usuarioActual != null) {
            catalogoUsuarios.actualizarUsuario(usuarioActual);
        }
        this.usuarioActual = null;
    }
    
    public void guardarProgreso() {
        if (usuarioActual != null) {
            catalogoUsuarios.actualizarUsuario(usuarioActual);
        }
    }
    
    public boolean estaLogueado() {
        return this.usuarioActual != null;
    }
    
    public void cerrar() {
        if (usuarioActual != null) {
            catalogoUsuarios.actualizarUsuario(usuarioActual);
        }
        GestorPersistencia.getInstancia().cerrar();
    }
    
    public void eliminarErrorFrecuente(ErrorFrecuente error) {
        if (usuarioActual != null) {
            usuarioActual.removeErrorFrecuente(error);
            GestorPersistencia.getInstancia().eliminar(error);
        }
    }
    
    // Getters
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public CatalogoUsuarios getCatalogoUsuarios() {
        return catalogoUsuarios;
    }
    
    public CatalogoCursos getCatalogoCursos() {
        return catalogoCursos;
    }
}