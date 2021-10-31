package audio.components.oscillators;

import audio.components.Generator;

public class SineOscillator extends Oscillator{

    private double step;

    public SineOscillator(double frequency, double amplitude, double phase, double sampleRate) {
        super(frequency, amplitude, phase, sampleRate);

        step = (2 * Math.PI * frequency) / sampleRate;
    }



    public double next() {
        double value = Math.sin(t + phase);
        t += step;
        return value * amplitude;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SineOscillator osc) {
            return osc.frequency == this.frequency &&
                    osc.amplitude == this.amplitude &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }

    @Override
    public Generator clone() {
        return new SineOscillator(frequency, amplitude, phase, sampleRate);
    }

    @Override
    public String toString() {
        return "Oscillator[Type=Sine, Frequency="+ frequency
                +", Amplitude="+ amplitude +", Phase="+ phase
                +", SampleRate="+ sampleRate +"]";
    }
}
