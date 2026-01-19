package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.estrategia.EstrategiaAprendizaje;
import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.modelo.Progreso;
import es.um.pds.spkr.util.EstilosApp;

public class VentanaSeleccionEstrategia extends JFrame {
    
    private SpkrApp app;
    private Curso curso;
    private VentanaPrincipal ventanaPrincipal;
    private Progreso progreso;
    
    private JPanel panelSecuencial;
    private JPanel panelAleatoria;
    private JPanel panelRepeticion;
    private JPanel panelSeleccionado;
    
    private JButton btnIniciar;
    private JButton btnCancelar;
    
    private String estrategiaSeleccionada = "Secuencial";
    
    public VentanaSeleccionEstrategia(SpkrApp app, Curso curso, VentanaPrincipal ventanaPrincipal, Progreso progreso) {
        this.app = app;
        this.curso = curso;
        this.ventanaPrincipal = ventanaPrincipal;
        this.progreso = progreso;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Seleccionar Estrategia");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        EstilosApp.aplicarEstiloVentana(this);
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        EstilosApp.aplicarEstiloPanel(panel);
        
        // Icono
        JLabel lblIcono = new JLabel();
        ImageIcon icono = EstilosApp.getIcono(50, 50);
        if (icono != null) {
            lblIcono.setIcon(icono);
        }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblIcono);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Título
        JLabel lblTitulo = EstilosApp.crearSubtitulo("¿Cómo quieres aprender?");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Opciones como tarjetas
        panelSecuencial = crearTarjetaEstrategia(
            "Secuencial",
            "Preguntas en orden",
            "Ideal para tu primera vez con el curso"
        );
        
        panelAleatoria = crearTarjetaEstrategia(
            "Aleatoria", 
            "Preguntas al azar",
            "Perfecto para repasar y reforzar"
        );
        
        panelRepeticion = crearTarjetaEstrategia(
            "Repetición Espaciada",
            "Repite los fallos",
            "Enfócate en lo que más te cuesta"
        );
        
        panel.add(panelSecuencial);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelAleatoria);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelRepeticion);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Seleccionar secuencial por defecto
        seleccionarTarjeta(panelSecuencial, "Secuencial");
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        EstilosApp.aplicarEstiloPanel(panelBotones);
        
        btnIniciar = new JButton("Comenzar");
        btnCancelar = new JButton("Cancelar");
        
        EstilosApp.aplicarEstiloBoton(btnIniciar);
        EstilosApp.aplicarEstiloBotonSecundario(btnCancelar);
        
        panelBotones.add(btnIniciar);
        panelBotones.add(btnCancelar);
        
        panel.add(panelBotones);
        
        add(panel);
        
        // Eventos
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarCurso();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private JPanel crearTarjetaEstrategia(String titulo, String subtitulo, String descripcion) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        tarjeta.setMaximumSize(new Dimension(370, 80));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(EstilosApp.COLOR_TEXTO);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(EstilosApp.COLOR_SECUNDARIO);
        
        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDescripcion.setForeground(new Color(130, 130, 130));
        
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createRigidArea(new Dimension(0, 3)));
        panelTexto.add(lblSubtitulo);
        panelTexto.add(Box.createRigidArea(new Dimension(0, 3)));
        panelTexto.add(lblDescripcion);
        
        tarjeta.add(panelTexto, BorderLayout.CENTER);
        
        // Evento de clic
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarTarjeta(tarjeta, titulo);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (tarjeta != panelSeleccionado) {
                    tarjeta.setBackground(new Color(248, 248, 248));
                    panelTexto.setBackground(new Color(248, 248, 248));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (tarjeta != panelSeleccionado) {
                    tarjeta.setBackground(Color.WHITE);
                    panelTexto.setBackground(Color.WHITE);
                }
            }
        });
        
        return tarjeta;
    }
    
    private void seleccionarTarjeta(JPanel tarjeta, String estrategia) {
        // Deseleccionar anterior
        if (panelSeleccionado != null) {
            panelSeleccionado.setBackground(Color.WHITE);
            panelSeleccionado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            actualizarColorFondo(panelSeleccionado, Color.WHITE);
        }
        
        // Seleccionar nueva
        panelSeleccionado = tarjeta;
        estrategiaSeleccionada = estrategia;
        
        tarjeta.setBackground(new Color(240, 245, 250));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstilosApp.COLOR_PRIMARIO, 2),
            BorderFactory.createEmptyBorder(14, 19, 14, 19)
        ));
        actualizarColorFondo(tarjeta, new Color(240, 245, 250));
    }
    
    private void actualizarColorFondo(JPanel panel, Color color) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(color);
                actualizarColorFondo((JPanel) comp, color);
            }
        }
    }
    
    private void iniciarCurso() {
        String nombreEstrategia;

        if ("Secuencial".equals(estrategiaSeleccionada)) {
            nombreEstrategia = "Secuencial";
        } else if ("Aleatoria".equals(estrategiaSeleccionada)) {
            nombreEstrategia = "Aleatoria";
        } else {
            nombreEstrategia = "Repetición Espaciada";
        }

        app.asignarEstrategiaProgreso(progreso, nombreEstrategia);
        EstrategiaAprendizaje estrategia = app.crearEstrategia(nombreEstrategia);

        // Mostrar ventana de preparación
        mostrarVentanaPreparacion(estrategia);
    }
    
    private void mostrarVentanaPreparacion(EstrategiaAprendizaje estrategia) {
        this.dispose();
        
        JFrame ventanaPreparacion = new JFrame("Spkr - Preparado");
        ventanaPreparacion.setSize(400, 400);
        ventanaPreparacion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaPreparacion.setLocationRelativeTo(null);
        ventanaPreparacion.setResizable(false);
        EstilosApp.aplicarEstiloVentana(ventanaPreparacion);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(EstilosApp.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblIcono = new JLabel();
        ImageIcon icono = EstilosApp.getIcono(60, 60);
        if (icono != null) {
            lblIcono.setIcon(icono);
        }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblIcono);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JLabel lblTitulo = new JLabel(curso.getTitulo());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(EstilosApp.COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblEstrategia = new JLabel("Estrategia: " + estrategiaSeleccionada);
        lblEstrategia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEstrategia.setForeground(EstilosApp.COLOR_SECUNDARIO);
        lblEstrategia.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblEstrategia);
        panel.add(Box.createRigidArea(new Dimension(0, 35)));
        
        JButton btnEmpezar = new JButton("Empezar");
        btnEmpezar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEmpezar.setBackground(EstilosApp.COLOR_PRIMARIO);
        btnEmpezar.setForeground(Color.WHITE);
        btnEmpezar.setBorder(BorderFactory.createEmptyBorder(15, 60, 15, 60));
        btnEmpezar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEmpezar.setFocusPainted(false);
        btnEmpezar.setContentAreaFilled(false);
        btnEmpezar.setOpaque(true);
        btnEmpezar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnEmpezar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnEmpezar.setBackground(EstilosApp.COLOR_SECUNDARIO);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnEmpezar.setBackground(EstilosApp.COLOR_PRIMARIO);
            }
        });
        
        btnEmpezar.addActionListener(e -> {
            ventanaPreparacion.dispose();
            VentanaEjercicio ventanaEjercicio = new VentanaEjercicio(app, curso, estrategia, ventanaPrincipal, progreso);
            ventanaEjercicio.setVisible(true);
        });
        
        panel.add(btnEmpezar);
        
        ventanaPreparacion.add(panel);
        ventanaPreparacion.setVisible(true);
    }
}