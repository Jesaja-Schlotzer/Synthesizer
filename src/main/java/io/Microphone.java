package io;

import audio.components.Generator;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;

public class Microphone extends Generator {


    byte[] microphoneBuffer; // TODO oder so

    TargetDataLine targetDataLine;
    AudioInputStream recordStream;

    /**
     * Standard constructor, with Mono AudioFormat
     */
    public Microphone() throws LineUnavailableException {
        this(new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100,
                8,
                1,
                1,
                44100,
                false));
    }

    public Microphone(AudioFormat audioFormat) throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        recordStream = new AudioInputStream(targetDataLine);

        targetDataLine.open();

        microphoneBuffer = new byte[audioFormat.getFrameSize()];
    }


    public void connectMicrophone() {
        targetDataLine.start();
    }

    public void startListening() {
        targetDataLine.start();
    }

    public void stopListening() {
        targetDataLine.stop();
    }

    public void disconnectMicrophone() {
        targetDataLine.close();
    }



    private int off;

    @Override
    public double next() {
        try {
            off++;
            if(off >= microphoneBuffer.length) {
                off = 0;
                recordStream.read(microphoneBuffer);
            }
            System.out.println(microphoneBuffer[off]);
            return microphoneBuffer[off];
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }



    @Override
    public String toString() {
        return "Microphone[TargetDataLine=" + targetDataLine.toString() + ", recordStream=" + recordStream.toString() + "]";
    }

    @Override
    public Object clone() {
        try {
            return new Microphone(targetDataLine.getFormat());
        } catch (LineUnavailableException e) {
            return null;
        }
    }
}
