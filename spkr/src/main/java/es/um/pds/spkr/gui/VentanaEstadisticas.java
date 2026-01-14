package es.um.pds.spkr.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        EstilosApp.aplicarEstiloVentana(this);
        
        Estadisticas stats = app.getUsuarioActual().getEstadisticas();
        int erroresPendientes = app.getUsuarioActual().getErroresFrecuentes().size();
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        EstilosApp.aplicarEstiloPanel(panel);
        
        // Icono y título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        EstilosApp.aplicarEstiloPanel(panelTitulo);
        
        JLabel lblIcono = new JLabel();
        ImageIcon icono = EstilosApp.getIcono(40, 40);
        if (icono != null) {
            lblIcono.setIcon(icono);
        }
        panelTitulo.add(lblIcono);
        
        JLabel lblTitulo = EstilosApp.crearSubtitulo("Mis Estadísticas");
        panelTitulo.add(lblTitulo);
        
        panel.add(panelTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Panel de diagrama
        int aciertos = stats.getEjerciciosCompletados() - erroresPendientes;
        if (aciertos < 0) aciertos = stats.getEjerciciosCompletados();
        
        PanelDiagramaQueso diagrama = new PanelDiagramaQueso(aciertos, erroresPendientes);
        diagrama.setPreferredSize(new Dimension(200, 200));
        diagrama.setMaximumSize(new Dimension(200, 200));
        diagrama.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(diagrama);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Leyenda del diagrama
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        EstilosApp.aplicarEstiloPanel(panelLeyenda);
        
        JPanel leyendaAciertos = crearLeyenda(EstilosApp.COLOR_EXITO, "Aciertos");
        JPanel leyendaErrores = crearLeyenda(EstilosApp.COLOR_ERROR, "Errores");
        
        panelLeyenda.add(leyendaAciertos);
        panelLeyenda.add(leyendaErrores);
        
        panel.add(panelLeyenda);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Panel de estadísticas detalladas
        JPanel panelStats = new JPanel(new GridLayout(5, 2, 10, 15));
        EstilosApp.aplicarEstiloPanel(panelStats);
        panelStats.setMaximumSize(new Dimension(350, 200));
        panelStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelStats.add(EstilosApp.crearEtiqueta("Ejercicios completados:"));
        panelStats.add(crearValor(String.valueOf(stats.getEjerciciosCompletados())));
        
        panelStats.add(EstilosApp.crearEtiqueta("Tiempo total de uso:"));
        panelStats.add(crearValor(stats.getTiempoTotalUso() + " minutos"));
        
        panelStats.add(EstilosApp.crearEtiqueta("Racha actual:"));
        panelStats.add(crearValor(stats.getRachaActual() + " días"));
        
        panelStats.add(EstilosApp.crearEtiqueta("Mejor racha:"));
        panelStats.add(crearValor(stats.getMejorRacha() + " días"));
        
        panelStats.add(EstilosApp.crearEtiqueta("Errores pendientes:"));
        panelStats.add(crearValor(String.valueOf(erroresPendientes)));
        
        panel.add(panelStats);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        EstilosApp.aplicarEstiloBoton(btnCerrar);
        btnCerrar.setMaximumSize(new Dimension(150, 40));
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnCerrar);
        
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        add(panel);
    }
    
    private JLabel crearValor(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(EstilosApp.COLOR_PRIMARIO);
        return label;
    }
    
    private JPanel crearLeyenda(Color color, String texto) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        EstilosApp.aplicarEstiloPanel(panel);
        
        JPanel cuadro = new JPanel();
        cuadro.setBackground(color);
        cuadro.setPreferredSize(new Dimension(15, 15));
        
        JLabel label = EstilosApp.crearEtiqueta(texto);
        
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
            
            int size = Math.min(getWidth(), getHeight()) - 20;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            
            if (total == 0) {
                // Si no hay datos, mostrar círculo gris
                g2d.setColor(new Color(200, 200, 200));
                g2d.fillOval(x, y, size, size);
                
                g2d.setColor(EstilosApp.COLOR_TEXTO);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                String texto = "Sin datos";
                FontMetrics fm = g2d.getFontMetrics();
                int textoX = x + (size - fm.stringWidth(texto)) / 2;
                int textoY = y + (size + fm.getAscent()) / 2;
                g2d.drawString(texto, textoX, textoY);
            } else {
                // Dibujar sección de aciertos (verde)
                int anguloAciertos = (int) Math.round(360.0 * aciertos / total);
                g2d.setColor(EstilosApp.COLOR_EXITO);
                g2d.fillArc(x, y, size, size, 90, -anguloAciertos);
                
                // Dibujar sección de errores (rojo)
                g2d.setColor(EstilosApp.COLOR_ERROR);
                g2d.fillArc(x, y, size, size, 90 - anguloAciertos, -(360 - anguloAciertos));
                
                // Dibujar porcentajes en el centro
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                
                int porcentajeAciertos = (aciertos * 100) / total;
                String texto = porcentajeAciertos + "% éxito";
                FontMetrics fm = g2d.getFontMetrics();
                int textoX = x + (size - fm.stringWidth(texto)) / 2;
                int textoY = y + (size + fm.getAscent()) / 2;
                
                // Fondo para el texto
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillOval(x + size/4, y + size/4, size/2, size/2);
                
                g2d.setColor(Color.WHITE);
                g2d.drawString(texto, textoX, textoY);
            }
        }
    }
}