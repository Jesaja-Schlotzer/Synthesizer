package audio.components.oscillators;

import audio.components.Generator;
import audio.interfaces.ModulationInterface;

public class TriangleOscillator extends Oscillator {

    private double period;

    public TriangleOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double phase, double sampleRate) {
        super(frequencyMod, amplitudeMod, phase, sampleRate);
    }


    @Override
    public double next() {
        period = sampleRate / frequencyMod.get();
        phase = ((phase + 90)/ 360) * period;

        double div = (t + phase) / period;
        double value = 2 * (div - Math.floor(0.5 + div));
        value = (Math.abs(value) - 0.5) * 2;
        t++;
        return value * amplitudeMod.get();
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TriangleOscillator osc) {
            return osc.frequencyMod == this.frequencyMod &&
                    osc.amplitudeMod == this.amplitudeMod &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate &&
                    osc.period == this.period;
        }
        return false;
    }

    @Override
    public Generator clone() {
        return new TriangleOscillator(frequencyMod, amplitudeMod, phase, sampleRate);
    }

    @Override
    public String toString() {
        return "Oscillator[Type=Triangle, Frequency="+ frequencyMod.get()
                +", Amplitude="+ amplitudeMod.get() +", Phase="+ phase
                +", SampleRate="+ sampleRate +"]";
    }
}
