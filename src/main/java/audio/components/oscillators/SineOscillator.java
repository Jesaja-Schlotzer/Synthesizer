package audio.components.oscillators;

import audio.components.Generator;
import audio.interfaces.ModulationInterface;

public class SineOscillator extends Oscillator {


    public SineOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double sampleRate) {
        super(frequencyMod, amplitudeMod, sampleRate);
    }



    public double next() {
        double value = Math.sin(t);
        t += (2 * Math.PI * frequencyMod.get()) / sampleRate;
        return value * amplitudeMod.get();
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SineOscillator osc) {
            return osc.frequencyMod == this.frequencyMod &&
                    osc.amplitudeMod == this.amplitudeMod &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }

    @Override
    public Generator clone() {
        return new SineOscillator(frequencyMod, amplitudeMod, sampleRate);
    }



    @Override
    public String toString() {
        return "Oscillator[Type=Sine, Frequency="+ frequencyMod.get()
                +", Amplitude="+ amplitudeMod.get()
                +", SampleRate="+ sampleRate +"]";
    }
}
