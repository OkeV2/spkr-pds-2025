package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.cargador.CargadorCursosJSON;
import es.um.pds.spkr.cargador.CargadorCursosYAML;
import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.modelo.Leccion;
import es.um.pds.spkr.modelo.Progreso;
import es.um.pds.spkr.util.EstilosApp;

public class VentanaPrincipal extends JFrame {
    
    private SpkrApp app;
    private JPanel panelCursos;
    private JScrollPane scrollPane;
    private int cursoSeleccionadoIndex = -1;
    private JPanel panelCursoSeleccionado = null;
    
    public VentanaPrincipal(SpkrApp app) {
        this.app = app;
        inicializarComponentes();
        actualizarListaCursos();
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Biblioteca de Cursos");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        EstilosApp.aplicarEstiloVentana(this);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        EstilosApp.aplicarEstiloPanel(panelPrincipal);
        
        // Panel superior con logo, título y usuario
        JPanel panelSuperior = new JPanel(new BorderLayout(15, 0));
        EstilosApp.aplicarEstiloPanel(panelSuperior);
        
        // Logo y título
        JPanel panelLogoTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        EstilosApp.aplicarEstiloPanel(panelLogoTitulo);
        
        JLabel lblLogo = new JLabel();
        ImageIcon logo = EstilosApp.getIcono(40, 40);
        if (logo != null) {
            lblLogo.setIcon(logo);
        }
        panelLogoTitulo.add(lblLogo);
        
        JLabel lblTitulo = new JLabel("Mis Cursos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(EstilosApp.COLOR_TEXTO);
        panelLogoTitulo.add(lblTitulo);
        
        panelSuperior.add(panelLogoTitulo, BorderLayout.WEST);
        
        // Panel usuario
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        EstilosApp.aplicarEstiloPanel(panelUsuario);
        
        JPanel panelSaludo = new JPanel();
        panelSaludo.setLayout(new BoxLayout(panelSaludo, BoxLayout.Y_AXIS));
        panelSaludo.setBackground(EstilosApp.COLOR_FONDO);
        
        JLabel lblSaludo = new JLabel("Hola, " + app.getUsuarioActual().getNombreUsuario());
        lblSaludo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSaludo.setForeground(EstilosApp.COLOR_TEXTO);
        lblSaludo.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel lblBienvenida = new JLabel("¿Listo para aprender hoy?");
        lblBienvenida.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblBienvenida.setForeground(EstilosApp.COLOR_SECUNDARIO);
        lblBienvenida.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        panelUsuario.add(panelSaludo);
        panelSaludo.add(lblBienvenida);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(220, 80, 80));
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setOpaque(true);
        
        btnCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(200, 60, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(220, 80, 80));
            }
        });
        
        panelUsuario.add(lblSaludo);
        panelUsuario.add(btnCerrarSesion);
        
        panelSuperior.add(panelUsuario, BorderLayout.EAST);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con cursos
        JPanel panelCentral = new JPanel(new BorderLayout(0, 15));
        EstilosApp.aplicarEstiloPanel(panelCentral);
        
        // Botones de acción superiores
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        EstilosApp.aplicarEstiloPanel(panelAcciones);
        
        JButton btnImportar = crearBotonAccion("+ Importar Curso", true);
        JButton btnExportar = crearBotonAccion("Exportar", false);
        
        panelAcciones.add(btnImportar);
        panelAcciones.add(btnExportar);
        
        panelCentral.add(panelAcciones, BorderLayout.NORTH);
        
        // Panel de cursos (grid)
        panelCursos = new JPanel();
        panelCursos.setLayout(new BoxLayout(panelCursos, BoxLayout.Y_AXIS));
        EstilosApp.aplicarEstiloPanel(panelCursos);
        
        scrollPane = new JScrollPane(panelCursos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // Panel derecho con opciones
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setPreferredSize(new Dimension(180, 0));
        EstilosApp.aplicarEstiloPanel(panelDerecho);
        
        JButton btnIniciar = crearBotonLateral("Iniciar Curso", EstilosApp.COLOR_PRIMARIO);
        JButton btnEstadisticas = crearBotonLateral("Estadísticas", EstilosApp.COLOR_SECUNDARIO);
        JButton btnRepasar = crearBotonLateral("Repasar Errores", EstilosApp.COLOR_SECUNDARIO);
        
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(btnIniciar);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(btnEstadisticas);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(btnRepasar);
        panelDerecho.add(Box.createVerticalGlue());
        
        panelPrincipal.add(panelDerecho, BorderLayout.EAST);
        
        add(panelPrincipal);
        
        // Eventos
        btnImportar.addActionListener(e -> importarCurso());
        btnExportar.addActionListener(e -> exportarCurso());
        btnIniciar.addActionListener(e -> iniciarCurso());
        btnEstadisticas.addActionListener(e -> verEstadisticas());
        btnRepasar.addActionListener(e -> repasarErrores());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }
    
    private JButton crearBotonAccion(String texto, boolean esPrimario) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        
        if (esPrimario) {
            boton.setBackground(EstilosApp.COLOR_PRIMARIO);
            boton.setForeground(Color.WHITE);
            boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    boton.setBackground(EstilosApp.COLOR_SECUNDARIO);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    boton.setBackground(EstilosApp.COLOR_PRIMARIO);
                }
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    boton.setBackground(new Color(50, 50, 50));
                }
                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    boton.setBackground(EstilosApp.COLOR_SECUNDARIO);
                }
            });
        } else {
            boton.setBackground(Color.WHITE);
            boton.setForeground(EstilosApp.COLOR_TEXTO);
            boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(9, 19, 9, 19)
            ));
            
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    boton.setBackground(new Color(245, 245, 245));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    boton.setBackground(Color.WHITE);
                }
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    boton.setBackground(new Color(230, 230, 230));
                }
                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    boton.setBackground(new Color(245, 245, 245));
                }
            });
        }
        
        return boton;
    }
    
    private JButton crearBotonLateral(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setMaximumSize(new Dimension(180, 50));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Color colorOriginal = color;
        Color colorHover = color.brighter();
        Color colorClick = color.darker();
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(colorHover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(colorOriginal);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                boton.setBackground(colorClick);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                boton.setBackground(colorHover);
            }
        });
        
        return boton;
    }
    
    private void actualizarListaCursos() {
        panelCursos.removeAll();
        cursoSeleccionadoIndex = -1;
        panelCursoSeleccionado = null;
        
        List<Curso> cursos = app.getUsuarioActual().getBiblioteca().getCursos();
        
        if (cursos.isEmpty()) {
            JPanel panelVacio = new JPanel();
            panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
            EstilosApp.aplicarEstiloPanel(panelVacio);
            panelVacio.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            
            JLabel lblVacio = new JLabel("No tienes cursos en tu biblioteca");
            lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblVacio.setForeground(new Color(150, 150, 150));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel lblAyuda = new JLabel("Haz clic en '+ Importar Curso' para añadir uno");
            lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblAyuda.setForeground(new Color(180, 180, 180));
            lblAyuda.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            panelVacio.add(lblVacio);
            panelVacio.add(Box.createRigidArea(new Dimension(0, 10)));
            panelVacio.add(lblAyuda);
            
            panelCursos.add(panelVacio);
        } else {
            for (int i = 0; i < cursos.size(); i++) {
                Curso curso = cursos.get(i);
                JPanel tarjetaCurso = crearTarjetaCurso(curso, i);
                panelCursos.add(tarjetaCurso);
                panelCursos.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        
        panelCursos.revalidate();
        panelCursos.repaint();
    }
    
    private JPanel crearTarjetaCurso(Curso curso, int index) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icono del idioma
        JLabel lblIcono = new JLabel("📚");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        tarjeta.add(lblIcono, BorderLayout.WEST);
        
        // Información del curso
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(curso.getTitulo());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(EstilosApp.COLOR_TEXTO);
        
        JLabel lblIdioma = new JLabel(curso.getIdioma());
        lblIdioma.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblIdioma.setForeground(EstilosApp.COLOR_SECUNDARIO);
        
        int numLecciones = curso.getLecciones().size();
        int numPreguntas = 0;
        for (Leccion l : curso.getLecciones()) {
            numPreguntas += l.getPreguntas().size();
        }
        
        JLabel lblDetalles = new JLabel(numLecciones + " lecciones • " + numPreguntas + " preguntas");
        lblDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDetalles.setForeground(new Color(150, 150, 150));
        
        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        panelInfo.add(lblIdioma);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        panelInfo.add(lblDetalles);
        
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        
     // Progreso si existe
        Progreso progreso = buscarProgreso(curso);
        if (progreso != null && progreso.getPreguntaActual() > 0) {
            int porcentaje = (progreso.getPreguntaActual() * 100) / numPreguntas;
            JLabel lblProgreso = new JLabel(porcentaje + "%");
            lblProgreso.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblProgreso.setForeground(EstilosApp.COLOR_EXITO);
            lblProgreso.setOpaque(false);
            
            tarjeta.add(lblProgreso, BorderLayout.EAST);
        }
        
        // Eventos
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarCurso(tarjeta, index);
                if (e.getClickCount() == 2) {
                    iniciarCurso();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (cursoSeleccionadoIndex != index) {
                    tarjeta.setBackground(new Color(250, 250, 252));
                    panelInfo.setBackground(new Color(250, 250, 252));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (cursoSeleccionadoIndex != index) {
                    tarjeta.setBackground(Color.WHITE);
                    panelInfo.setBackground(Color.WHITE);
                }
            }
        });
        
        return tarjeta;
    }
    
    private void seleccionarCurso(JPanel tarjeta, int index) {
        // Deseleccionar anterior
        if (panelCursoSeleccionado != null) {
            panelCursoSeleccionado.setBackground(Color.WHITE);
            panelCursoSeleccionado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
            ));
            actualizarFondoHijos(panelCursoSeleccionado, Color.WHITE);
        }
        
        // Seleccionar nuevo
        panelCursoSeleccionado = tarjeta;
        cursoSeleccionadoIndex = index;
        
        tarjeta.setBackground(new Color(240, 245, 255));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstilosApp.COLOR_PRIMARIO, 2),
            BorderFactory.createEmptyBorder(17, 19, 17, 19)
        ));
        actualizarFondoHijos(tarjeta, new Color(240, 245, 255));
    }
    
    private void actualizarFondoHijos(Container container, Color color) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(color);
                actualizarFondoHijos((Container) comp, color);
            }
        }
    }
    
    private Progreso buscarProgreso(Curso curso) {
        for (Progreso p : app.getUsuarioActual().getProgresos()) {
            if (p.getCurso() != null && p.getCurso().getTitulo() != null &&
                p.getCurso().getTitulo().equals(curso.getTitulo())) {
                return p;
            }
        }
        return null;
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
        if (cursoSeleccionadoIndex == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para exportar");
            return;
        }
        
        Curso curso = app.getUsuarioActual().getBiblioteca().getCursos().get(cursoSeleccionadoIndex);
        
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
        if (cursoSeleccionadoIndex == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para iniciar");
            return;
        }
        
        Curso curso = app.getUsuarioActual().getBiblioteca().getCursos().get(cursoSeleccionadoIndex);
        
        if (curso.getLecciones().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este curso no tiene lecciones");
            return;
        }
        
        // Buscar si hay progreso guardado
        Progreso progresoExistente = null;
        for (Progreso p : app.getUsuarioActual().getProgresos()) {
            if (p.getCurso() != null && p.getCurso().getTitulo() != null && 
                p.getCurso().getTitulo().equals(curso.getTitulo())) {
                progresoExistente = p;
                break;
            }
        }
        
        // Si hay progreso y no está completado, preguntar
        if (progresoExistente != null && progresoExistente.getPreguntaActual() > 0) {
            int totalPreguntas = 0;
            for (Leccion l : curso.getLecciones()) {
                totalPreguntas += l.getPreguntas().size();
            }
            
            if (progresoExistente.getPreguntaActual() < totalPreguntas) {
                int opcion = JOptionPane.showOptionDialog(this,
                    "Tienes un progreso guardado en este curso.\n" +
                    "Pregunta " + progresoExistente.getPreguntaActual() + " de " + totalPreguntas + "\n\n" +
                    "¿Qué deseas hacer?",
                    "Progreso encontrado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Continuar", "Empezar de cero"},
                    "Continuar");
                
                if (opcion == 0) {
                    // Continuar con la estrategia guardada
                    VentanaEjercicio ventanaEjercicio = new VentanaEjercicio(app, curso, progresoExistente.getEstrategia(), this, progresoExistente);
                    ventanaEjercicio.setVisible(true);
                    return;
                } else {
                    // Empezar de cero
                    progresoExistente.reiniciar();
                    app.guardarProgreso();
                }
            } else {
                // Curso completado, preguntar si quiere repetir
                int opcion = JOptionPane.showConfirmDialog(this,
                    "Ya has completado este curso.\n¿Deseas repetirlo desde el principio?",
                    "Curso completado",
                    JOptionPane.YES_NO_OPTION);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    progresoExistente.reiniciar();
                    app.guardarProgreso();
                } else {
                    return;
                }
            }
        }
        
        // Si no hay progreso, crear uno nuevo
        if (progresoExistente == null) {
            progresoExistente = new Progreso(curso);
            app.getUsuarioActual().addProgreso(progresoExistente);
            app.guardarProgreso();
        }
        
        VentanaSeleccionEstrategia ventanaEstrategia = new VentanaSeleccionEstrategia(app, curso, this, progresoExistente);
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