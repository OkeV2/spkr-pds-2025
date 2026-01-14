package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.cargador.CargadorCursosJSON;
import es.um.pds.spkr.cargador.CargadorCursosYAML;
import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.util.EstilosApp;

public class VentanaPrincipal extends JFrame {
    
    private SpkrApp app;
    private JList<String> listaCursos;
    private DefaultListModel<String> modeloLista;
    private JButton btnImportar;
    private JButton btnExportar;
    private JButton btnIniciarCurso;
    private JButton btnEstadisticas;
    private JButton btnRepasarErrores;
    private JButton btnCerrarSesion;
    
    public VentanaPrincipal(SpkrApp app) {
        this.app = app;
        inicializarComponentes();
        actualizarListaCursos();
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Biblioteca de Cursos");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        EstilosApp.aplicarEstiloVentana(this);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        EstilosApp.aplicarEstiloPanel(panelPrincipal);
        
        // Panel superior con logo y usuario
        JPanel panelSuperior = new JPanel(new BorderLayout());
        EstilosApp.aplicarEstiloPanel(panelSuperior);
        
        // Logo pequeño
        JLabel lblLogo = new JLabel();
        ImageIcon logo = EstilosApp.getIcono(40, 40);
        if (logo != null) {
            lblLogo.setIcon(logo);
        }
        panelSuperior.add(lblLogo, BorderLayout.WEST);
        
        // Título y usuario
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        EstilosApp.aplicarEstiloPanel(panelTitulo);
        JLabel lblTitulo = EstilosApp.crearSubtitulo("Mis Cursos");
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo, BorderLayout.CENTER);
        
        JLabel lblUsuario = EstilosApp.crearEtiqueta("Hola, " + app.getUsuarioActual().getNombreUsuario());
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        panelSuperior.add(lblUsuario, BorderLayout.EAST);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Lista de cursos
        modeloLista = new DefaultListModel<>();
        listaCursos = new JList<>(modeloLista);
        listaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCursos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        listaCursos.setBackground(Color.WHITE);
        listaCursos.setFixedCellHeight(50);
        listaCursos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listaCursos.setSelectionBackground(new Color(230, 230, 230));
        listaCursos.setSelectionForeground(EstilosApp.COLOR_TEXTO);
        
        JScrollPane scrollPane = new JScrollPane(listaCursos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        EstilosApp.aplicarEstiloPanel(panelBotones);
        
        btnImportar = new JButton("Importar Curso");
        btnExportar = new JButton("Exportar Curso");
        btnIniciarCurso = new JButton("Iniciar Curso");
        btnEstadisticas = new JButton("Estadísticas");
        btnRepasarErrores = new JButton("Repasar Errores");
        btnCerrarSesion = new JButton("Cerrar Sesión");
        
        EstilosApp.aplicarEstiloBoton(btnImportar);
        EstilosApp.aplicarEstiloBoton(btnExportar);
        EstilosApp.aplicarEstiloBoton(btnIniciarCurso);
        EstilosApp.aplicarEstiloBoton(btnEstadisticas);
        EstilosApp.aplicarEstiloBoton(btnRepasarErrores);
        EstilosApp.aplicarEstiloBotonSecundario(btnCerrarSesion);
        
        Dimension botonSize = new Dimension(160, 40);
        btnImportar.setMaximumSize(botonSize);
        btnExportar.setMaximumSize(botonSize);
        btnIniciarCurso.setMaximumSize(botonSize);
        btnEstadisticas.setMaximumSize(botonSize);
        btnRepasarErrores.setMaximumSize(botonSize);
        btnCerrarSesion.setMaximumSize(botonSize);
        
        panelBotones.add(btnImportar);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotones.add(btnExportar);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotones.add(btnIniciarCurso);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotones.add(btnEstadisticas);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotones.add(btnRepasarErrores);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 30)));
        panelBotones.add(btnCerrarSesion);
        
        panelPrincipal.add(panelBotones, BorderLayout.EAST);
        
        add(panelPrincipal);
        
        // Eventos
        btnImportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importarCurso();
            }
        });
        
        btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarCurso();
            }
        });
        
        btnIniciarCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarCurso();
            }
        });
        
        btnEstadisticas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verEstadisticas();
            }
        });
        
        btnRepasarErrores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repasarErrores();
            }
        });
        
        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
    }
    
    private void actualizarListaCursos() {
        modeloLista.clear();
        List<Curso> cursos = app.getUsuarioActual().getBiblioteca().getCursos();
        for (Curso curso : cursos) {
            modeloLista.addElement(curso.getTitulo() + " - " + curso.getIdioma());
        }
    }
    
    private void importarCurso() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de curso");
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String ruta = archivo.getAbsolutePath();
            
            try {
                Curso curso;
                if (ruta.endsWith(".json")) {
                    CargadorCursosJSON cargador = new CargadorCursosJSON();
                    curso = cargador.cargarCurso(ruta);
                } else if (ruta.endsWith(".yaml") || ruta.endsWith(".yml")) {
                    CargadorCursosYAML cargador = new CargadorCursosYAML();
                    curso = cargador.cargarCurso(ruta);
                } else {
                    JOptionPane.showMessageDialog(this, "Formato no soportado. Use JSON o YAML.");
                    return;
                }
                
                app.getUsuarioActual().getBiblioteca().addCurso(curso);
                app.guardarProgreso();
                actualizarListaCursos();
                JOptionPane.showMessageDialog(this, "Curso importado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al importar: " + ex.getMessage());
            }
        }
    }
    
    private void exportarCurso() {
        int indice = listaCursos.getSelectedIndex();
        if (indice == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para exportar");
            return;
        }
        
        Curso curso = app.getUsuarioActual().getBiblioteca().getCursos().get(indice);
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar curso como");
        
        int resultado = fileChooser.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String ruta = archivo.getAbsolutePath();
            
            try {
                if (ruta.endsWith(".json")) {
                    CargadorCursosJSON cargador = new CargadorCursosJSON();
                    cargador.exportarCurso(curso, ruta);
                } else if (ruta.endsWith(".yaml") || ruta.endsWith(".yml")) {
                    CargadorCursosYAML cargador = new CargadorCursosYAML();
                    cargador.exportarCurso(curso, ruta);
                } else {
                    CargadorCursosJSON cargador = new CargadorCursosJSON();
                    cargador.exportarCurso(curso, ruta + ".json");
                }
                
                JOptionPane.showMessageDialog(this, "Curso exportado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
            }
        }
    }
    
    private void iniciarCurso() {
        int indice = listaCursos.getSelectedIndex();
        if (indice == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para iniciar");
            return;
        }
        
        Curso curso = app.getUsuarioActual().getBiblioteca().getCursos().get(indice);
        
        if (curso.getLecciones().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este curso no tiene lecciones");
            return;
        }
        
        VentanaSeleccionEstrategia ventanaEstrategia = new VentanaSeleccionEstrategia(app, curso, this);
        ventanaEstrategia.setVisible(true);
    }
    
    private void verEstadisticas() {
        VentanaEstadisticas ventanaEstadisticas = new VentanaEstadisticas(app);
        ventanaEstadisticas.setVisible(true);
    }
    
    private void repasarErrores() {
        if (app.getUsuarioActual().getErroresFrecuentes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tiene errores para repasar");
            return;
        }
        
        VentanaRepaso ventanaRepaso = new VentanaRepaso(app, this);
        ventanaRepaso.setVisible(true);
    }
    
    private void cerrarSesion() {
        app.logout();
        VentanaLogin ventanaLogin = new VentanaLogin(app);
        ventanaLogin.setVisible(true);
        this.dispose();
    }
}