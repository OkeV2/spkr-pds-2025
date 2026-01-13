package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.um.pds.spkr.SpkrApp;

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
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel lblTitulo = new JLabel("Registro");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
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
        
        // Email
        JLabel lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblEmail, gbc);
        
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtEmail, gbc);
        
        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblPassword, gbc);
        
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(txtPassword, gbc);
        
        // Confirmar contraseña
        JLabel lblConfirmar = new JLabel("Confirmar contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblConfirmar, gbc);
        
        txtConfirmarPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(txtConfirmarPassword, gbc);
        
        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(lblMensaje, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel();
        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);
        
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