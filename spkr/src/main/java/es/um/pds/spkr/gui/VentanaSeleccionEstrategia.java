package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.estrategia.EstrategiaAleatoria;
import es.um.pds.spkr.estrategia.EstrategiaAprendizaje;
import es.um.pds.spkr.estrategia.EstrategiaRepeticionEspaciada;
import es.um.pds.spkr.estrategia.EstrategiaSecuencial;
import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.modelo.Progreso;
import es.um.pds.spkr.util.EstilosApp;

public class VentanaSeleccionEstrategia extends JFrame {
    
    private SpkrApp app;
    private Curso curso;
    private VentanaPrincipal ventanaPrincipal;
    private Progreso progreso;
    private JRadioButton rbSecuencial;
    private JRadioButton rbAleatoria;
    private JRadioButton rbRepeticionEspaciada;
    private JButton btnIniciar;
    private JButton btnCancelar;
    
    public VentanaSeleccionEstrategia(SpkrApp app, Curso curso, VentanaPrincipal ventanaPrincipal, Progreso progreso) {
        this.app = app;
        this.curso = curso;
        this.ventanaPrincipal = ventanaPrincipal;
        this.progreso = progreso;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Seleccionar Estrategia");
        setSize(400, 400);
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
        ImageIcon icono = EstilosApp.getIcono(60, 60);
        if (icono != null) {
            lblIcono.setIcon(icono);
        }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblIcono);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Título
        JLabel lblTitulo = EstilosApp.crearSubtitulo("¿Cómo quieres aprender?");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Opciones
        rbSecuencial = new JRadioButton("Secuencial - Preguntas en orden");
        rbAleatoria = new JRadioButton("Aleatoria - Preguntas al azar");
        rbRepeticionEspaciada = new JRadioButton("Repetición Espaciada - Repite los fallos");
        
        rbSecuencial.setFont(EstilosApp.FUENTE_NORMAL);
        rbAleatoria.setFont(EstilosApp.FUENTE_NORMAL);
        rbRepeticionEspaciada.setFont(EstilosApp.FUENTE_NORMAL);
        
        rbSecuencial.setBackground(EstilosApp.COLOR_FONDO);
        rbAleatoria.setBackground(EstilosApp.COLOR_FONDO);
        rbRepeticionEspaciada.setBackground(EstilosApp.COLOR_FONDO);
        
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbSecuencial);
        grupo.add(rbAleatoria);
        grupo.add(rbRepeticionEspaciada);
        
        rbSecuencial.setSelected(true);
        
        rbSecuencial.setAlignmentX(Component.CENTER_ALIGNMENT);
        rbAleatoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        rbRepeticionEspaciada.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(rbSecuencial);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(rbAleatoria);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(rbRepeticionEspaciada);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Botones
        btnIniciar = new JButton("Iniciar");
        btnCancelar = new JButton("Cancelar");
        
        EstilosApp.aplicarEstiloBoton(btnIniciar);
        EstilosApp.aplicarEstiloBotonSecundario(btnCancelar);
        
        btnIniciar.setMaximumSize(new Dimension(200, 40));
        btnCancelar.setMaximumSize(new Dimension(200, 40));
        
        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(btnIniciar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnCancelar);
        
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
    
    private void iniciarCurso() {
        EstrategiaAprendizaje estrategia;
        String nombreEstrategia;
        
        if (rbSecuencial.isSelected()) {
            estrategia = new EstrategiaSecuencial();
            nombreEstrategia = "Secuencial";
        } else if (rbAleatoria.isSelected()) {
            estrategia = new EstrategiaAleatoria();
            nombreEstrategia = "Aleatoria";
        } else {
            estrategia = new EstrategiaRepeticionEspaciada();
            nombreEstrategia = "Repetición Espaciada";
        }
        
        progreso.setEstrategia(nombreEstrategia);
        app.guardarProgreso();
        
        VentanaEjercicio ventanaEjercicio = new VentanaEjercicio(app, curso, estrategia, ventanaPrincipal, progreso);
        ventanaEjercicio.setVisible(true);
        this.dispose();
    }
}