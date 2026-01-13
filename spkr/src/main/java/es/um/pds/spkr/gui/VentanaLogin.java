package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.um.pds.spkr.SpkrApp;

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
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel lblTitulo = new JLabel("SPKR");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        // Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblUsuario, gbc);
        
        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtUsuario, gbc);
        
        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPassword, gbc);
        
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtPassword, gbc);
        
        // Mensaje de error
        lblMensaje = new JLabel("");
        lblMensaje.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(lblMensaje, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel();
        btnLogin = new JButton("Iniciar Sesión");
        btnRegistro = new JButton("Registrarse");
        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistro);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);
        
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