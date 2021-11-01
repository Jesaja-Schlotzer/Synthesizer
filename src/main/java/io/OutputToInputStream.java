package io;

import audio.modules.io.Port;

import java.io.IOException;
import java.io.InputStream;


public class OutputToInputStream extends InputStream {


    Port output;


    public OutputToInputStream(Port output) {
        this.output = output;
    }


    @Override
    public int read() {
        return (int) output.out();
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
            output.out();
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


    Port markOutput;

    @Override
    public synchronized void mark(int readlimit) {
        markOutput = output;
    }

    @Override
    public synchronized void reset() throws IOException {
        output = markOutput;
        markOutput = null;
    }

    @Override
    public boolean markSupported() {
        return true;
    }
}
