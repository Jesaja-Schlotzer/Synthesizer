package audio.components.oscillators;

import audio.components.Generator;
import audio.interfaces.ModulationInterface;

public class TriangleOscillator extends Oscillator {

    private double period;

    public TriangleOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double sampleRate) {
        super(frequencyMod, amplitudeMod, sampleRate);
    }


    @Override
    public double next() {
        period = sampleRate / frequencyMod.get();

        double div = t / period;
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
                    osc.sampleRate == this.sampleRate &&
                    osc.period == this.period;
        }
        return false;
    }

    @Override
    public Generator clone() {
        return new TriangleOscillator(frequencyMod, amplitudeMod, sampleRate);
    }


    @Override
    public String toString() {
        return "Oscillator[Type=Triangle, Frequency="+ frequencyMod.get()
                +", Amplitude="+ amplitudeMod.get()
                +", SampleRate="+ sampleRate +"]";
    }
}
