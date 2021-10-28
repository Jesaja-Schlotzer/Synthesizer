package audio.components.oscillators;

import io.GeneratorInputStream;

public abstract class Oscillator {

    protected double i;

    protected double frequency;
    protected double amplitude;
    protected double phase;
    protected double sampleRate;

    public Oscillator(double frequency, double amplitude, double phase, double sampleRate) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;
        this.sampleRate = sampleRate;
    }


    public abstract double next();


    public GeneratorInputStream getGeneratorInputStream() {
        return new GeneratorInputStream(this);
    }


    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }



    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    @Override
    public abstract String toString();
}
