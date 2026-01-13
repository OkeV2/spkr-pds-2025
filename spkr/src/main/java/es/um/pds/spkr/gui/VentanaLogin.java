package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.util.EstilosApp;

public class VentanaLogin extends JFrame {
    
    private SpkrApp app;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JLabel lblMensaje;
    
    public VentanaLogin(SpkrApp app) {
        this.app = app;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Iniciar Sesión");
        setSize(450, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        EstilosApp.aplicarEstiloVentana(this);
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 40, 50));
        EstilosApp.aplicarEstiloPanel(panel);
        
        // Logo
        JLabel lblLogo = new JLabel();
        ImageIcon logo = EstilosApp.getLogo(200, 200);
        if (logo != null) {
            lblLogo.setIcon(logo);
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblLogo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Campo Usuario
        JLabel lblUsuario = EstilosApp.crearEtiqueta("Usuario");
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblUsuario);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtUsuario = new JTextField();
        EstilosApp.aplicarEstiloCampoTexto(txtUsuario);
        txtUsuario.setMaximumSize(new Dimension(250, 35));
        txtUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(txtUsuario);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo Contraseña
        JLabel lblPassword = EstilosApp.crearEtiqueta("Contraseña");
        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtPassword = new JPasswordField();
        EstilosApp.aplicarEstiloCampoTexto(txtPassword);
        txtPassword.setMaximumSize(new Dimension(250, 35));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(txtPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Mensaje de error
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(EstilosApp.FUENTE_NORMAL);
        lblMensaje.setForeground(EstilosApp.COLOR_ERROR);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblMensaje);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Botón Login
        btnLogin = new JButton("Iniciar Sesión");
        EstilosApp.aplicarEstiloBoton(btnLogin);
        btnLogin.setMaximumSize(new Dimension(250, 40));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnLogin);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Botón Registro
        btnRegistro = new JButton("Registrarse");
        EstilosApp.aplicarEstiloBotonSecundario(btnRegistro);
        btnRegistro.setMaximumSize(new Dimension(250, 40));
        btnRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnRegistro);
        
        add(panel);
        
        // Eventos
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        btnRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirRegistro();
            }
        });
    }
    
    private void login() {
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());
        
        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            return;
        }
        
        if (app.login(usuario, password)) {
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(app);
            ventanaPrincipal.setVisible(true);
            this.dispose();
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
        }
    }
    
    private void abrirRegistro() {
        VentanaRegistro ventanaRegistro = new VentanaRegistro(app, this);
        ventanaRegistro.setVisible(true);
    }
}