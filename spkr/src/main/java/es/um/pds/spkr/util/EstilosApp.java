package es.um.pds.spkr.util;

import java.awt.*;
import javax.swing.*;

public class EstilosApp {
    
    // Colores de la aplicación - Gris elegante
    public static final Color COLOR_PRIMARIO = new Color(70, 70, 70);        // Gris oscuro
    public static final Color COLOR_SECUNDARIO = new Color(100, 100, 100);   // Gris medio
    public static final Color COLOR_FONDO = new Color(250, 250, 250);        // Blanco hueso
    public static final Color COLOR_TEXTO = new Color(33, 33, 33);           // Gris muy oscuro
    public static final Color COLOR_EXITO = new Color(76, 175, 80);          // Verde
    public static final Color COLOR_ERROR = new Color(220, 80, 80);          // Rojo suave
    public static final Color COLOR_BOTON_TEXTO = Color.WHITE;
    
    // Fuentes
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);
    
    // Imágenes
    private static ImageIcon logoOriginal;
    private static ImageIcon iconoOriginal;
    private static ImageIcon iconoInvertidoOriginal;
    
    static {
        try {
            java.net.URL logoUrl = EstilosApp.class.getResource("/imagenes/logo.png");
            if (logoUrl != null) {
                logoOriginal = new ImageIcon(logoUrl);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo: " + e.getMessage());
        }
        try {
            java.net.URL iconoUrl = EstilosApp.class.getResource("/imagenes/icono.png");
            if (iconoUrl != null) {
                iconoOriginal = new ImageIcon(iconoUrl);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
        try {
            java.net.URL iconoInvUrl = EstilosApp.class.getResource("/imagenes/iconoInvertido.png");
            if (iconoInvUrl != null) {
                iconoInvertidoOriginal = new ImageIcon(iconoInvUrl);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono invertido: " + e.getMessage());
        }
    }
    
    public static ImageIcon getLogo(int ancho, int alto) {
        if (logoOriginal == null) return null;
        Image img = logoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    public static ImageIcon getIcono(int ancho, int alto) {
        if (iconoOriginal == null) return null;
        Image img = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    public static void aplicarEstiloBoton(JButton boton) {
        boton.setBackground(COLOR_PRIMARIO);
        boton.setForeground(COLOR_BOTON_TEXTO);
        boton.setFont(FUENTE_BOTON);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        
        // Efecto hover y clic
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            Color colorOriginal = COLOR_PRIMARIO;
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(COLOR_SECUNDARIO);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(colorOriginal);
            }
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(50, 50, 50));
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                boton.setBackground(COLOR_SECUNDARIO);
            }
        });
    }
    
    public static void aplicarEstiloBotonSecundario(JButton boton) {
        boton.setBackground(Color.WHITE);
        boton.setForeground(COLOR_PRIMARIO);
        boton.setFont(FUENTE_BOTON);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        
        // Efecto hover y clic
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(245, 245, 245));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(Color.WHITE);
            }
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(230, 230, 230));
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(245, 245, 245));
            }
        });
    }
    
    public static void aplicarEstiloCampoTexto(JTextField campo) {
        campo.setFont(FUENTE_NORMAL);
        campo.setPreferredSize(new Dimension(250, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    public static void aplicarEstiloPanel(JPanel panel) {
        panel.setBackground(COLOR_FONDO);
    }
    
    public static void aplicarEstiloVentana(JFrame ventana) {
        ventana.getContentPane().setBackground(COLOR_FONDO);
        
        // Usar lista de iconos para diferentes tamaños
        java.util.List<Image> iconos = new java.util.ArrayList<>();
        
        if (iconoOriginal != null) {
            // Icono pequeño para esquina de ventana (16x16)
            iconos.add(iconoOriginal.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        }
        
        if (iconoInvertidoOriginal != null) {
            // Icono grande para barra de tareas (32x32, 48x48)
            iconos.add(iconoInvertidoOriginal.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            iconos.add(iconoInvertidoOriginal.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
        }
        
        if (!iconos.isEmpty()) {
            ventana.setIconImages(iconos);
        }
    }
    
    public static JLabel crearTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_TITULO);
        label.setForeground(COLOR_PRIMARIO);
        return label;
    }
    
    public static JLabel crearSubtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_SUBTITULO);
        label.setForeground(COLOR_TEXTO);
        return label;
    }
    
    public static JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_NORMAL);
        label.setForeground(COLOR_TEXTO);
        return label;
    }
}