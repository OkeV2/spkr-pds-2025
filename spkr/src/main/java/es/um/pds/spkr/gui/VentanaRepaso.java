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
import es.um.pds.spkr.util.EstilosApp;

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
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        EstilosApp.aplicarEstiloVentana(this);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        EstilosApp.aplicarEstiloPanel(panelPrincipal);
        
        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        EstilosApp.aplicarEstiloPanel(panelSuperior);
        
        JLabel lblTitulo = EstilosApp.crearSubtitulo("Repaso de Errores");
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        lblProgreso = new JLabel();
        lblProgreso.setFont(EstilosApp.FUENTE_NORMAL);
        lblProgreso.setForeground(EstilosApp.COLOR_SECUNDARIO);
        panelSuperior.add(lblProgreso, BorderLayout.EAST);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        EstilosApp.aplicarEstiloPanel(panelCentral);
        
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        
        lblEnunciado = new JLabel();
        lblEnunciado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEnunciado.setForeground(EstilosApp.COLOR_TEXTO);
        lblEnunciado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblEnunciado);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 25)));
        
        panelRespuesta = new JPanel();
        panelRespuesta.setLayout(new BoxLayout(panelRespuesta, BoxLayout.Y_AXIS));
        panelRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
        EstilosApp.aplicarEstiloPanel(panelRespuesta);
        panelCentral.add(panelRespuesta);
        
        panelCentral.add(Box.createRigidArea(new Dimension(0, 25)));
        
        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblResultado);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        EstilosApp.aplicarEstiloPanel(panelBotones);
        
        btnComprobar = new JButton("Comprobar");
        btnSiguiente = new JButton("Siguiente");
        btnSalir = new JButton("Salir");
        
        EstilosApp.aplicarEstiloBoton(btnComprobar);
        EstilosApp.aplicarEstiloBoton(btnSiguiente);
        EstilosApp.aplicarEstiloBotonSecundario(btnSalir);
        
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
        
        lblProgreso.setText((indiceActual + 1) + " / " + erroresPendientes.size());
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
            rb.setFont(EstilosApp.FUENTE_NORMAL);
            rb.setBackground(EstilosApp.COLOR_FONDO);
            rb.setActionCommand(opcion);
            grupoOpciones.add(rb);
            opcionesTest.add(rb);
            panelRespuesta.add(rb);
            panelRespuesta.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
    
    private void mostrarPreguntaTraduccion() {
        JLabel lblInstruccion = EstilosApp.crearEtiqueta("Escribe la traducción:");
        panelRespuesta.add(lblInstruccion);
        panelRespuesta.add(Box.createRigidArea(new Dimension(0, 10)));
        
        txtRespuesta = new JTextField();
        EstilosApp.aplicarEstiloCampoTexto(txtRespuesta);
        txtRespuesta.setMaximumSize(new Dimension(350, 40));
        panelRespuesta.add(txtRespuesta);
    }
    
    private void mostrarPreguntaHuecos() {
        JLabel lblInstruccion = EstilosApp.crearEtiqueta("Completa la palabra que falta:");
        panelRespuesta.add(lblInstruccion);
        panelRespuesta.add(Box.createRigidArea(new Dimension(0, 10)));
        
        txtRespuesta = new JTextField();
        EstilosApp.aplicarEstiloCampoTexto(txtRespuesta);
        txtRespuesta.setMaximumSize(new Dimension(350, 40));
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
            lblResultado.setText("¡Correcto! Has dominado este error.");
            lblResultado.setForeground(EstilosApp.COLOR_EXITO);
            aciertos++;
            
            errorActual.reducirErrores();
            if (errorActual.estaDominada()) {
                app.getUsuarioActual().removeErrorFrecuente(errorActual);
            }
        } else {
            String respuestaCorrecta = obtenerRespuestaCorrecta();
            lblResultado.setText("Incorrecto. La respuesta era: " + respuestaCorrecta);
            lblResultado.setForeground(EstilosApp.COLOR_ERROR);
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
        String mensaje = "¡Repaso completado!\n\n" +
                        "Aciertos: " + aciertos + "\n" +
                        "Errores: " + errores;
        
        JOptionPane.showMessageDialog(this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        app.guardarProgreso();
        this.dispose();
    }
    
    private void salir() {
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Deseas salir del repaso?",
            "Salir",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            app.guardarProgreso();
            this.dispose();
        }
    }
}