package audio.components.oscillators;

import audio.components.Generator;

public class OldSawtoothOscillator extends OldOscillator {

    private double period;

    public OldSawtoothOscillator(double frequency, double amplitude, double phase, double sampleRate) {
        super(frequency, amplitude, phase, sampleRate);

        this.period = sampleRate / frequency;
        this.phase = ((phase + 90)/ 360) * period;
    }


    @Override
    public double next() {
        double div = (t + phase) / period;
        double val = 2 * (div - Math.floor(0.5 + div));
        t++;
        return val * amplitude;
    }




    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OldSquareOscillator osc) {
            return osc.frequency == this.frequency &&
                    osc.amplitude == this.amplitude &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }

    @Override
    public Generator clone() {
        return new OldSawtoothOscillator(frequency, amplitude, phase, sampleRate);
    }

    @Override
    public String toString() {
        return "Oscillator[Type=Sawtooth, Frequency="+ frequency
                +", Amplitude="+ amplitude +", Phase="+ phase
                +", SampleRate="+ sampleRate +"]";
    }
}
