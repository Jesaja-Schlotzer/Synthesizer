package io;

import audio.components.oscillators.Oscillator;

import java.io.IOException;
import java.io.InputStream;


public class GeneratorInputStream extends InputStream {

    Oscillator oscillator;

    public GeneratorInputStream(Oscillator oscillator) {
        this.oscillator = oscillator;
    }


    @Override
    public int read() {
        return (int) oscillator.next();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b,0,b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        byte[] readBytes = new byte[len];
        for (int i = 0; i < readBytes.length; i++) {
            readBytes[i] = (byte) read();
        }

        System.arraycopy(readBytes, 0, b, off, len);

        return len;
    }


    @Override
    public long skip(long n) throws IOException {
        for (long i = 0; i < n; i++) {
            oscillator.next();
        }
        return n;
    }


    @Override
    public int available() throws IOException {
        return Integer.MAX_VALUE;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }


    Oscillator markOscillator;

    @Override
    public synchronized void mark(int readlimit) {
        try {
            markOscillator = (Oscillator) oscillator.clone();
            markOscillator.setI(oscillator.getI());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        oscillator = markOscillator;
        markOscillator = null;
    }

    @Override
    public boolean markSupported() {
        return true;
    }
}
