package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.modelo.*;

public class VentanaRepaso extends JFrame {
    
    private SpkrApp app;
    private VentanaPrincipal ventanaPrincipal;
    
    private List<ErrorFrecuente> erroresPendientes;
    private int indiceActual;
    private int aciertos;
    private int errores;
    
    private JLabel lblProgreso;
    private JLabel lblEnunciado;
    private JPanel panelRespuesta;
    private JButton btnComprobar;
    private JButton btnSiguiente;
    private JButton btnSalir;
    private JLabel lblResultado;
    
    private JTextField txtRespuesta;
    private ButtonGroup grupoOpciones;
    private List<JRadioButton> opcionesTest;
    
    private Pregunta preguntaActualObj;
    private ErrorFrecuente errorActual;
    
    public VentanaRepaso(SpkrApp app, VentanaPrincipal ventanaPrincipal) {
        this.app = app;
        this.ventanaPrincipal = ventanaPrincipal;
        this.indiceActual = 0;
        this.aciertos = 0;
        this.errores = 0;
        
        cargarErrores();
        inicializarComponentes();
        mostrarPregunta();
    }
    
    private void cargarErrores() {
        erroresPendientes = new ArrayList<>(app.getUsuarioActual().getErroresFrecuentes());
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Repaso de Errores");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior
        lblProgreso = new JLabel();
        lblProgreso.setFont(new Font("Arial", Font.BOLD, 14));
        panelPrincipal.add(lblProgreso, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        
        lblEnunciado = new JLabel();
        lblEnunciado.setFont(new Font("Arial", Font.PLAIN, 18));
        lblEnunciado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblEnunciado);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panelRespuesta = new JPanel();
        panelRespuesta.setLayout(new BoxLayout(panelRespuesta, BoxLayout.Y_AXIS));
        panelRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(panelRespuesta);
        
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        
        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 16));
        lblResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblResultado);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnComprobar = new JButton("Comprobar");
        btnSiguiente = new JButton("Siguiente");
        btnSalir = new JButton("Salir");
        
        btnSiguiente.setEnabled(false);
        
        panelBotones.add(btnComprobar);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnSalir);
        
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Eventos
        btnComprobar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comprobarRespuesta();
            }
        });
        
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siguientePregunta();
            }
        });
        
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salir();
            }
        });
    }
    
    private void mostrarPregunta() {
        if (indiceActual >= erroresPendientes.size()) {
            mostrarResultadoFinal();
            return;
        }
        
        errorActual = erroresPendientes.get(indiceActual);
        preguntaActualObj = errorActual.getPregunta();
        
        lblProgreso.setText("Repaso " + (indiceActual + 1) + " de " + erroresPendientes.size() +
                           " | Aciertos: " + aciertos + " | Errores: " + errores);
        lblEnunciado.setText(preguntaActualObj.getEnunciado());
        lblResultado.setText(" ");
        
        panelRespuesta.removeAll();
        
        if (preguntaActualObj instanceof PreguntaTest) {
            mostrarPreguntaTest((PreguntaTest) preguntaActualObj);
        } else if (preguntaActualObj instanceof PreguntaTraduccion) {
            mostrarPreguntaTraduccion();
        } else if (preguntaActualObj instanceof PreguntaHuecos) {
            mostrarPreguntaHuecos();
        }
        
        panelRespuesta.revalidate();
        panelRespuesta.repaint();
        
        btnComprobar.setEnabled(true);
        btnSiguiente.setEnabled(false);
    }
    
    private void mostrarPreguntaTest(PreguntaTest pregunta) {
        grupoOpciones = new ButtonGroup();
        opcionesTest = new ArrayList<>();
        
        List<String> opciones = new ArrayList<>();
        opciones.add(pregunta.getOpcionCorrecta());
        opciones.add(pregunta.getOpcionIncorrecta1());
        opciones.add(pregunta.getOpcionIncorrecta2());
        Collections.shuffle(opciones);
        
        for (String opcion : opciones) {
            JRadioButton rb = new JRadioButton(opcion);
            rb.setFont(new Font("Arial", Font.PLAIN, 14));
            rb.setActionCommand(opcion);
            grupoOpciones.add(rb);
            opcionesTest.add(rb);
            panelRespuesta.add(rb);
            panelRespuesta.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }
    
    private void mostrarPreguntaTraduccion() {
        JLabel lblInstruccion = new JLabel("Escribe la traducción:");
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 14));
        panelRespuesta.add(lblInstruccion);
        panelRespuesta.add(Box.createRigidArea(new Dimension(0, 10)));
        
        txtRespuesta = new JTextField(20);
        txtRespuesta.setFont(new Font("Arial", Font.PLAIN, 14));
        txtRespuesta.setMaximumSize(new Dimension(300, 30));
        panelRespuesta.add(txtRespuesta);
    }
    
    private void mostrarPreguntaHuecos() {
        JLabel lblInstruccion = new JLabel("Completa la palabra que falta:");
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 14));
        panelRespuesta.add(lblInstruccion);
        panelRespuesta.add(Box.createRigidArea(new Dimension(0, 10)));
        
        txtRespuesta = new JTextField(20);
        txtRespuesta.setFont(new Font("Arial", Font.PLAIN, 14));
        txtRespuesta.setMaximumSize(new Dimension(300, 30));
        panelRespuesta.add(txtRespuesta);
    }
    
    private void comprobarRespuesta() {
        String respuesta = obtenerRespuesta();
        
        if (respuesta == null || respuesta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una respuesta");
            return;
        }
        
        boolean correcta = preguntaActualObj.validarRespuesta(respuesta);
        
        if (correcta) {
            lblResultado.setText("¡Correcto!");
            lblResultado.setForeground(new Color(0, 150, 0));
            aciertos++;
            
            errorActual.reducirErrores();
            if (errorActual.estaDominada()) {
                app.getUsuarioActual().removeErrorFrecuente(errorActual);
            }
        } else {
            String respuestaCorrecta = obtenerRespuestaCorrecta();
            lblResultado.setText("Incorrecto. La respuesta era: " + respuestaCorrecta);
            lblResultado.setForeground(Color.RED);
            errores++;
        }
        
        btnComprobar.setEnabled(false);
        btnSiguiente.setEnabled(true);
    }
    
    private String obtenerRespuesta() {
        if (preguntaActualObj instanceof PreguntaTest) {
            ButtonModel seleccion = grupoOpciones.getSelection();
            if (seleccion != null) {
                return seleccion.getActionCommand();
            }
            return null;
        } else {
            return txtRespuesta.getText().trim();
        }
    }
    
    private String obtenerRespuestaCorrecta() {
        if (preguntaActualObj instanceof PreguntaTest) {
            return ((PreguntaTest) preguntaActualObj).getOpcionCorrecta();
        } else if (preguntaActualObj instanceof PreguntaTraduccion) {
            return ((PreguntaTraduccion) preguntaActualObj).getRespuestaCorrecta();
        } else if (preguntaActualObj instanceof PreguntaHuecos) {
            return ((PreguntaHuecos) preguntaActualObj).getPalabraOculta();
        }
        return "";
    }
    
    private void siguientePregunta() {
        indiceActual++;
        mostrarPregunta();
    }
    
    private void mostrarResultadoFinal() {
        JOptionPane.showMessageDialog(this,
            "¡Repaso completado!\n\n" +
            "Aciertos: " + aciertos + "\n" +
            "Errores: " + errores
        );
        
        this.dispose();
    }
    
    private void salir() {
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Deseas salir del repaso?",
            "Salir",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }
}