package audio.components.oscillators;

import audio.components.Generator;
import audio.modules.io.*;

public abstract class Oscillator extends Generator {

    @InputPort
    protected Port frequencyInputPort = Port.NULL;

    public void setFrequencyInputPort(Port frequencyInputPort) {
        if(frequencyInputPort != null) {
            this.frequencyInputPort = frequencyInputPort;
        }
    }


    @InputPort
    protected Port amplitudeInputPort = Port.NULL;

    public void setAmplitudeInputPort(Port amplitudeInputPort) {
        if(amplitudeInputPort != null) {
            this.amplitudeInputPort = amplitudeInputPort;
        }
    }



    @OutputPort
    protected final Port mainOutput = this::next;

    public Port getMainOutput() {
        return mainOutput;
    }



    protected double sampleRate;
    protected double t = 0;


    public Oscillator(Port frequencyInputPort, Port amplitudeInputPort, double sampleRate) {
        setFrequencyInputPort(frequencyInputPort);
        setAmplitudeInputPort(amplitudeInputPort);
        this.sampleRate = sampleRate;
    }


    public Oscillator(double frequency, double amplitude, double sampleRate) {
        frequencyInputPort = (frequency >= 0 ? () -> frequency : Port.NULL);
        amplitudeInputPort = (amplitude >= 0 ? () -> amplitude : Port.NULL);
        this.sampleRate = sampleRate;
    }



    public void reset() {
        t = 0;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }
}
