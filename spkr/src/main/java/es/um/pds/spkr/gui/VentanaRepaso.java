package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
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
        erroresPendientes = new ArrayList<>(app.obtenerErroresFrecuentesActuales());
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Repaso de Errores");
        setSize(650, 580);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        EstilosApp.aplicarEstiloVentana(this);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(EstilosApp.COLOR_FONDO);
        
        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Fila superior
        JPanel filaTop = new JPanel(new BorderLayout());
        filaTop.setBackground(Color.WHITE);
        filaTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JPanel panelTituloIcono = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTituloIcono.setBackground(Color.WHITE);
        
        JLabel lblIcono = new JLabel();
        ImageIcon icono = EstilosApp.getIcono(30, 30);
        if (icono != null) {
            lblIcono.setIcon(icono);
        }
        panelTituloIcono.add(lblIcono);
        
        JLabel lblTitulo = new JLabel("Repaso de Errores");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(EstilosApp.COLOR_TEXTO);
        panelTituloIcono.add(lblTitulo);
        
        filaTop.add(panelTituloIcono, BorderLayout.WEST);
        
        lblPreguntaNum = new JLabel();
        lblPreguntaNum.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPreguntaNum.setForeground(EstilosApp.COLOR_SECUNDARIO);
        filaTop.add(lblPreguntaNum, BorderLayout.EAST);
        
        panelSuperior.add(filaTop);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Barra de progreso
        barraProgreso = new JProgressBar(0, erroresPendientes.size());
        barraProgreso.setValue(0);
        barraProgreso.setPreferredSize(new Dimension(0, 8));
        barraProgreso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        barraProgreso.setBorderPainted(false);
        barraProgreso.setBackground(new Color(230, 230, 230));
        barraProgreso.setForeground(new Color(255, 152, 0));
        panelSuperior.add(barraProgreso);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(EstilosApp.COLOR_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        
        lblEnunciado = new JLabel();
        lblEnunciado.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblEnunciado.setForeground(EstilosApp.COLOR_TEXTO);
        lblEnunciado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblEnunciado);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 25)));
        
        panelRespuesta = new JPanel();
        panelRespuesta.setLayout(new BoxLayout(panelRespuesta, BoxLayout.Y_AXIS));
        panelRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelRespuesta.setBackground(EstilosApp.COLOR_FONDO);
        panelCentral.add(panelRespuesta);
        
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        
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
        
        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelInferior.setBackground(EstilosApp.COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 30, 25, 30));
        
        btnComprobar = new JButton("Comprobar");
        btnSiguiente = new JButton("Siguiente");
        btnSalir = new JButton("Salir");
        
        estilizarBotonPrincipal(btnComprobar);
        estilizarBotonSecundario(btnSiguiente);
        estilizarBotonSalir(btnSalir);
        
        btnSiguiente.setEnabled(false);
        
        panelInferior.add(btnComprobar);
        panelInferior.add(btnSiguiente);
        panelInferior.add(btnSalir);
        
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
        boton.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(EstilosApp.COLOR_SECUNDARIO);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(EstilosApp.COLOR_PRIMARIO);
            }
        });
    }
    
    private void estilizarBotonSecundario(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(Color.WHITE);
        boton.setForeground(EstilosApp.COLOR_PRIMARIO);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstilosApp.COLOR_PRIMARIO, 2),
            BorderFactory.createEmptyBorder(10, 33, 10, 33)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) boton.setBackground(Color.WHITE);
            }
        });
    }
    
    private void estilizarBotonSalir(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        boton.setBackground(EstilosApp.COLOR_FONDO);
        boton.setForeground(new Color(150, 150, 150));
        boton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setForeground(EstilosApp.COLOR_TEXTO);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setForeground(new Color(150, 150, 150));
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
        
        lblPreguntaNum.setText((indiceActual + 1) + " / " + erroresPendientes.size());
        barraProgreso.setValue(indiceActual);
        lblEnunciado.setText(preguntaActualObj.getEnunciado());
        
        panelResultado.setVisible(false);
        
        panelRespuesta.removeAll();
        panelesOpciones = new ArrayList<>();
        
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
            
            panelOpcion.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    rb.setSelected(true);
                    actualizarSeleccionOpciones();
                }
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!rb.isSelected()) {
                        panelOpcion.setBackground(new Color(250, 250, 252));
                        rb.setBackground(new Color(250, 250, 252));
                    }
                }
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
                panel.setBackground(new Color(255, 248, 235));
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 152, 0), 2),
                    BorderFactory.createEmptyBorder(14, 19, 14, 19)
                ));
                rb.setBackground(new Color(255, 248, 235));
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
        panelRespuesta.add(lblInstruccion);
        panelRespuesta.add(Box.createRigidArea(new Dimension(0, 12)));
        
        txtRespuesta = new JTextField();
        txtRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtRespuesta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtRespuesta.setMaximumSize(new Dimension(400, 50));
        panelRespuesta.add(txtRespuesta);
    }
    
    private void mostrarPreguntaHuecos() {
        JLabel lblInstruccion = new JLabel("Completa la palabra que falta:");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setForeground(EstilosApp.COLOR_SECUNDARIO);
        panelRespuesta.add(lblInstruccion);
        panelRespuesta.add(Box.createRigidArea(new Dimension(0, 12)));
        
        txtRespuesta = new JTextField();
        txtRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtRespuesta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtRespuesta.setMaximumSize(new Dimension(400, 50));
        panelRespuesta.add(txtRespuesta);
    }
    
    private void comprobarRespuesta() {
        String respuesta = obtenerRespuesta();

        if (respuesta == null || respuesta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una respuesta");
            return;
        }

        // Delegar la lógica de negocio al controlador
        ResultadoRespuesta resultado = app.procesarRespuestaRepaso(errorActual, respuesta);

        panelResultado.setVisible(true);

        if (resultado.isCorrecta()) {
            lblResultado.setText("¡Correcto! Error dominado.");
            lblResultado.setForeground(EstilosApp.COLOR_EXITO);
            lblRespuestaCorrecta.setText("");
            aciertos++;
        } else {
            lblResultado.setText("Incorrecto");
            lblResultado.setForeground(EstilosApp.COLOR_ERROR);
            lblRespuestaCorrecta.setText("La respuesta correcta era: " + resultado.getRespuestaCorrecta());
            errores++;
        }

        btnComprobar.setEnabled(false);
        btnComprobar.setBackground(new Color(180, 180, 180));
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