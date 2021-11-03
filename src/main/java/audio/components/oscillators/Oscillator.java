package audio.components.oscillators;

import audio.components.Generator;
import audio.enums.SampleRate;
import audio.modules.io.*;

public abstract class Oscillator extends Generator {

    @InputPort
    protected Port frequencyInputPort = Port.NULL;

    public void setFrequencyInputPort(Port frequencyInputPort) {
        if(frequencyInputPort != null) {
            this.frequencyInputPort = () -> Math.max(frequencyInputPort.out(), 0);
        }
    }


    @InputPort
    protected Port amplitudeInputPort = Port.NULL;

    public void setAmplitudeInputPort(Port amplitudeInputPort) {
        if(amplitudeInputPort != null) {
            this.amplitudeInputPort = () -> Math.max(frequencyInputPort.out(), 0);
        }
    }



    @OutputPort
    protected final Port mainOutputPort = this::next;

    public Port getMainOutputPort() {
        return mainOutputPort;
    }



    protected double sampleRate;
    protected double t = 0;


    public Oscillator(Port frequencyInputPort, Port amplitudeInputPort, SampleRate sampleRate) {
        setFrequencyInputPort(frequencyInputPort);
        setAmplitudeInputPort(amplitudeInputPort);
        this.sampleRate = sampleRate.get();
    }


    public Oscillator(double frequency, double amplitude, SampleRate sampleRate) {
        frequencyInputPort = (frequency >= 0 ? () -> frequency : Port.NULL);
        amplitudeInputPort = (amplitude >= 0 ? () -> amplitude : Port.NULL);
        this.sampleRate = sampleRate.get();
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
