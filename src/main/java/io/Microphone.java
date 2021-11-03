package io;

import audio.components.Generator;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;


import javax.sound.sampled.*;
import java.io.IOException;

public class Microphone extends Generator {

    @OutputPort
    private final Port outputPort = this::next;

    public Port getOutputPort() {
        return outputPort;
    }


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
    protected double next() {
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
    public Generator clone() {
        try {
            return new Microphone(targetDataLine.getFormat());
        } catch (LineUnavailableException e) {
            return null;
        }
    }
}
