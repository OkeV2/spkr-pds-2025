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
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        EstilosApp.aplicarEstiloVentana(this);
        
        Estadisticas stats = app.obtenerEstadisticasActuales();
        int erroresPendientes = app.contarErroresFrecuentes();
        int aciertos = app.obtenerAciertosEstadisticas();
        int porcentaje = app.calcularPorcentajeAciertos();
        
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
        panelCentral.setBorder(BorderFactory.createEmptyBorder(25, 40, 20, 40));
        
        // Panel del diagrama y porcentaje lado a lado
        JPanel panelDiagramaInfo = new JPanel(new GridLayout(1, 2, 30, 0));
        panelDiagramaInfo.setBackground(EstilosApp.COLOR_FONDO);
        panelDiagramaInfo.setMaximumSize(new Dimension(500, 160));
        panelDiagramaInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Diagrama de queso (solo el gráfico)
        PanelDiagramaQueso diagrama = new PanelDiagramaQueso(aciertos, erroresPendientes);
        diagrama.setPreferredSize(new Dimension(140, 140));
        
        // Panel de información del porcentaje
        JPanel panelPorcentaje = new JPanel();
        panelPorcentaje.setLayout(new BoxLayout(panelPorcentaje, BoxLayout.Y_AXIS));
        panelPorcentaje.setBackground(EstilosApp.COLOR_FONDO);
        
        panelPorcentaje.add(Box.createVerticalGlue());
        
        JLabel lblPorcentajeNum = new JLabel(porcentaje + "%");
        lblPorcentajeNum.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblPorcentajeNum.setForeground(EstilosApp.COLOR_PRIMARIO);
        lblPorcentajeNum.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPorcentaje.add(lblPorcentajeNum);
        
        JLabel lblPorcentajeTexto = new JLabel("de aciertos");
        lblPorcentajeTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPorcentajeTexto.setForeground(EstilosApp.COLOR_SECUNDARIO);
        lblPorcentajeTexto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPorcentaje.add(lblPorcentajeTexto);
        
        panelPorcentaje.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Leyenda
        JPanel leyendaAciertos = crearLeyenda(EstilosApp.COLOR_EXITO, "Aciertos: " + aciertos);
        leyendaAciertos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPorcentaje.add(leyendaAciertos);
        
        panelPorcentaje.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JPanel leyendaErrores = crearLeyenda(EstilosApp.COLOR_ERROR, "Errores: " + erroresPendientes);
        leyendaErrores.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPorcentaje.add(leyendaErrores);
        
        panelPorcentaje.add(Box.createVerticalGlue());
        
        panelDiagramaInfo.add(diagrama);
        panelDiagramaInfo.add(panelPorcentaje);
        
        panelCentral.add(panelDiagramaInfo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Tarjetas de estadísticas
        JPanel panelTarjetas = new JPanel(new GridLayout(2, 2, 15, 15));
        panelTarjetas.setBackground(EstilosApp.COLOR_FONDO);
        panelTarjetas.setMaximumSize(new Dimension(500, 180));
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
        cuadro.setPreferredSize(new Dimension(12, 12));
        
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(EstilosApp.COLOR_TEXTO);
        
        panel.add(cuadro);
        panel.add(label);
        
        return panel;
    }
    
    // Clase interna para el diagrama de queso (solo el gráfico)
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
                // Círculo vacío
                g2d.setColor(new Color(220, 220, 220));
                g2d.fillOval(x, y, size, size);
                
                // Círculo interior
                int innerSize = (int)(size * 0.5);
                int innerX = x + (size - innerSize) / 2;
                int innerY = y + (size - innerSize) / 2;
                g2d.setColor(EstilosApp.COLOR_FONDO);
                g2d.fillOval(innerX, innerY, innerSize, innerSize);
            } else {
                // Sombra
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillOval(x + 2, y + 2, size, size);
                
                // Calcular ángulos
                int anguloAciertos = (int) Math.round(360.0 * aciertos / total);
                
                // Sector aciertos
                g2d.setColor(EstilosApp.COLOR_EXITO);
                g2d.fillArc(x, y, size, size, 90, -anguloAciertos);
                
                // Sector errores
                g2d.setColor(EstilosApp.COLOR_ERROR);
                g2d.fillArc(x, y, size, size, 90 - anguloAciertos, -(360 - anguloAciertos));
                
                // Círculo interior (efecto donut)
                int innerSize = (int)(size * 0.5);
                int innerX = x + (size - innerSize) / 2;
                int innerY = y + (size - innerSize) / 2;
                g2d.setColor(EstilosApp.COLOR_FONDO);
                g2d.fillOval(innerX, innerY, innerSize, innerSize);
            }
        }
    }
}