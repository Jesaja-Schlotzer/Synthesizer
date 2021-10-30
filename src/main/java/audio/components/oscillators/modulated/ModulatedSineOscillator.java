package audio.components.oscillators.modulated;

import audio.interfaces.ModulationInterface;

public class ModulatedSineOscillator extends ModulatedOscillator {


    public ModulatedSineOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double phase, double sampleRate) {
        super(frequencyMod, amplitudeMod, phase, sampleRate);
    }



    public double next() {
        double value = Math.sin(t + phase);
        t += (2 * Math.PI * frequencyMod.get()) / sampleRate;
        return value * amplitudeMod.get();
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModulatedSineOscillator osc) {
            return osc.frequencyMod == this.frequencyMod &&
                    osc.amplitudeMod == this.amplitudeMod &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }

    @Override
    public Object clone() {
        return new ModulatedSineOscillator(frequencyMod, amplitudeMod, phase, sampleRate);
    }

    @Override
    public String toString() {
        return "ModulatedOscillator[Type=Sine, Frequency="+ frequencyMod.get()
                +", Amplitude="+ amplitudeMod.get() +", Phase="+ phase
                +", SampleRate="+ sampleRate +"]";
    }
}
