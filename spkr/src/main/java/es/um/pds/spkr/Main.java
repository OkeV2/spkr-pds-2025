package es.um.pds.spkr;

import javax.swing.SwingUtilities;

import es.um.pds.spkr.gui.VentanaLogin;

public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SpkrApp app = new SpkrApp();
                VentanaLogin ventanaLogin = new VentanaLogin(app);
                ventanaLogin.setVisible(true);
            }
        });
    }
}