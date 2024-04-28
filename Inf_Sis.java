package com.mycompany.inf_sis;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Inf_Sis extends JFrame {

    JLabel lbltitulo, JLabel,osNameLabel, osVersionLabel, osArchLabel, userNameLabel, userHomeLabel, userDirLabel;;
    JButton cmdLlamada1, cmdLlamada2, cmdLlamada3;
    
    Inf_Sis (String titulo){
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(340, 100);
    lbltitulo = new JLabel("Interface de llamadas");
    cmdLlamada1 = new JButton("Llamada 1");
    cmdLlamada2 = new JButton("Llamada 2");
    cmdLlamada3 = new JButton("Llamada 3");
    
        String osVersion = System.getProperty("os.version");        
        // Arquitectura del sistema operativo
        String osArch = System.getProperty("os.arch");
        // Nombre del sistema operativo
        String osName = System.getProperty("os.name");
        // Información sobre el usuario actual
        String userName = System.getProperty("user.name");     
        // Directorio de trabajo actual
        String userDir = System.getProperty("user.dir");
        // Directorio del usuario
        String userHome = System.getProperty("user.home");
        
        String mensaje= "Arquitectura del sistema operativo: " + osArch + "\n" +
                "Nombre del sistema operativo: " + osName + "\n" +
                "Informacion sobre el usuario actual: " + userName + "\n" +
                "Directorio de trabajo actual: " + userDir + "\n" +
                "Directorio del usuario: " + userHome;
        
    cmdLlamada1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
       JOptionPane.showMessageDialog(null, mensaje );
    }});
    
    cmdLlamada2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
       
    }});
    
    cmdLlamada3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
       
    }});
    
    JPanel paneltitulo= new JPanel();
    GridLayout g1= new GridLayout(1,1,2,1);
    setLayout(g1);
    paneltitulo.add(lbltitulo);
    
    JPanel panelBotones= new JPanel();
    BorderLayout g2= new BorderLayout();
    setLayout(g2);
    panelBotones.add(cmdLlamada1, BorderLayout.WEST);
    panelBotones.add(cmdLlamada2, BorderLayout.CENTER);
    panelBotones.add(cmdLlamada3, BorderLayout.EAST);
    
     Container cp = getContentPane();
    cp.add(paneltitulo, BorderLayout.NORTH);
    cp.add(panelBotones,BorderLayout.CENTER);
    setVisible(true);

    }
   
    
    
    public static void main(String[] args) {
    Inf_Sis v= new Inf_Sis("Llamadas");

    }
}