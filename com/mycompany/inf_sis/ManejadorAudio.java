package com.mycompany.inf_sis;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFileFormat.Type;

import java.io.File;
//import java.util.Scanner;

public class ManejadorAudio {
    private static AudioFormat formatoGrabacion = new AudioFormat(44100, 16, 1, true, false);
    private static DataLine.Info infoLinea = new DataLine.Info(TargetDataLine.class, formatoGrabacion);
    //private static Scanner entrada = new Scanner(System.in);
    private static File archivo = new File("Grabación.wav");
    private static TargetDataLine lineaGrabacion;
    private static SourceDataLine lineaReproduccion;

    public static void iniciarGrabacion() {
        // System.out.println("Presione enter para empezar a grabar");
        // entrada.nextLine();
        try {
            lineaGrabacion = (TargetDataLine)AudioSystem.getLine(infoLinea);
        } catch (LineUnavailableException e) {
            System.out.println("Linea no disponible");
            e.printStackTrace();
            return;
        }

        Runnable hiloGrabacion = new Runnable() {
            @Override
            public void run() {
                System.out.println("Grabando...");
                try {
                    lineaGrabacion.open(formatoGrabacion, lineaGrabacion.getBufferSize());
                    lineaGrabacion.start();
                    AudioInputStream transmicionAudio = new AudioInputStream(lineaGrabacion);
                    AudioSystem.write(transmicionAudio, Type.WAVE, new File("Grabación.wav"));
                } catch (Exception e) {
                    System.out.println("Error al grabar");
                    e.printStackTrace();
                }
            }
        };
        new Thread(hiloGrabacion).start();
        //Thread.sleep(200);
    }
    
    public static void detenerGrabacion() {
        //System.out.println("Presione enter para terminar de grabar");
        //entrada.nextLine();
        lineaGrabacion.stop();
        lineaGrabacion.close();
        System.out.println("Grabación finalizada\n");
    }

    public static void iniciarReproduccion() {
        AudioInputStream transmicionAudio;
        try {
            transmicionAudio = AudioSystem.getAudioInputStream(archivo);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo");
            e.printStackTrace();
            return;
        }
        //System.out.println("Presione enter para reproducir");
        //entrada.nextLine();
        
        infoLinea = new DataLine.Info(SourceDataLine.class, formatoGrabacion);
        try {
            lineaReproduccion = (SourceDataLine)AudioSystem.getLine(infoLinea);
        } catch (LineUnavailableException e) {
            System.out.println("Linea no disponible");
            e.printStackTrace();
        }
        
        Runnable hiloReproducción = new Runnable() {
            @Override
            public void run() {
                System.out.println("Reproduciendo...");
                try {
                    lineaReproduccion.open(formatoGrabacion);
                    lineaReproduccion.start();
                    int tamañoBuffer = 4096;
                    byte[] bufferDeBytes = new byte[tamañoBuffer];
                    int bytesLeidos = -1;
                    while ((bytesLeidos = transmicionAudio.read(bufferDeBytes)) != -1) {
                        lineaReproduccion.write(bufferDeBytes, 0, bytesLeidos);
                    }
                } catch (Exception e) {
                    System.out.println("Error al reproducir");
                    e.printStackTrace();
                }
            }
        };
        new Thread(hiloReproducción).start();
    }

    public static void detenerReproduccion() {
        lineaReproduccion.stop();
        lineaReproduccion.close();
        System.out.println("Reproducción interrumpida\n");
    }
    
    public void imprimirInformacionAudio() {
        for (Type type : AudioSystem.getAudioFileTypes()) {
            System.out.println(type.toString());
        }
        System.out.println("------------------------------------------------");
    
        Mixer.Info[] infoMixers = AudioSystem.getMixerInfo();
        for (Mixer.Info infoMixer : infoMixers) {
            // Get mixer for each info
            Mixer mixer = AudioSystem.getMixer(infoMixer);
            String mixerName = infoMixer.getName();
            System.out.println("MIXER NAME: " + mixerName);
            System.out.println("MIXER : " + infoMixer.toString());
            System.out.println("MIXER DESC: " + infoMixer.getDescription());
    
            // Check if is input device
            Line.Info[] lines = mixer.getTargetLineInfo();
            if (lines.length > 0) {
                for (Line.Info line : lines) {
                    if (line.getLineClass().equals(TargetDataLine.class)) {
                        System.out.println("LINE INFO: " + line.toString());
                    }
                }
            }
            // Check if is output device
            lines = mixer.getSourceLineInfo();
            if (lines.length > 0) {
                for (Line.Info line : lines) {
                    if (line.getLineClass().equals(SourceDataLine.class)) {
                        System.out.println("LINE INFO: " + line.toString());
                    }
                }
            }
            System.out.println("------------------------------------------------");
        }
    }
}
