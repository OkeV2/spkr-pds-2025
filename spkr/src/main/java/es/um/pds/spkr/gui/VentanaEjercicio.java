package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.estrategia.EstrategiaAleatoria;
import es.um.pds.spkr.estrategia.EstrategiaAprendizaje;
import es.um.pds.spkr.estrategia.EstrategiaRepeticionEspaciada;
import es.um.pds.spkr.estrategia.EstrategiaSecuencial;
import es.um.pds.spkr.modelo.*;
import es.um.pds.spkr.util.EstilosApp;

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
    
    private JLabel lblPreguntaNum;
    private JProgressBar barraProgreso;
    private JLabel lblEnunciado;
    private JPanel panelRespuesta;
    private JButton btnComprobar;
    private JButton btnSiguiente;
    private JButton btnSalir;
    private JPanel panelResultado;
    private JLabel lblResultado;
    private JLabel lblRespuestaCorrecta;
    
    private JTextField txtRespuesta;
    private ButtonGroup grupoOpciones;
    private List<JRadioButton> opcionesTest;
    private List<JPanel> panelesOpciones;
    
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
        setSize(650, 580);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        EstilosApp.aplicarEstiloVentana(this);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(EstilosApp.COLOR_FONDO);
        
        // Panel superior con progreso
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Fila superior: número de pregunta y botón salir
        JPanel filaTop = new JPanel(new BorderLayout());
        filaTop.setBackground(Color.WHITE);
        filaTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        lblPreguntaNum = new JLabel();
        lblPreguntaNum.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPreguntaNum.setForeground(EstilosApp.COLOR_SECUNDARIO);
        filaTop.add(lblPreguntaNum, BorderLayout.WEST);
        
        btnSalir = new JButton("x Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSalir.setForeground(new Color(150, 150, 150));
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setFocusPainted(false);
        btnSalir.setContentAreaFilled(false);
        filaTop.add(btnSalir, BorderLayout.EAST);
        
        panelSuperior.add(filaTop);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Barra de progreso
        barraProgreso = new JProgressBar(0, todasLasPreguntas.size());
        barraProgreso.setValue(preguntaActual);
        barraProgreso.setPreferredSize(new Dimension(0, 8));
        barraProgreso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        barraProgreso.setBorderPainted(false);
        barraProgreso.setBackground(new Color(230, 230, 230));
        barraProgreso.setForeground(EstilosApp.COLOR_PRIMARIO);
        panelSuperior.add(barraProgreso);
        
        // Estadísticas
        JPanel panelStats = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelStats.setBackground(Color.WHITE);
        panelStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel lblAciertos = new JLabel("Aciertos: " + aciertos);
        lblAciertos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAciertos.setForeground(EstilosApp.COLOR_EXITO);
        
        JLabel lblErrores = new JLabel("Errores: " + errores);
        lblErrores.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblErrores.setForeground(EstilosApp.COLOR_ERROR);
        
        panelStats.add(lblAciertos);
        panelStats.add(lblErrores);
        
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSuperior.add(panelStats);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(EstilosApp.COLOR_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        
        // Enunciado
        lblEnunciado = new JLabel();
        lblEnunciado.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblEnunciado.setForeground(EstilosApp.COLOR_TEXTO);
        lblEnunciado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblEnunciado);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Panel de respuestas
        panelRespuesta = new JPanel();
        panelRespuesta.setLayout(new BoxLayout(panelRespuesta, BoxLayout.Y_AXIS));
        panelRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelRespuesta.setBackground(EstilosApp.COLOR_FONDO);
        panelCentral.add(panelRespuesta);
        
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel de resultado (inicialmente oculto)
        panelResultado = new JPanel();
        panelResultado.setLayout(new BoxLayout(panelResultado, BoxLayout.Y_AXIS));
        panelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelResultado.setBackground(EstilosApp.COLOR_FONDO);
        panelResultado.setVisible(false);
        
        lblResultado = new JLabel();
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblRespuestaCorrecta = new JLabel();
        lblRespuestaCorrecta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRespuestaCorrecta.setForeground(EstilosApp.COLOR_SECUNDARIO);
        lblRespuestaCorrecta.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelResultado.add(lblResultado);
        panelResultado.add(Box.createRigidArea(new Dimension(0, 5)));
        panelResultado.add(lblRespuestaCorrecta);
        
        panelCentral.add(panelResultado);
        panelCentral.add(Box.createVerticalGlue());
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelInferior.setBackground(EstilosApp.COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 30, 25, 30));
        
        btnComprobar = new JButton("Comprobar");
        btnSiguiente = new JButton("Siguiente");
        
        estilizarBotonPrincipal(btnComprobar);
        estilizarBotonSecundario(btnSiguiente);
        
        btnSiguiente.setEnabled(false);
        
        panelInferior.add(btnComprobar);
        panelInferior.add(btnSiguiente);
        
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Eventos
        btnComprobar.addActionListener(e -> comprobarRespuesta());
        btnSiguiente.addActionListener(e -> siguientePregunta());
        btnSalir.addActionListener(e -> salir());
    }
    
    private void estilizarBotonPrincipal(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(EstilosApp.COLOR_PRIMARIO);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(EstilosApp.COLOR_SECUNDARIO);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(EstilosApp.COLOR_PRIMARIO);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(new Color(50, 50, 50));
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(EstilosApp.COLOR_SECUNDARIO);
            }
        });
    }
    
    private void estilizarBotonSecundario(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(Color.WHITE);
        boton.setForeground(EstilosApp.COLOR_PRIMARIO);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstilosApp.COLOR_PRIMARIO, 2),
            BorderFactory.createEmptyBorder(10, 38, 10, 38)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(Color.WHITE);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(new Color(230, 230, 230));
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(new Color(245, 245, 245));
            }
        });
    }
    
    private void mostrarPregunta() {
        preguntaActualObj = estrategia.siguientePregunta(todasLasPreguntas, preguntaActual);
        
        if (preguntaActualObj == null) {
            mostrarResultadoFinal();
            return;
        }
        
        // Actualizar UI
        lblPreguntaNum.setText("Pregunta " + (preguntaActual + 1) + " de " + todasLasPreguntas.size());
        barraProgreso.setValue(preguntaActual);
        lblEnunciado.setText(preguntaActualObj.getEnunciado());
        
        panelResultado.setVisible(false);
        
        panelRespuesta.removeAll();
        panelesOpciones = new ArrayList<>();
        
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
        btnComprobar.setBackground(EstilosApp.COLOR_PRIMARIO);
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
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            panelOpcion.setMaximumSize(new Dimension(500, 60));
            panelOpcion.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JRadioButton rb = new JRadioButton(opcion);
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            rb.setBackground(Color.WHITE);
            rb.setFocusPainted(false);
            rb.setActionCommand(opcion);
            
            grupoOpciones.add(rb);
            opcionesTest.add(rb);
            panelesOpciones.add(panelOpcion);
            
            // Evento para seleccionar al hacer clic en el panel
            panelOpcion.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    rb.setSelected(true);
                    actualizarSeleccionOpciones();
                }
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!rb.isSelected()) {
                        panelOpcion.setBackground(new Color(250, 250, 252));
                        rb.setBackground(new Color(250, 250, 252));
                    }
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!rb.isSelected()) {
                        panelOpcion.setBackground(Color.WHITE);
                        rb.setBackground(Color.WHITE);
                    }
                }
            });
            
            rb.addActionListener(e -> actualizarSeleccionOpciones());
            
            panelOpcion.add(rb, BorderLayout.CENTER);
            panelRespuesta.add(panelOpcion);
            panelRespuesta.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
    
    private void actualizarSeleccionOpciones() {
        for (int i = 0; i < opcionesTest.size(); i++) {
            JRadioButton rb = opcionesTest.get(i);
            JPanel panel = panelesOpciones.get(i);
            
            if (rb.isSelected()) {
                panel.setBackground(new Color(240, 245, 255));
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(EstilosApp.COLOR_PRIMARIO, 2),
                    BorderFactory.createEmptyBorder(14, 19, 14, 19)
                ));
                rb.setBackground(new Color(240, 245, 255));
            } else {
                panel.setBackground(Color.WHITE);
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
                rb.setBackground(Color.WHITE);
            }
        }
    }
    
    private void mostrarPreguntaTraduccion() {
        JLabel lblInstruccion = new JLabel("Escribe la traducción:");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setForeground(EstilosApp.COLOR_SECUNDARIO);
        lblInstruccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelRespuesta.add(lblInstruccion);
        panelRespuesta.add(Box.createRigidArea(new Dimension(0, 12)));
        
        txtRespuesta = new JTextField();
        txtRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtRespuesta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtRespuesta.setMaximumSize(new Dimension(400, 50));
        txtRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelRespuesta.add(txtRespuesta);
    }
    
    private void mostrarPreguntaHuecos(PreguntaHuecos pregunta) {
        JLabel lblInstruccion = new JLabel("Completa la palabra que falta:");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setForeground(EstilosApp.COLOR_SECUNDARIO);
        lblInstruccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelRespuesta.add(lblInstruccion);
        panelRespuesta.add(Box.createRigidArea(new Dimension(0, 12)));
        
        txtRespuesta = new JTextField();
        txtRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtRespuesta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtRespuesta.setMaximumSize(new Dimension(400, 50));
        txtRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelRespuesta.add(txtRespuesta);
    }
    
    private void comprobarRespuesta() {
        String respuesta = obtenerRespuesta();
        
        if (respuesta == null || respuesta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una respuesta");
            return;
        }
        
        boolean correcta = preguntaActualObj.validarRespuesta(respuesta);
        
        panelResultado.setVisible(true);
        
        if (correcta) {
            lblResultado.setText("¡Correcto!");
            lblResultado.setForeground(EstilosApp.COLOR_EXITO);
            lblRespuestaCorrecta.setText("");
            aciertos++;
            progreso.registrarAcierto();
            
            if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                ((EstrategiaRepeticionEspaciada) estrategia).registrarAcierto(preguntaActualObj);
            }
            
            // Marcar opción correcta en verde
            if (preguntaActualObj instanceof PreguntaTest) {
                marcarOpcionCorrecta();
            }
        } else {
            String respuestaCorrectaStr = obtenerRespuestaCorrecta();
            lblResultado.setText("Incorrecto");
            lblResultado.setForeground(EstilosApp.COLOR_ERROR);
            lblRespuestaCorrecta.setText("La respuesta correcta era: " + respuestaCorrectaStr);
            errores++;
            progreso.registrarError();
            
            if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                ((EstrategiaRepeticionEspaciada) estrategia).registrarFallo(preguntaActualObj);
            }
            
            registrarError();
            
            // Marcar opciones en rojo/verde
            if (preguntaActualObj instanceof PreguntaTest) {
                marcarOpcionIncorrecta();
            }
        }
        
        app.getUsuarioActual().getEstadisticas().incrementarEjercicios();
        app.guardarProgreso();
        
        btnComprobar.setEnabled(false);
        btnComprobar.setBackground(new Color(180, 180, 180));
        btnSiguiente.setEnabled(true);
    }
    
    private void marcarOpcionCorrecta() {
        for (int i = 0; i < opcionesTest.size(); i++) {
            JRadioButton rb = opcionesTest.get(i);
            JPanel panel = panelesOpciones.get(i);
            
            if (rb.isSelected()) {
                panel.setBackground(new Color(232, 245, 233));
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(EstilosApp.COLOR_EXITO, 2),
                    BorderFactory.createEmptyBorder(14, 19, 14, 19)
                ));
                rb.setBackground(new Color(232, 245, 233));
            }
        }
    }
    
    private void marcarOpcionIncorrecta() {
        String correcta = ((PreguntaTest) preguntaActualObj).getOpcionCorrecta();
        
        for (int i = 0; i < opcionesTest.size(); i++) {
            JRadioButton rb = opcionesTest.get(i);
            JPanel panel = panelesOpciones.get(i);
            
            if (rb.isSelected()) {
                panel.setBackground(new Color(255, 235, 238));
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(EstilosApp.COLOR_ERROR, 2),
                    BorderFactory.createEmptyBorder(14, 19, 14, 19)
                ));
                rb.setBackground(new Color(255, 235, 238));
            }
            
            if (rb.getActionCommand().equals(correcta)) {
                panel.setBackground(new Color(232, 245, 233));
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(EstilosApp.COLOR_EXITO, 2),
                    BorderFactory.createEmptyBorder(14, 19, 14, 19)
                ));
                rb.setBackground(new Color(232, 245, 233));
            }
        }
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