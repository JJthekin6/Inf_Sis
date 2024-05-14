package com.mycompany.inf_sis;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;


class Panel2 extends JFrame {
     JLabel Labeltitulo2 = new JLabel("Dispositivo de entrada 'Microfono'");
     JButton button = new JButton("Grabar");
     JFrame frame = new JFrame("Llamada de entrada");
     JFileChooser buscadorArchivos = new JFileChooser();
     
     Panel2 (String titulo1){
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(250, 200);
        frame.setLayout(new FlowLayout());
        buscadorArchivos.setFileFilter(new FileNameExtensionFilter("archivos WAV", "wav"));

        
        // ActionListener para cambiar el texto del botón cuando se hace clic en él
        button.addActionListener(new ActionListener() {
            boolean grabando = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!grabando) {
                    buscadorArchivos.setDialogTitle("Elegir ubicación de guardado");
                    int estadoBusqueda = buscadorArchivos.showSaveDialog(Panel2.this);
                    if (estadoBusqueda == JFileChooser.APPROVE_OPTION) {
                        grabando = !grabando;
                        button.setText("Detener");
                        ManejadorAudio.setArchivo(buscadorArchivos.getSelectedFile());
                        ManejadorAudio.iniciarGrabacion();
                    }
                } else {
                    grabando = !grabando;
                    button.setText("Grabar");
                    ManejadorAudio.detenerGrabacion();
                    frame.dispose();
                }
            }
        });

        frame.getContentPane().add(Labeltitulo2);
        frame.getContentPane().add(button);
        frame.setVisible(true);
    }
}
     
