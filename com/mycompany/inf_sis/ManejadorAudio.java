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

    /* Formato de grabación: En esta variable se indica el formato que usaremos para abrir las líneas de
     * audio de entrada/salida. En este caso estamos usado los valores sugeridos para el formato WAV que
     * encontramos en: https://docs.fileformat.com/es/audio/wav/ */
    private static AudioFormat formatoGrabacion = new AudioFormat(44100, 16, 1, true, false);

    /* Información de la línea de datos: Este es un objeto que necesitamos para poder crear las líneas de 
     * audio más adelante, se crea usando el formato de grabación.*/
    private static DataLine.Info infoLinea = new DataLine.Info(TargetDataLine.class, formatoGrabacion);

    /* Archivo de gración: Este es el archivo que usaremos para grabar y reproducir el audio, en el caso
     * de nuestro programa solo manejamos un archivo a la vez (porqué si no tendríamos que haber 
     * implementado un administrador de archivos). */
    private static File archivo = new File("Grabación.wav");

    /* Líneas de audio: Aquí solo se hace la declaración, pero estas son las líneas de audio que usaremos
     * más adelante.*/
    private static TargetDataLine lineaGrabacion;  //Línea del microfonó principal del sistema
    private static SourceDataLine lineaReproduccion; //Línea del altavoz principal del sistema

    //M. INICIAR GRABACIÓN
    /* Este es el método que se ejecuta cuando le picamos al botón de "Grabar" en la llamada 2*/
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
                    //Abrimos la línea de grabación con el formato que especificamos anteriormente
                    lineaGrabacion.open(formatoGrabacion, lineaGrabacion.getBufferSize());
                    lineaGrabacion.start();

                    /* Creamos una transmición de audio a partir de la línea de grabación, este objeto
                    /* maneja el flujo de bytes directo desde el micrófono. */
                    AudioInputStream transmicionAudio = new AudioInputStream(lineaGrabacion);

                    //Usamos la transmición de audio para guardar la grabación en el archivo
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

    //M. DETENER GRABACIÓN
    /* Este es el método que se ejecuta cuando le picamos al botón de "Detener" en la llamada 2 */
    public static void detenerGrabacion() {
        /* Solo tenemos que cerrar la línea de grabacion. Cuando la transmición de audio se quede sin
         * bytes que mandar, el hilo de grabación se cierra solito. */
        lineaGrabacion.stop();
        lineaGrabacion.close();
        System.out.println("Grabación finalizada\n"); //"\n" es un salto de línea, osea como picar enter
    }

    //M. INICIAR REPRODUCCIÓN
    /* Este es el método que se ejecuta cuando le picamos al botón de "Reproducir" en la llamada 3 */
    public static void iniciarReproduccion() {
        AudioInputStream transmicionAudio; //Declaración de una transmición de audio
        //Bloque "try/catch" por si hay un problema al leer el archivo
        try {
            /* Creamos una transmición de audio a partir del archivo de grabación, este objeto almacenara
             * los bytes de audio que leemos desde el archivo. */
            transmicionAudio = AudioSystem.getAudioInputStream(archivo);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo");
            e.printStackTrace();
            return; //Si no se puede leer el archivo cancelamos la reproducción
        }
        
        /* Actualizamos el objeto con la información de la línea para indicarle que ahora usaremos una
         * línea de salida. Lo que cambia es que en el primer parametro ponemos "SourceDataLine". */
        infoLinea = new DataLine.Info(SourceDataLine.class, formatoGrabacion);
        //Bloque "try/catch" por si la línea se encuentra ocupada
        try {
            lineaReproduccion = (SourceDataLine)AudioSystem.getLine(infoLinea);
        } catch (LineUnavailableException e) {
            System.out.println("Linea no disponible");
            e.printStackTrace();
        }

        /* Creamos otra implementación anónima de la interfaz "Runnable" para poder procesar la
         * reproducción en un hilo separado. */
        Runnable hiloReproducción = new Runnable() {
            //Método sobrescrito "run" que se ejecutará automaticamente cuando lancemos el hilo
            @Override
            public void run() {
                System.out.println("Reproduciendo...");
                //Bloque "try/catch" por si hay un problema con la línea o al reproducir el audio
                try {
                    //Abrimos la línea de reproducción con el formato que especificamos anteriormente
                    lineaReproduccion.open(formatoGrabacion);
                    lineaReproduccion.start();

                    //Creamos un buffer de bytes para mandar los bytes del archivo en rondas
                    int tamañoBuffer = 4096; //Tamaño del buffer
                    byte[] bufferDeBytes = new byte[tamañoBuffer]; //Buffer como arreglo de bytes
                    int bytesLeidos = -1; //Contador de bytes leídos

                    //Cilco para mandar los bytes a la línea de reproducción
                    while ((bytesLeidos = transmicionAudio.read(bufferDeBytes)) != -1) {
                        //Usamos el método "write" de la línea de reproducción para mandar el buffer
                        lineaReproduccion.write(bufferDeBytes, 0, bytesLeidos);
                    }
                } catch (Exception e) {
                    System.out.println("Error al reproducir");
                    e.printStackTrace();
                }
            }
        };
        //Creamos un hilo con el objeto "Runnable" que acabamos de crear y lo lanzamos
        new Thread(hiloReproducción).start();
    }

    //M. DETENDER REPRODUCCIÓN
    /* Este es el método que se ejecuta cuando le picamos al botón de "Detener" en la llamada 3 */
    public static void detenerReproduccion() {
        /* Igual que con la grabación solo tenemos que cerrar la línea de reproducción y el hilo
         * se cierra solito. */
        lineaReproduccion.stop();
        lineaReproduccion.close();
        System.out.println("Reproducción interrumpida\n");
    }

    //M. IMPRIMIR INFORMACIÓN DE AUDIO
    /* No se utiliza en la aplicación pero aquí se puede obtener la información de los dispositivos de
     * entrada y salida de audio del sistema. */
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
