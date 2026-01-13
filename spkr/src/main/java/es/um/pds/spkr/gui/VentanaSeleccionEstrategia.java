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

public class VentanaSeleccionEstrategia extends JFrame {
    
    private SpkrApp app;
    private Curso curso;
    private VentanaPrincipal ventanaPrincipal;
    private JRadioButton rbSecuencial;
    private JRadioButton rbAleatoria;
    private JRadioButton rbRepeticionEspaciada;
    private JButton btnIniciar;
    private JButton btnCancelar;
    
    public VentanaSeleccionEstrategia(SpkrApp app, Curso curso, VentanaPrincipal ventanaPrincipal) {
        this.app = app;
        this.curso = curso;
        this.ventanaPrincipal = ventanaPrincipal;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Seleccionar Estrategia de Aprendizaje");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel lblTitulo = new JLabel("¿Cómo quieres aprender?");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        // Opciones
        rbSecuencial = new JRadioButton("Secuencial - Preguntas en orden");
        rbAleatoria = new JRadioButton("Aleatoria - Preguntas al azar");
        rbRepeticionEspaciada = new JRadioButton("Repetición Espaciada - Repite los fallos");
        
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbSecuencial);
        grupo.add(rbAleatoria);
        grupo.add(rbRepeticionEspaciada);
        
        rbSecuencial.setSelected(true);
        
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(rbSecuencial, gbc);
        
        gbc.gridy = 2;
        panel.add(rbAleatoria, gbc);
        
        gbc.gridy = 3;
        panel.add(rbRepeticionEspaciada, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel();
        btnIniciar = new JButton("Iniciar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnIniciar);
        panelBotones.add(btnCancelar);
        
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);
        
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
        
        if (rbSecuencial.isSelected()) {
            estrategia = new EstrategiaSecuencial();
        } else if (rbAleatoria.isSelected()) {
            estrategia = new EstrategiaAleatoria();
        } else {
            estrategia = new EstrategiaRepeticionEspaciada();
        }
        
        VentanaEjercicio ventanaEjercicio = new VentanaEjercicio(app, curso, estrategia, ventanaPrincipal);
        ventanaEjercicio.setVisible(true);
        this.dispose();
    }
}