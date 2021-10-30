package io;

import audio.Synthesizer;
import audio.components.oscillators.Oscillator;
import audio.components.oscillators.SineOscillator;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer {


    public static final int BUFFER_SIZE = 1024;



    private Synthesizer synth;
    private AudioFormat audioFormat;
    private InputStream audioInputStream;
    private SourceDataLine sourceDataLine;


    private int sampleRate;



    public AudioPlayer(Synthesizer synth) {
        this.synth = synth;

        sampleRate = 44100; // TODO später von Synth lesen
/*
        audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                8,
                2,
                2,
                sampleRate,
                false);*/

        audioFormat = new AudioFormat(
                44100/2,
                8,
                2,
                true,
                false);
    }


    public void init() {
        try {
            audioInputStream = new GeneratorInputStream(synth);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        try {
            sourceDataLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void play(int seconds) {
        try {
            int bytesRead = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            sourceDataLine.start();

            int cycles = (sampleRate * 2/* <-- Stereo*/ * seconds) / BUFFER_SIZE;
            for (int i = 0; i < cycles; i++) {

                bytesRead = audioInputStream.read(buffer);
                // It is possible at this point manipulate the data in buffer[].
                // The write operation blocks while the system plays the sound.
                sourceDataLine.write(buffer, 0, bytesRead);
            }
            sourceDataLine.drain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        try {
            sourceDataLine.close();
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
