package io;

import audio.modules.io.InputPort;
import audio.modules.io.Port;

import java.io.IOException;
import java.io.InputStream;


public class PortInputStream extends InputStream {

    @InputPort
    private Port inputPort = Port.NULL;


    public void setInputPort(Port inputPort) {
        if (inputPort != null) {
            this.inputPort = inputPort;
        }
    }


    public PortInputStream(Port inputPort) {
        this.inputPort = inputPort;
    }


    @Override
    public int read() {
        return (int) inputPort.out();
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
            inputPort.out();
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


    // Mark etc. is completely broken and does not work as intended, but this should be fine, because it should not be used anyway.

    Port markOutput;

    @Override
    public synchronized void mark(int readlimit) {
        markOutput = inputPort;
    }

    @Override
    public synchronized void reset() throws IOException {
        inputPort = markOutput;
        markOutput = null;
    }

    @Override
    public boolean markSupported() {
        return true;
    }
}
