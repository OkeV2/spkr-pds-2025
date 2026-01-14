package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.estrategia.EstrategiaAprendizaje;
import es.um.pds.spkr.estrategia.EstrategiaRepeticionEspaciada;
import es.um.pds.spkr.modelo.*;
import es.um.pds.spkr.util.EstilosApp;
import es.um.pds.spkr.estrategia.EstrategiaAleatoria;
import es.um.pds.spkr.estrategia.EstrategiaSecuencial;

public class VentanaEjercicio extends JFrame {
    
    private SpkrApp app;
    private Curso curso;
    private EstrategiaAprendizaje estrategia;
    private VentanaPrincipal ventanaPrincipal;
    private Progreso progreso;
    
    private List<Pregunta> todasLasPreguntas;
    private int preguntaActual;
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
    
    public VentanaEjercicio(SpkrApp app, Curso curso, EstrategiaAprendizaje estrategia, VentanaPrincipal ventanaPrincipal, Progreso progreso) {
        this.app = app;
        this.curso = curso;
        this.estrategia = estrategia;
        this.ventanaPrincipal = ventanaPrincipal;
        this.progreso = progreso;
        this.preguntaActual = progreso.getPreguntaActual();
        this.aciertos = progreso.getAciertos();
        this.errores = progreso.getErrores();
        
        cargarPreguntas();
        inicializarComponentes();
        mostrarPregunta();
    }
    
    public VentanaEjercicio(SpkrApp app, Curso curso, String nombreEstrategia, VentanaPrincipal ventanaPrincipal, Progreso progreso) {
        this.app = app;
        this.curso = curso;
        this.ventanaPrincipal = ventanaPrincipal;
        this.progreso = progreso;
        this.preguntaActual = progreso.getPreguntaActual();
        this.aciertos = progreso.getAciertos();
        this.errores = progreso.getErrores();
        
        // Crear estrategia según el nombre
        if ("Aleatoria".equals(nombreEstrategia)) {
            this.estrategia = new EstrategiaAleatoria();
        } else if ("Repetición Espaciada".equals(nombreEstrategia)) {
            this.estrategia = new EstrategiaRepeticionEspaciada();
        } else {
            this.estrategia = new EstrategiaSecuencial();
        }
        
        cargarPreguntas();
        inicializarComponentes();
        mostrarPregunta();
    }
    
    private void cargarPreguntas() {
        todasLasPreguntas = new ArrayList<>();
        for (Leccion leccion : curso.getLecciones()) {
            todasLasPreguntas.addAll(leccion.getPreguntas());
        }
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - " + curso.getTitulo());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        EstilosApp.aplicarEstiloVentana(this);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        EstilosApp.aplicarEstiloPanel(panelPrincipal);
        
        // Panel superior - Progreso
        JPanel panelSuperior = new JPanel(new BorderLayout());
        EstilosApp.aplicarEstiloPanel(panelSuperior);
        
        lblProgreso = new JLabel();
        lblProgreso.setFont(EstilosApp.FUENTE_NORMAL);
        lblProgreso.setForeground(EstilosApp.COLOR_SECUNDARIO);
        panelSuperior.add(lblProgreso, BorderLayout.WEST);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        EstilosApp.aplicarEstiloPanel(panelCentral);
        
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
        
        // Panel inferior - Botones
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
        preguntaActualObj = estrategia.siguientePregunta(todasLasPreguntas, preguntaActual);
        
        if (preguntaActualObj == null) {
            mostrarResultadoFinal();
            return;
        }
        
        lblProgreso.setText("Pregunta " + (preguntaActual + 1) + " de " + todasLasPreguntas.size() + 
                           "  |  Aciertos: " + aciertos + "  |  Errores: " + errores);
        lblEnunciado.setText(preguntaActualObj.getEnunciado());
        lblResultado.setText(" ");
        
        panelRespuesta.removeAll();
        
        if (preguntaActualObj instanceof PreguntaTest) {
            mostrarPreguntaTest((PreguntaTest) preguntaActualObj);
        } else if (preguntaActualObj instanceof PreguntaTraduccion) {
            mostrarPreguntaTraduccion();
        } else if (preguntaActualObj instanceof PreguntaHuecos) {
            mostrarPreguntaHuecos((PreguntaHuecos) preguntaActualObj);
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
            JPanel panelOpcion = new JPanel(new BorderLayout());
            panelOpcion.setBackground(Color.WHITE);
            panelOpcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
            ));
            panelOpcion.setMaximumSize(new Dimension(450, 55));
            panelOpcion.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JRadioButton rb = new JRadioButton(opcion);
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            rb.setBackground(Color.WHITE);
            rb.setFocusPainted(false);
            rb.setActionCommand(opcion);
            
            grupoOpciones.add(rb);
            opcionesTest.add(rb);
            
            panelOpcion.add(rb, BorderLayout.CENTER);
            panelRespuesta.add(panelOpcion);
            panelRespuesta.add(Box.createRigidArea(new Dimension(0, 8)));
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
    
    private void mostrarPreguntaHuecos(PreguntaHuecos pregunta) {
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
            lblResultado.setText("¡Correcto!");
            lblResultado.setForeground(EstilosApp.COLOR_EXITO);
            aciertos++;
            progreso.registrarAcierto();
            
            if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                ((EstrategiaRepeticionEspaciada) estrategia).registrarAcierto(preguntaActualObj);
            }
        } else {
            String respuestaCorrecta = obtenerRespuestaCorrecta();
            lblResultado.setText("Incorrecto. La respuesta era: " + respuestaCorrecta);
            lblResultado.setForeground(EstilosApp.COLOR_ERROR);
            errores++;
            progreso.registrarError();
            
            if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                ((EstrategiaRepeticionEspaciada) estrategia).registrarFallo(preguntaActualObj);
            }
            
            registrarError();
        }
        
        app.getUsuarioActual().getEstadisticas().incrementarEjercicios();
        app.guardarProgreso();
        
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
    
    private void registrarError() {
        List<ErrorFrecuente> erroresFrecuentes = app.getUsuarioActual().getErroresFrecuentes();
        
        for (ErrorFrecuente ef : erroresFrecuentes) {
            if (ef.getPregunta().equals(preguntaActualObj)) {
                ef.incrementarErrores();
                return;
            }
        }
        
        ErrorFrecuente nuevoError = new ErrorFrecuente(preguntaActualObj);
        app.getUsuarioActual().addErrorFrecuente(nuevoError);
    }
    
    private void siguientePregunta() {
        preguntaActual++;
        mostrarPregunta();
    }
    
    private void mostrarResultadoFinal() {
        int porcentaje = 0;
        if (aciertos + errores > 0) {
            porcentaje = (aciertos * 100) / (aciertos + errores);
        }
        
        String mensaje = "¡Curso completado!\n\n" +
                        "Aciertos: " + aciertos + "\n" +
                        "Errores: " + errores + "\n" +
                        "Porcentaje: " + porcentaje + "%";
        
        JOptionPane.showMessageDialog(this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        app.guardarProgreso();
        this.dispose();
    }
    
    private void salir() {
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Deseas salir? Tu progreso se guardará.", 
            "Salir", 
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            app.guardarProgreso();
            this.dispose();
        }
    }
}