package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;

import es.um.pds.spkr.SpkrApp;
import es.um.pds.spkr.modelo.Estadisticas;
import es.um.pds.spkr.util.EstilosApp;

public class VentanaEstadisticas extends JFrame {
    
    private SpkrApp app;
    
    public VentanaEstadisticas(SpkrApp app) {
        this.app = app;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Spkr - Estadísticas");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        EstilosApp.aplicarEstiloVentana(this);
        
        Estadisticas stats = app.getUsuarioActual().getEstadisticas();
        int erroresPendientes = app.getUsuarioActual().getErroresFrecuentes().size();
        int aciertos = stats.getEjerciciosCompletados() - erroresPendientes;
        if (aciertos < 0) aciertos = stats.getEjerciciosCompletados();
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(EstilosApp.COLOR_FONDO);
        
        // Panel superior con título
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelTitulo.setBackground(Color.WHITE);
        
        JLabel lblIcono = new JLabel();
        ImageIcon icono = EstilosApp.getIcono(40, 40);
        if (icono != null) {
            lblIcono.setIcon(icono);
        }
        panelTitulo.add(lblIcono);
        
        JLabel lblTitulo = new JLabel("Mis Estadísticas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(EstilosApp.COLOR_TEXTO);
        panelTitulo.add(lblTitulo);
        
        panelSuperior.add(panelTitulo);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(EstilosApp.COLOR_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        
        // Diagrama de queso
        PanelDiagramaQueso diagrama = new PanelDiagramaQueso(aciertos, erroresPendientes);
        diagrama.setPreferredSize(new Dimension(220, 220));
        diagrama.setMaximumSize(new Dimension(220, 220));
        diagrama.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(diagrama);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Leyenda
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelLeyenda.setBackground(EstilosApp.COLOR_FONDO);
        panelLeyenda.add(crearLeyenda(EstilosApp.COLOR_EXITO, "Aciertos"));
        panelLeyenda.add(crearLeyenda(EstilosApp.COLOR_ERROR, "Errores"));
        panelCentral.add(panelLeyenda);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Tarjetas de estadísticas
        JPanel panelTarjetas = new JPanel(new GridLayout(2, 2, 15, 15));
        panelTarjetas.setBackground(EstilosApp.COLOR_FONDO);
        panelTarjetas.setMaximumSize(new Dimension(450, 180));
        panelTarjetas.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelTarjetas.add(crearTarjetaEstadistica("Ejercicios", String.valueOf(stats.getEjerciciosCompletados()), EstilosApp.COLOR_PRIMARIO));
        panelTarjetas.add(crearTarjetaEstadistica("Tiempo de uso", stats.getTiempoTotalUso() + " min", EstilosApp.COLOR_SECUNDARIO));
        panelTarjetas.add(crearTarjetaEstadistica("Racha actual", stats.getRachaActual() + " días", EstilosApp.COLOR_EXITO));
        panelTarjetas.add(crearTarjetaEstadistica("Mejor racha", stats.getMejorRacha() + " días", new Color(255, 152, 0)));
        
        panelCentral.add(panelTarjetas);
        panelCentral.add(Box.createVerticalGlue());
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botón
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInferior.setBackground(EstilosApp.COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 30, 25, 30));
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBackground(EstilosApp.COLOR_PRIMARIO);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(12, 50, 12, 50));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setOpaque(true);
        
        btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCerrar.setBackground(EstilosApp.COLOR_SECUNDARIO);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCerrar.setBackground(EstilosApp.COLOR_PRIMARIO);
            }
        });
        
        btnCerrar.addActionListener(e -> dispose());
        
        panelInferior.add(btnCerrar);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(color);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setForeground(EstilosApp.COLOR_SECUNDARIO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        tarjeta.add(lblValor);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 8)));
        tarjeta.add(lblTitulo);
        
        return tarjeta;
    }
    
    private JPanel crearLeyenda(Color color, String texto) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setBackground(EstilosApp.COLOR_FONDO);
        
        JPanel cuadro = new JPanel();
        cuadro.setBackground(color);
        cuadro.setPreferredSize(new Dimension(14, 14));
        
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(EstilosApp.COLOR_TEXTO);
        
        panel.add(cuadro);
        panel.add(label);
        
        return panel;
    }
    
    // Clase interna para el diagrama de queso
    private class PanelDiagramaQueso extends JPanel {
        
        private int aciertos;
        private int errores;
        
        public PanelDiagramaQueso(int aciertos, int errores) {
            this.aciertos = aciertos;
            this.errores = errores;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int total = aciertos + errores;
            
            int size = Math.min(getWidth(), getHeight()) - 10;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            
            if (total == 0) {
                g2d.setColor(new Color(220, 220, 220));
                g2d.fillOval(x, y, size, size);
                
                g2d.setColor(EstilosApp.COLOR_SECUNDARIO);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                String texto = "Sin datos";
                FontMetrics fm = g2d.getFontMetrics();
                int textoX = x + (size - fm.stringWidth(texto)) / 2;
                int textoY = y + (size + fm.getAscent()) / 2 - 5;
                g2d.drawString(texto, textoX, textoY);
            } else {
                int anguloAciertos = (int) Math.round(360.0 * aciertos / total);
                g2d.setColor(EstilosApp.COLOR_EXITO);
                g2d.fillArc(x, y, size, size, 90, -anguloAciertos);
                
                g2d.setColor(EstilosApp.COLOR_ERROR);
                g2d.fillArc(x, y, size, size, 90 - anguloAciertos, -(360 - anguloAciertos));
                
                // Círculo central
                int centroSize = size / 2;
                int centroX = x + (size - centroSize) / 2;
                int centroY = y + (size - centroSize) / 2;
                g2d.setColor(EstilosApp.COLOR_FONDO);
                g2d.fillOval(centroX, centroY, centroSize, centroSize);
                
                // Porcentaje
                int porcentaje = (aciertos * 100) / total;
                g2d.setColor(EstilosApp.COLOR_TEXTO);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
                String texto = porcentaje + "%";
                FontMetrics fm = g2d.getFontMetrics();
                int textoX = x + (size - fm.stringWidth(texto)) / 2;
                int textoY = y + (size + fm.getAscent()) / 2 - 5;
                g2d.drawString(texto, textoX, textoY);
            }
        }
    }
}