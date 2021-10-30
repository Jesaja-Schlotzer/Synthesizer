package audio.components.oscillators;

import audio.components.Generator;

public abstract class Oscillator extends Generator {

    protected double frequency;
    protected double amplitude;
    protected double phase;
    protected double sampleRate;


    public Oscillator(double frequency, double amplitude, double phase, double sampleRate) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;
        this.sampleRate = sampleRate;

        this.t = 0;
    }


    public double getFrequency() {
        return frequency;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public double getPhase() {
        return phase;
    }

    public double getSampleRate() {
        return sampleRate;
    }


    public abstract boolean equals(Object obj);
}
