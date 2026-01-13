package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.modelo.Estadisticas;

public class VentanaEstadisticas extends JFrame {
    
    private SpkrApp app;
    
    public VentanaEstadisticas(SpkrApp app) {
        this.app = app;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Estadísticas");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        Estadisticas stats = app.getUsuarioActual().getEstadisticas();
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        JLabel lblTitulo = new JLabel("Mis Estadísticas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitulo, gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        
        // Tiempo total
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tiempo total de uso:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(stats.getTiempoTotalUso() + " minutos"), gbc);
        
        // Racha actual
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Racha actual:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(stats.getRachaActual() + " días"), gbc);
        
        // Mejor racha
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Mejor racha:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(stats.getMejorRacha() + " días"), gbc);
        
        // Ejercicios completados
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Ejercicios completados:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(stats.getEjerciciosCompletados())), gbc);
        
        // Errores frecuentes
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Errores pendientes:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(app.getUsuarioActual().getErroresFrecuentes().size())), gbc);
        
        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnCerrar, gbc);
        
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        add(panel);
    }
}