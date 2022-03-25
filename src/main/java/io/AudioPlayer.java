package io;

import audio.modules.io.InputPort;
import audio.modules.io.Port;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * The <code>AudioPlayer</code> passes an incoming signal to the PC speaker.
 */
public class AudioPlayer {


    public static final int BUFFER_SIZE = 1024;


    @InputPort
    private Port inputPort;

    /**
     * @param inputPort the signal to send to the speaker
     */
    public void setInputPort(Port inputPort) {
        if (inputPort != null) {
            this.inputPort = inputPort;
        }
    }


    private AudioFormat audioFormat;
    private InputStream audioInputStream;
    private SourceDataLine sourceDataLine;

    private Thread playerThread;


    /**
     * Constructs an <code>AudioPlayer</code> with a standard <code>AudioFormat</code> and takes in an <code>InputPort</code>.
     * @param inputPort the signal to send to the speaker
     */
    public AudioPlayer(Port inputPort) {
        setInputPort(inputPort);

        audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100,
                8,
                1,
                1,
                44100,
                false);
    }

    /**
     * Constructs an <code>AudioPlayer</code> with a custom <code>AudioFormat</code> and takes in an <code>InputPort</code>.
     * @param inputPort the signal to send to the speaker
     */
    public AudioPlayer(Port inputPort, AudioFormat audioFormat) {
        setInputPort(inputPort);

        this.audioFormat = audioFormat;
    }


    /**
     * Initializes the <code>AudioPlayer</code>.
     */
    public void init() {
        try {
            audioInputStream = new PortInputStream(inputPort);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);


            playerThread = new Thread(() -> {
                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while (!playerThread.isInterrupted()) {

                    try {
                        bytesRead = audioInputStream.read(buffer);
                    } catch (IOException e) {
                        System.err.println("Failed to read from AudioInputStream. Stopping player.");
                        break;
                    }

                    sourceDataLine.write(buffer, 0, bytesRead);
                }

                sourceDataLine.close();

                try {
                    audioInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    /**
     * Starts the <code>AudioPlayer</code> to actually send audio to the speaker.
     */
    public void start() {
        try {
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            playerThread.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    /**
     * Stops the <code>AudioPlayer</code> from sending audio to a speaker.
     * Needs to be initialized again afterwards to start the <code>AudioPlayer</code> again.
     */
    public void stop() {
        playerThread.interrupt();
    }
}
