package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    
    // Temporizador
    private Timer temporizador;
    private int segundosTotales;
    private boolean pausado;
    
    private JLabel lblPreguntaNum;
    private JLabel lblTemporizador;
    private JProgressBar barraProgreso;
    private JLabel lblEnunciado;
    private JPanel panelRespuesta;
    private JButton btnSiguiente;
    private JButton btnPausar;
    private JButton btnSalir;
    private JPanel panelResultado;
    private JLabel lblResultado;
    private JLabel lblRespuestaCorrecta;
    private JLabel lblAciertosContador;
    private JLabel lblErroresContador;
    
    private JTextField txtRespuesta;
    private ButtonGroup grupoOpciones;
    private List<JRadioButton> opcionesTest;
    private List<JPanel> panelesOpciones;
    
    private Pregunta preguntaActualObj;
    private boolean preguntaRespondida;
    
    public VentanaEjercicio(SpkrApp app, Curso curso, EstrategiaAprendizaje estrategia, VentanaPrincipal ventanaPrincipal, Progreso progreso) {
        this.app = app;
        this.curso = curso;
        this.estrategia = estrategia;
        this.ventanaPrincipal = ventanaPrincipal;
        this.progreso = progreso;
        this.preguntaActual = progreso.getPreguntaActual();
        this.aciertos = progreso.getAciertos();
        this.errores = progreso.getErrores();
        this.segundosTotales = progreso.getTiempoSegundos();
        this.pausado = false;
        this.preguntaRespondida = false;
        
        cargarPreguntas();
        inicializarComponentes();
        iniciarTemporizador();
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
        this.segundosTotales = progreso.getTiempoSegundos();
        this.pausado = false;
        this.preguntaRespondida = false;
        
        if ("Aleatoria".equals(nombreEstrategia)) {
            this.estrategia = new EstrategiaAleatoria();
        } else if ("Repetición Espaciada".equals(nombreEstrategia)) {
            this.estrategia = new EstrategiaRepeticionEspaciada();
        } else {
            this.estrategia = new EstrategiaSecuencial();
        }
        
        cargarPreguntas();
        inicializarComponentes();
        iniciarTemporizador();
        mostrarPregunta();
    }
    
    private void cargarPreguntas() {
        todasLasPreguntas = new ArrayList<>();
        for (Leccion leccion : curso.getLecciones()) {
            todasLasPreguntas.addAll(leccion.getPreguntas());
        }
    }
    
    private void iniciarTemporizador() {
        temporizador = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pausado) {
                    segundosTotales++;
                    actualizarLabelTemporizador();
                }
            }
        });
        temporizador.start();
    }
    
    private void actualizarLabelTemporizador() {
        int horas = segundosTotales / 3600;
        int minutos = (segundosTotales % 3600) / 60;
        int segundos = segundosTotales % 60;
        
        if (horas > 0) {
            lblTemporizador.setText(String.format("%d:%02d:%02d", horas, minutos, segundos));
        } else {
            lblTemporizador.setText(String.format("%02d:%02d", minutos, segundos));
        }
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - " + curso.getTitulo());
        setSize(650, 620);
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
        
        // Fila superior: número de pregunta, temporizador y botón salir
        JPanel filaTop = new JPanel(new BorderLayout());
        filaTop.setBackground(Color.WHITE);
        filaTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        lblPreguntaNum = new JLabel();
        lblPreguntaNum.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPreguntaNum.setForeground(EstilosApp.COLOR_SECUNDARIO);
        filaTop.add(lblPreguntaNum, BorderLayout.WEST);
        
        // Panel central con temporizador
        JPanel panelTemporizador = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelTemporizador.setBackground(Color.WHITE);
        
        lblTemporizador = new JLabel("00:00");
        lblTemporizador.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTemporizador.setForeground(EstilosApp.COLOR_PRIMARIO);
        panelTemporizador.add(lblTemporizador);
        
        filaTop.add(panelTemporizador, BorderLayout.CENTER);
        
        // Panel derecho con botones pausar y salir
        JPanel panelBotonesSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBotonesSuperior.setBackground(Color.WHITE);
        
        btnPausar = new JButton("Pausar");
        btnPausar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPausar.setForeground(Color.WHITE);
        btnPausar.setBackground(new Color(255, 152, 0));
        btnPausar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnPausar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPausar.setFocusPainted(false);
        btnPausar.setContentAreaFilled(false);
        btnPausar.setOpaque(true);
        
        btnPausar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!pausado) {
                    btnPausar.setBackground(new Color(255, 167, 38));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!pausado) {
                    btnPausar.setBackground(new Color(255, 152, 0));
                }
            }
        });
        
        btnSalir = new JButton("Guardar y Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setBackground(EstilosApp.COLOR_SECUNDARIO);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setFocusPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setOpaque(true);
        
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnSalir.setBackground(new Color(100, 100, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnSalir.setBackground(EstilosApp.COLOR_SECUNDARIO);
            }
        });
        
        panelBotonesSuperior.add(btnPausar);
        panelBotonesSuperior.add(btnSalir);
        
        filaTop.add(panelBotonesSuperior, BorderLayout.EAST);
        
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
        
        lblAciertosContador = new JLabel("Aciertos: " + aciertos);
        lblAciertosContador.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAciertosContador.setForeground(EstilosApp.COLOR_EXITO);
        
        lblErroresContador = new JLabel("Errores: " + errores);
        lblErroresContador.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblErroresContador.setForeground(EstilosApp.COLOR_ERROR);
        
        panelStats.add(lblAciertosContador);
        panelStats.add(lblErroresContador);
        
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
        
        // Panel inferior con botón siguiente
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelInferior.setBackground(EstilosApp.COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 30, 25, 30));
        
        btnSiguiente = new JButton("Siguiente");
        estilizarBotonPrincipal(btnSiguiente);
        btnSiguiente.setEnabled(false);
        
        panelInferior.add(btnSiguiente);
        
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Eventos
        btnSiguiente.addActionListener(e -> siguientePregunta());
        btnPausar.addActionListener(e -> togglePausa());
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
    
    private void togglePausa() {
        pausado = !pausado;
        if (pausado) {
            btnPausar.setText("Reanudar");
            btnPausar.setBackground(EstilosApp.COLOR_EXITO);
        } else {
            btnPausar.setText("Pausar");
            btnPausar.setBackground(new Color(255, 152, 0));
        }
    }
    
    private void mostrarPregunta() {
        preguntaActualObj = estrategia.siguientePregunta(todasLasPreguntas, preguntaActual);
        preguntaRespondida = false;
        
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
        
        btnSiguiente.setEnabled(false);
        btnSiguiente.setBackground(new Color(180, 180, 180));
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
            
            // Evento para seleccionar y comprobar automáticamente
            panelOpcion.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (!preguntaRespondida) {
                        rb.setSelected(true);
                        comprobarRespuesta();
                    }
                }
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!preguntaRespondida && !rb.isSelected()) {
                        panelOpcion.setBackground(new Color(250, 250, 252));
                        rb.setBackground(new Color(250, 250, 252));
                    }
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!preguntaRespondida && !rb.isSelected()) {
                        panelOpcion.setBackground(Color.WHITE);
                        rb.setBackground(Color.WHITE);
                    }
                }
            });
            
            rb.addActionListener(e -> {
                if (!preguntaRespondida) {
                    comprobarRespuesta();
                }
            });
            
            panelOpcion.add(rb, BorderLayout.CENTER);
            panelRespuesta.add(panelOpcion);
            panelRespuesta.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
    
    private void mostrarPreguntaTraduccion() {
        JLabel lblInstruccion = new JLabel("Escribe la traducción y pulsa Enter:");
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
        
        // Comprobar al pulsar Enter
        txtRespuesta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !preguntaRespondida) {
                    comprobarRespuesta();
                }
            }
        });
        
        panelRespuesta.add(txtRespuesta);
        txtRespuesta.requestFocusInWindow();
    }
    
    private void mostrarPreguntaHuecos(PreguntaHuecos pregunta) {
        JLabel lblInstruccion = new JLabel("Completa la palabra que falta y pulsa Enter:");
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
        
        // Comprobar al pulsar Enter
        txtRespuesta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !preguntaRespondida) {
                    comprobarRespuesta();
                }
            }
        });
        
        panelRespuesta.add(txtRespuesta);
        txtRespuesta.requestFocusInWindow();
    }
    
    private void comprobarRespuesta() {
        String respuesta = obtenerRespuesta();
        
        if (respuesta == null || respuesta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una respuesta");
            return;
        }
        
        preguntaRespondida = true;
        
     // Deshabilitar todos los radio buttons inmediatamente
        if (opcionesTest != null) {
            for (JRadioButton rb : opcionesTest) {
                rb.setEnabled(false);
            }
        }
        
        boolean correcta = preguntaActualObj.validarRespuesta(respuesta);
        boolean yaContada = false;
        
        // Verificar si la pregunta ya fue contada anteriormente
        if (estrategia instanceof EstrategiaRepeticionEspaciada) {
            yaContada = ((EstrategiaRepeticionEspaciada) estrategia).esPreguntaYaContada(preguntaActualObj);
        }
        
        panelResultado.setVisible(true);
        
        if (correcta) {
            lblResultado.setText("¡Correcto!");
            lblResultado.setForeground(EstilosApp.COLOR_EXITO);
            lblRespuestaCorrecta.setText("");
            
            if (!yaContada) {
                aciertos++;
                progreso.registrarAcierto();
                lblAciertosContador.setText("Aciertos: " + aciertos);
                
                if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                    ((EstrategiaRepeticionEspaciada) estrategia).marcarComoContada(preguntaActualObj);
                }
            }
            
            if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                ((EstrategiaRepeticionEspaciada) estrategia).registrarAcierto(preguntaActualObj);
            }
            
            if (preguntaActualObj instanceof PreguntaTest) {
                marcarOpcionCorrecta();
            }
        } else {
            String respuestaCorrectaStr = obtenerRespuestaCorrecta();
            lblResultado.setText("Incorrecto");
            lblResultado.setForeground(EstilosApp.COLOR_ERROR);
            lblRespuestaCorrecta.setText("La respuesta correcta era: " + respuestaCorrectaStr);
            
            if (!yaContada) {
                errores++;
                progreso.registrarError();
                lblErroresContador.setText("Errores: " + errores);
                registrarError();
                
                if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                    ((EstrategiaRepeticionEspaciada) estrategia).marcarComoContada(preguntaActualObj);
                }
            }
            
            if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                ((EstrategiaRepeticionEspaciada) estrategia).registrarFallo(preguntaActualObj);
            }
            
            if (preguntaActualObj instanceof PreguntaTest) {
                marcarOpcionIncorrecta();
            }
        }
        
        if (!yaContada) {
            app.getUsuarioActual().getEstadisticas().incrementarEjercicios();
        }
        
        // Deshabilitar campo de texto si existe
        if (txtRespuesta != null) {
            txtRespuesta.setEnabled(false);
        }
        
        btnSiguiente.setEnabled(true);
        btnSiguiente.setBackground(EstilosApp.COLOR_PRIMARIO);
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
            // Mantener color del texto
            rb.setFocusable(false);
            rb.setForeground(EstilosApp.COLOR_TEXTO);
            for (java.awt.event.MouseListener ml : panel.getMouseListeners()) {
                panel.removeMouseListener(ml);
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
            // Mantener color del texto
            rb.setFocusable(false);
            rb.setForeground(EstilosApp.COLOR_TEXTO);
            for (java.awt.event.MouseListener ml : panel.getMouseListeners()) {
                panel.removeMouseListener(ml);
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
        temporizador.stop();
        
        int porcentaje = 0;
        if (aciertos + errores > 0) {
            porcentaje = (aciertos * 100) / (aciertos + errores);
        }
        
        // Guardar tiempo en progreso
        progreso.setTiempoSegundos(segundosTotales);
        
        // Añadir tiempo a estadísticas globales (en minutos)
        int minutos = segundosTotales / 60;
        if (minutos > 0) {
            app.getUsuarioActual().getEstadisticas().incrementarTiempo(minutos);
        }
        
        String tiempoFormateado;
        int horas = segundosTotales / 3600;
        int minutosDisplay = (segundosTotales % 3600) / 60;
        int segundos = segundosTotales % 60;
        if (horas > 0) {
            tiempoFormateado = String.format("%d:%02d:%02d", horas, minutosDisplay, segundos);
        } else {
            tiempoFormateado = String.format("%02d:%02d", minutosDisplay, segundos);
        }
        
        String mensaje = "¡Curso completado!\n\n" +
                        "Aciertos: " + aciertos + "\n" +
                        "Errores: " + errores + "\n" +
                        "Porcentaje: " + porcentaje + "%\n" +
                        "Tiempo: " + tiempoFormateado;
        
        JOptionPane.showMessageDialog(this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        app.guardarProgreso();
        ventanaPrincipal.actualizarListaCursos();
        ventanaPrincipal.setVisible(true);
        this.dispose();
    }
    
    private void salir() {
        temporizador.stop();
        progreso.setTiempoSegundos(segundosTotales);
        
        // Añadir tiempo a estadísticas globales (en minutos)
        int minutos = segundosTotales / 60;
        if (minutos > 0) {
            app.getUsuarioActual().getEstadisticas().incrementarTiempo(minutos);
        }
        
        app.guardarProgreso();
        ventanaPrincipal.actualizarListaCursos();
        ventanaPrincipal.setVisible(true);
        this.dispose();
    }
}