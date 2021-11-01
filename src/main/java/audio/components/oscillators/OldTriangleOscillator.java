package audio.components.oscillators;

import audio.components.Generator;

public class OldTriangleOscillator extends OldOscillator {

    private double period;

    public OldTriangleOscillator(double frequency, double amplitude, double phase, double sampleRate) {
        super(frequency, amplitude, phase, sampleRate);

        this.period = sampleRate / frequency;
        this.phase = ((phase + 90)/ 360) * period;
    }



    public double next() {
        double div = (t + phase) / period;
        double value = 2 * (div - Math.floor(0.5 + div));
        value = (Math.abs(value) - 0.5) * 2;
        t++;
        return value * amplitude;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OldSineOscillator osc) {
            return osc.frequency == this.frequency &&
                    osc.amplitude == this.amplitude &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }

    @Override
    public Generator clone() {
        return new OldSineOscillator(frequency, amplitude, phase, sampleRate);
    }

    @Override
    public String toString() {
        return "Oscillator[Type=Triangle, Frequency="+ frequency
                +", Amplitude="+ amplitude +", Phase="+ phase
                +", SampleRate="+ sampleRate +"]";
    }
}
