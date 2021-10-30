package io;

import audio.components.Generator;

import java.io.IOException;
import java.io.InputStream;


public class GeneratorInputStream extends InputStream {

    Generator generator;


    public GeneratorInputStream(Generator generator) {
        this.generator = generator;
    }


    @Override
    public int read() {
        return (int) generator.next();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b,0, b.length);
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
            generator.next();
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


    Generator markGenerator;

    @Override
    public synchronized void mark(int readlimit) {
        markGenerator = (Generator) markGenerator.clone();
        markGenerator.setT(markGenerator.getT());
    }

    @Override
    public synchronized void reset() throws IOException {
        generator = markGenerator;
        markGenerator = null;
    }

    @Override
    public boolean markSupported() {
        return true;
    }
}
