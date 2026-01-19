package es.um.pds.spkr.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String nombreUsuario;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Estadisticas estadisticas;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Biblioteca biblioteca;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Progreso> progresos;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<ErrorFrecuente> erroresFrecuentes;
    
    public Usuario() {
        this.progresos = new ArrayList<>();
        this.erroresFrecuentes = new ArrayList<>();
    }
    
    public Usuario(String nombreUsuario, String email, String password) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.fechaRegistro = new Date();
        this.progresos = new ArrayList<>();
        this.erroresFrecuentes = new ArrayList<>();
    }
    
    public void addProgreso(Progreso progreso) {
        this.progresos.add(progreso);
    }
    
    public void addErrorFrecuente(ErrorFrecuente error) {
        this.erroresFrecuentes.add(error);
    }
    
    public void removeErrorFrecuente(ErrorFrecuente error) {
        this.erroresFrecuentes.remove(error);
    }
    
    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Date getFechaRegistro() {
        return fechaRegistro != null ? new Date(fechaRegistro.getTime()) : null;
    }
    
    public Estadisticas getEstadisticas() {
        return estadisticas;
    }
    
    public void setEstadisticas(Estadisticas estadisticas) {
        this.estadisticas = estadisticas;
    }
    
    public Biblioteca getBiblioteca() {
        return biblioteca;
    }
    
    public void setBiblioteca(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }
    
    public List<Progreso> getProgresos() {
        return Collections.unmodifiableList(progresos);
    }

    public List<ErrorFrecuente> getErroresFrecuentes() {
        return Collections.unmodifiableList(erroresFrecuentes);
    }
}