package io;

import audio.components.Generator;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * The <code>Microphone</code> class can capture a microphone input.
 */
public class Microphone extends Generator {


    private final byte[] microphoneBuffer;

    private final TargetDataLine targetDataLine;
    private final AudioInputStream recordStream;

    /**
     * Constructs an <code>Microphone</code> with a standard <code>AudioFormat</code>.
     * @throws LineUnavailableException if a matching line is not available due to resource restrictions
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

    /**
     * Constructs an <code>Microphone</code> with a custom <code>AudioFormat</code>.
     * @param audioFormat the <code>AudioFormat</code> the microphone data will be formatted with.
     * @throws LineUnavailableException if a matching line is not available due to resource restrictions
     */
    public Microphone(AudioFormat audioFormat) throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        recordStream = new AudioInputStream(targetDataLine);

        targetDataLine.open();

        microphoneBuffer = new byte[audioFormat.getFrameSize()];
    }


    /**
     * The <code>OutputPort</code> starts to provide an audio signal from the microphone.
     */
    public void startListening() {
        targetDataLine.start();
    }

    /**
     * The <code>OutputPort</code> stops to provide an audio signal from the microphone.
     */
    public void stopListening() {
        targetDataLine.stop();
    }

    /**
     * Closes the connection to the microphone.
     */
    public void disconnectMicrophone() {
        targetDataLine.close();
    }



    private int off;

    /**
     * @return the next piece of audio data from the microphone
     */
    @Override
    protected double next() {
        try {
            off++;
            if(off >= microphoneBuffer.length) {
                off = 0;
                recordStream.read(microphoneBuffer);
            }
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
