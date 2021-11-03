package io;

import audio.modules.io.InputPort;
import audio.modules.io.Port;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer {


    public static final int BUFFER_SIZE = 1024;


    @InputPort
    private Port inputPort;

    public void setInputPort(Port inputPort) {
        if (inputPort != null) {
            this.inputPort = inputPort;
        }
    }


    private AudioFormat audioFormat;
    private InputStream audioInputStream;
    private SourceDataLine sourceDataLine;

    private Thread playerThread;


    private int sampleRate;



    public AudioPlayer(Port inputPort) {
        setInputPort(inputPort);

        sampleRate = 44100; // TODO später dynamisch (mit SampleRate enum) (genauso wie audioformat evtl dyn)

        audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                8,
                1,
                1,
                sampleRate,
                false);
    }


    public void init() {
        try {
            audioInputStream = new PortInputStream(inputPort);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);


            playerThread = new Thread(() -> {
                int bytesRead = 0;
                byte[] buffer = new byte[BUFFER_SIZE];
                while (!playerThread.isInterrupted()) {

                    try {
                        bytesRead = audioInputStream.read(buffer);
                    } catch (IOException e) {
                        System.out.println("Failed to read from AudioInputStream. Stopping player.");
                        break;
                    }

                    sourceDataLine.write(buffer, 0, bytesRead);
                }

            });

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        try {
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            playerThread.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }



    public void stop() {
        try {
            playerThread.interrupt();
            sourceDataLine.close();
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
