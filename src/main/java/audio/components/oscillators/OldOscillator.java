package audio.components.oscillators;

import audio.components.Generator;

public abstract class OldOscillator extends Generator {

    protected double frequency;
    protected double amplitude;
    protected double phase;
    protected double sampleRate;


    public OldOscillator(double frequency, double amplitude, double phase, double sampleRate) {
        this.frequency = Math.max(frequency, 0);
        this.amplitude = Math.max(amplitude, 0);
        this.phase = phase;
        this.sampleRate = Math.max(sampleRate, 0);

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
