package com.mycompany.inf_sis;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;


class Panel2 extends JFrame {
     JLabel Labeltitulo2 = new JLabel("Dispositivo de entrada 'Microfono'");
     JButton button = new JButton("Grabar");
     JFrame frame = new JFrame("Llamada de entrada");
     
     Panel2 (String titulo1){
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(250, 200);
        frame.setLayout(new FlowLayout());

        
        // ActionListener para cambiar el texto del botón cuando se hace clic en él
        button.addActionListener(new ActionListener() {
            boolean grabando = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                grabando = !grabando; // Cambiar el estado de grabando
                if (grabando) {
                    button.setText("Detener");
                    ManejadorAudio.iniciarGrabacion();
                } else {
                    button.setText("Grabar");
                    ManejadorAudio.detenerGrabacion();
                    frame.dispose();
                    // Aquí puedes agregar lógica para detener la grabación
                }
            }
        });

        frame.getContentPane().add(Labeltitulo2);
        frame.getContentPane().add(button);
        frame.setVisible(true);
    }
}
     
