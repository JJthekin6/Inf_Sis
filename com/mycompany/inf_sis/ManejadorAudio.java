package com.mycompany.inf_sis;

//Librerías importadas para el manejo de audio
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

//Librería importada para el manejo de archivos
import java.io.File;

//CLASE MANEJADOR DE AUDIO
/* Clase que utiliza las librerías de audio para acceder a los dispositivos de entrada/salida del sistema
 * para grabar y reproducir audio. */
public class ManejadorAudio {
    //VARIABLES ESTÁTICAS DE LA CLASE
    /* En realidad esta clase debió haber sido una interfaz porque nunca se instancía y todos sus
     * métodos son estáticos, but oh well... */

    /* Formato de grabación: En esta variable se indica el formato que usaremos para abrir las lineas de
     * audio de entrada/salida. En este caso estamos usado los valores sugeridos para el formato WAV que
     * encontramos en: https://docs.fileformat.com/es/audio/wav/ */
    private static AudioFormat formatoGrabacion = new AudioFormat(44100, 16, 1, true, false);

    /* Información de la linea de datos: Este es un objeto que necesitamos para poder crear las lineas de 
     * audio más adelante, se crea usando el formato de grabación.*/
    private static DataLine.Info infoLinea = new DataLine.Info(TargetDataLine.class, formatoGrabacion);

    /* Archivo de gración: Este es el archivo que usaremos para grabar y reproducir el audio, en el caso
     * de nuestro programa solo manejamos un archivo a la vez (porqué si no tendríamos que haber 
     * implementado un administrador de archivos). */
    private static File archivo = new File("Grabación.wav");

    /* Lineas de audio: Aquí solo se hace la declaración, pero estas son las lineas de audio que usaremos
     * más adelante.*/
    private static TargetDataLine lineaGrabacion;  //Linea del microfonó principal del sistema
    private static SourceDataLine lineaReproduccion; //Linea del altavoz principal del sistema

    //M. INICIAR GRABACIÓN
    /* Este es el método que se ejecuta cuando le picamos al botón de "grabar" en la llamada 2*/
    public static void iniciarGrabacion() {
        //Bloque "try/catch" por si la línea está ocupada
        try {
            /* Obtenemos la línea de grabación del micrófono del sistema usando la información de la linea
             * que definimos al principio */
            lineaGrabacion = (TargetDataLine)AudioSystem.getLine(infoLinea);
        } catch (LineUnavailableException e) {
            System.out.println("Linea no disponible");
            e.printStackTrace();
            return;
        }

        /* Creamos una implementación anónima de la interfaz "Runnable" para poder procesar la grabación
         * en un hilo separado al que maneja la interfaz. */
        Runnable hiloGrabacion = new Runnable() {
            //Método sobrescrito "run" que se ejecutará automaticamente cuando lancemos el hilo
            @Override
            public void run() {
                System.out.println("Grabando...");
                //Bloque "try/catch" por si hay un problema con la línea o al guardar el archivo
                try {
                    //Abrimos la linea de grabación con el formato que especificamos anteriormente
                    lineaGrabacion.open(formatoGrabacion, lineaGrabacion.getBufferSize());
                    lineaGrabacion.start();
                    /* Creamos una transmición de audio a partir de la línea de grabación, este objeto
                    /* maneja el flujo de bytes directo desde el micrófono */
                    AudioInputStream transmicionAudio = new AudioInputStream(lineaGrabacion);
                    /* Usamos la transmición de audio para guardar la grabación en el archivo */
                    AudioSystem.write(transmicionAudio, Type.WAVE, archivo);
                } catch (Exception e) {
                    System.out.println("Error al grabar");
                    e.printStackTrace();
                }
            }
        };
        //Creamos un hilo con el objeto "Runnable" que acabamos de crear y lo lanzamos
        new Thread(hiloGrabacion).start();
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
