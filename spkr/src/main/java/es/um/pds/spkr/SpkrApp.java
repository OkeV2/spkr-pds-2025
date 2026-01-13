package es.um.pds.spkr;

import es.um.pds.spkr.catalogo.CatalogoCursos;
import es.um.pds.spkr.catalogo.CatalogoUsuarios;
import es.um.pds.spkr.modelo.Usuario;
import es.um.pds.spkr.modelo.Estadisticas;
import es.um.pds.spkr.modelo.Biblioteca;

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
            return true;
        }
        return false;
    }
    
    public void logout() {
        this.usuarioActual = null;
    }
    
    public boolean estaLogueado() {
        return this.usuarioActual != null;
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