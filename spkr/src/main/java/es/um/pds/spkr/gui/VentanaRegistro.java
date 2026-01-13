package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.util.EstilosApp;

public class VentanaRegistro extends JFrame {
    
    private SpkrApp app;
    private VentanaLogin ventanaLogin;
    private JTextField txtUsuario;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private JLabel lblMensaje;
    
    public VentanaRegistro(SpkrApp app, VentanaLogin ventanaLogin) {
        this.app = app;
        this.ventanaLogin = ventanaLogin;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Registro");
        setSize(450, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Título
        JLabel lblTitulo = EstilosApp.crearSubtitulo("Crear cuenta");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
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
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Campo Email
        JLabel lblEmail = EstilosApp.crearEtiqueta("Email");
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtEmail = new JTextField();
        EstilosApp.aplicarEstiloCampoTexto(txtEmail);
        txtEmail.setMaximumSize(new Dimension(250, 35));
        txtEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(txtEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
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
        
        // Campo Confirmar Contraseña
        JLabel lblConfirmar = EstilosApp.crearEtiqueta("Confirmar contraseña");
        lblConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblConfirmar);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtConfirmarPassword = new JPasswordField();
        EstilosApp.aplicarEstiloCampoTexto(txtConfirmarPassword);
        txtConfirmarPassword.setMaximumSize(new Dimension(250, 35));
        txtConfirmarPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(txtConfirmarPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Mensaje de error
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(EstilosApp.FUENTE_NORMAL);
        lblMensaje.setForeground(EstilosApp.COLOR_ERROR);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblMensaje);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Botón Registrar
        btnRegistrar = new JButton("Registrar");
        EstilosApp.aplicarEstiloBoton(btnRegistrar);
        btnRegistrar.setMaximumSize(new Dimension(250, 40));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnRegistrar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        EstilosApp.aplicarEstiloBotonSecundario(btnCancelar);
        btnCancelar.setMaximumSize(new Dimension(250, 40));
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnCancelar);
        
        add(panel);
        
        // Eventos
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrar();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void registrar() {
        String usuario = txtUsuario.getText();
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());
        String confirmar = new String(txtConfirmarPassword.getPassword());
        
        if (usuario.isEmpty() || email.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            return;
        }
        
        if (!password.equals(confirmar)) {
            lblMensaje.setText("Las contraseñas no coinciden");
            return;
        }
        
        if (app.registrarUsuario(usuario, email, password)) {
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");
            dispose();
        } else {
            lblMensaje.setText("El usuario o email ya existe");
        }
    }
}