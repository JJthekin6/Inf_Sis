package com.mycompany.inf_sis;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;


class Panel3 extends JFrame {
     JLabel Labeltitulo2 = new JLabel("Dispositivo de salida 'Bocina'");
     JButton button = new JButton("Reproducir");
     JFrame frame = new JFrame("Llamada de salida");
     
     Panel3 (String titulo1){
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(250, 200);
        frame.setLayout(new FlowLayout());

        
        // ActionListener para cambiar el texto del botón cuando se hace clic en él
        button.addActionListener(new ActionListener() {
            boolean reproduciendo = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                reproduciendo = !reproduciendo; // Cambiar el estado de grabando
                if (reproduciendo) {
                    button.setText("Detener");
                    ManejadorAudio.iniciarReproduccion();
                } else {
                    button.setText("Reproducir");
                    ManejadorAudio.detenerReproduccion();
                    // Aquí puedes agregar lógica para detener la grabación
                }
            }
        });

        frame.getContentPane().add(Labeltitulo2);
        frame.getContentPane().add(button);
        frame.setVisible(true);
    }
}
     
