package com.mycompany.inf_sis;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;


class Panel3 extends JFrame {
     JLabel Labeltitulo2 = new JLabel("Dispositivo de salida 'Bocina'");
     JButton button = new JButton("Reproducir");
     JFrame frame = new JFrame("Llamada de salida");
     JFileChooser buscadorArchivos = new JFileChooser();
     
     Panel3 (String titulo1){
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(250, 200);
        frame.setLayout(new FlowLayout());
        buscadorArchivos.setFileFilter(new FileNameExtensionFilter("archivos WAV", "wav"));

        
        // ActionListener para cambiar el texto del botón cuando se hace clic en él
        button.addActionListener(new ActionListener() {
            boolean reproduciendo = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!reproduciendo) {
                    buscadorArchivos.setDialogTitle("Elegir archivo para reproducir");
                    int estadoBusqueda = buscadorArchivos.showOpenDialog(Panel3.this);
                    if (estadoBusqueda == JFileChooser.APPROVE_OPTION) {
                        reproduciendo = !reproduciendo;
                        button.setText("Detener");
                        ManejadorAudio.setArchivo(buscadorArchivos.getSelectedFile());
                        ManejadorAudio.iniciarReproduccion();
                    }
                } else {
                    reproduciendo = !reproduciendo;
                    button.setText("Reproducir");
                    ManejadorAudio.detenerReproduccion();
                }
            }
        });

        frame.getContentPane().add(Labeltitulo2);
        frame.getContentPane().add(button);
        frame.setVisible(true);
    }
}
     
