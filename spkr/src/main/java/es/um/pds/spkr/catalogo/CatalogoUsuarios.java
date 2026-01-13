package es.um.pds.spkr.catalogo;

import java.util.ArrayList;
import java.util.List;

import es.um.pds.spkr.modelo.Usuario;

public class CatalogoUsuarios {
    
    private List<Usuario> usuarios;
    
    public CatalogoUsuarios() {
        this.usuarios = new ArrayList<>();
    }
    
    public void addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }
    
    public Usuario getUsuario(String nombreUsuario) {
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equals(nombreUsuario)) {
                return u;
            }
        }
        return null;
    }
    
    public boolean existeUsuario(String nombreUsuario) {
        return getUsuario(nombreUsuario) != null;
    }
    
    public boolean existeEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean validarCredenciales(String nombreUsuario, String password) {
        Usuario u = getUsuario(nombreUsuario);
        if (u != null) {
            return u.getPassword().equals(password);
        }
        return false;
    }
    
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}