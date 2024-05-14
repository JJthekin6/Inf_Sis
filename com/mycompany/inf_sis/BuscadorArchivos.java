package com.mycompany.inf_sis;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;
import java.io.*;

public class BuscadorArchivos extends JFrame implements ActionListener {
    JLabel titulo;
    JButton boton1_clanex, boton2_entrada, boton3_salida, boton4_salir;

    //agregar una barra de nuestro menu
    JMenuBar menuBar1;

    //opciones que llevara nuestro menu bar
    JMenu opciones  , colores;

    //las pestanas que tendra nuestro menu item 
    JMenuItem  rojo , azul , verde , miElCreador;


    public BuscadorArchivos() {
        setLayout(null);

        menuBar1 = new JMenuBar();
        setJMenuBar(menuBar1);

        opciones = new JMenu("Opciones");
        menuBar1.add(opciones);
        
        colores = new JMenu("color de fondo");
        opciones.add(colores);

        rojo = new JMenuItem("Rojo");
        colores.add(rojo);
        rojo.addActionListener(this);

        azul = new JMenuItem("Azul");
        colores.add(azul);
        azul.addActionListener(this);

        verde = new JMenuItem("Verde");
        colores.add(verde);
        verde.addActionListener(this);

        miElCreador = new JMenuItem("El creador");
        menuBar1.add(miElCreador);
        miElCreador.addActionListener(this);


        titulo = new JLabel("LLAMADAS DE SISTEMAS OPERATIVOS");
        titulo.setBounds(130, 25, 350, 30);
        titulo.setFont(new Font("Arial", Font.BOLD, 17)); // Establecer el tamaño de la fuente y el estilo
        titulo.setForeground(Color.black); // Establecer el color del texto
        add(titulo);

        // boton para la funcion de sistemas clanex
        boton1_clanex = new JButton(" Info del Sistema");
        boton1_clanex.setBackground(Color.ORANGE);
        boton1_clanex.setBounds(40, 120, 150, 50);
        boton1_clanex.setFont(new Font("Arial", Font.PLAIN, 14)); // Establecer la fuente Arial
        boton1_clanex.setForeground(Color.black); // Establecer el color de letra rojo
        add(boton1_clanex);
        boton1_clanex.addActionListener(this);

        // boton para funcion de sistemas de entrada
        boton2_entrada = new JButton("Sistema De Entrada");
        boton2_entrada.setBackground(Color.ORANGE);
        boton2_entrada.setBounds(210, 120, 170, 50);
        boton2_entrada.setFont(new Font("Arial", Font.PLAIN, 14));
        boton2_entrada.setForeground(Color.black);
        add(boton2_entrada);
        boton2_entrada.addActionListener(this);

        // boton para la funcion de sistema de salida
        boton3_salida = new JButton("Sistema de Salida");
        boton3_salida.setBackground(Color.orange);
        boton3_salida.setBounds(400, 120, 170, 50);
        boton3_salida.setFont(new Font("Arial", Font.PLAIN, 14));
        boton3_salida.setForeground(Color.black);
        add(boton3_salida);
        boton3_salida.addActionListener(this);

        // boton para salir del programa
        boton4_salir = new JButton("SALIR");
        boton4_salir.setBackground(Color.red);
        boton4_salir.setBounds(410, 240, 170, 50);
        boton4_salir.setFont(new Font("Arial", Font.PLAIN, 14));
        boton4_salir.setForeground(Color.black);
        add(boton4_salir);
        boton4_salir.addActionListener(this);

        

    }

    public void actionPerformed(ActionEvent oliver) {
        if (oliver.getSource() == boton1_clanex) {
            // funcion para llamar al sistema y mostrar informacion
            mostrarInformacionSistema();
        }

        if (oliver.getSource() == boton2_entrada) {
            // mostrar la funcion donde te de la opcion de abrir microfono y grabar y que se
            // guarde
            escribirArchivo();
        }

        if (oliver.getSource() == boton3_salida) {
            mostrardatos();
        }
        // funcion para salir del programas
        if (oliver.getSource() == boton4_salir) {
            System.exit(0);
        }

        if (oliver.getSource()==rojo) {
            getContentPane().setBackground(new Color(255 , 0,0));
        }

        if (oliver.getSource()==azul) {
            getContentPane().setBackground(new Color(0 , 0,255));
        }

        if (oliver.getSource()==verde) {
            getContentPane().setBackground(new Color(0 , 255,0));
        }

        if (oliver.getSource() == miElCreador) {
            JOptionPane.showMessageDialog(null, "Es desarrollado por el equipo de Wilbert Chan, Vanessa Acosta, Miguel Arceo y Carlos Noh\n");
        }
    }

    private void mostrarInformacionSistema() {
        try {
            // Ejecutar el comando del sistema para obtener información general del sistema
            Process proceso = Runtime.getRuntime().exec("systeminfo");

            // Leer la salida del proceso
            java.util.Scanner scanner = new java.util.Scanner(proceso.getInputStream()).useDelimiter("\\A");
            String informacion = scanner.hasNext() ? scanner.next() : "";

            // Mostrar la información en un cuadro de diálogo emergente
            JTextArea areaTexto = new JTextArea(informacion);
            areaTexto.setFont(new Font("Arial", Font.PLAIN, 12));
            areaTexto.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(areaTexto);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "Información del Sistema", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener información del sistema.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void escribirArchivo() {
        try {
            // Mostrar un cuadro de diálogo para que el usuario escriba el texto
            JTextArea textoArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textoArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Escribir Texto", JOptionPane.PLAIN_MESSAGE);

            // Obtener el texto del JTextArea
            String texto = textoArea.getText();

            // Pedir al usuario la ubicación de la carpeta donde guardar el archivo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Archivo de Texto");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int seleccion = fileChooser.showSaveDialog(this);

            // Si el usuario elige una carpeta y confirma, guardar el archivo de texto
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File carpeta = fileChooser.getSelectedFile();
                File archivo = new File(carpeta, "texto.txt");
                FileWriter escritor = new FileWriter(archivo);
                escritor.write(texto);
                escritor.close();
                JOptionPane.showMessageDialog(this, "Texto guardado correctamente en " + archivo.getAbsolutePath(),
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo de texto.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrardatos() {
        try {
            // Seleccionar el archivo guardado
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Archivo de Texto");
            int seleccion = fileChooser.showOpenDialog(this);
    
            // Si el usuario selecciona un archivo y confirma
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
    
                // Leer el contenido del archivo
                FileReader lector = new FileReader(archivo);
                BufferedReader bufferedReader = new BufferedReader(lector);
                StringBuilder contenido = new StringBuilder();
                String linea;
                while ((linea = bufferedReader.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }
                bufferedReader.close();
    
                // Mostrar el contenido del archivo en un cuadro de diálogo
                JTextArea areaTexto = new JTextArea(contenido.toString());
                areaTexto.setFont(new Font("Arial", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(areaTexto);
                scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "Contenido del Archivo", JOptionPane.INFORMATION_MESSAGE);
    
                // Mostrar la ubicación del archivo en un mensaje
                JOptionPane.showMessageDialog(this, "El archivo se encuentra en: " + archivo.getAbsolutePath(), "Ubicación del Archivo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de texto.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Pasamos a diseñar nuestro tamaño y dónde se ejecutará nuestro programa
        BuscadorArchivos ventana = new BuscadorArchivos();
        ventana.setTitle("Llamada de sistemas");
        ventana.setBounds(0, 0, 615, 375); // El tamaño de nuestra interfaz
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Para cerrar la ventana al salir
        ventana.setVisible(true);
        ventana.setResizable(false);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
    }
}