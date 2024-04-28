package com.mycompany.inf_sis;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFileFormat.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        for (Type type : AudioSystem.getAudioFileTypes()) {
            System.out.println(type.toString());
        }
        System.out.println("------------------------------------------------");

        AudioFormat formatoGrabacion = new AudioFormat(44100, 16, 1, true, false);
        DataLine.Info infoLinea = new DataLine.Info(TargetDataLine.class, formatoGrabacion);
        if (!AudioSystem.isLineSupported(infoLinea)) {
            System.out.println("Line matching " + infoLinea + " not supported.");
        }

        Scanner entrada = new Scanner(System.in);

        TargetDataLine lineaGrabacion = (TargetDataLine)AudioSystem.getLine(infoLinea);
        System.out.println("Presione enter para empezar a grabar");
        entrada.nextLine();
        
        Runnable hiloGrabacion = new Runnable() {
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

        Thread.sleep(200);
        System.out.println("Presione enter para terminar de grabar");
        entrada.nextLine();
        lineaGrabacion.stop();
        lineaGrabacion.close();
        System.out.println("Grabación finalizada.\n");

        File archivo = new File("Grabación.wav");
        AudioInputStream transmicionAudio = AudioSystem.getAudioInputStream(archivo);
        System.out.println("Presione enter para reproducir");
        entrada.nextLine();

        infoLinea = new DataLine.Info(SourceDataLine.class, formatoGrabacion);
        SourceDataLine lineaReproduccion = (SourceDataLine)AudioSystem.getLine(infoLinea);
        lineaReproduccion.open(formatoGrabacion);
        lineaReproduccion.start();
        int tamañoBuffer = 4096;
        byte[] bufferDeBytes = new byte[tamañoBuffer];
        int bytesLeidos = -1;
        while ((bytesLeidos = transmicionAudio.read(bufferDeBytes)) != -1) {
            lineaReproduccion.write(bufferDeBytes, 0, bytesLeidos);
        }
        
        /* Mixer.Info[] infoMixers = AudioSystem.getMixerInfo();

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
        } */
    }
}
