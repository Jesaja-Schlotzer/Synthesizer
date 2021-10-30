package audio.components.oscillators.modulated;

import audio.interfaces.ModulationInterface;

public class ModulatedTriangleOscillator extends ModulatedOscillator {

    private double period;

    public ModulatedTriangleOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double phase, double sampleRate) {
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
        if (obj instanceof ModulatedTriangleOscillator osc) {
            return osc.frequencyMod == this.frequencyMod &&
                    osc.amplitudeMod == this.amplitudeMod &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate &&
                    osc.period == this.period;
        }
        return false;
    }

    @Override
    public Object clone() {
        return new ModulatedTriangleOscillator(frequencyMod, amplitudeMod, phase, sampleRate);
    }

    @Override
    public String toString() {
        return "ModulatedOscillator[Type=Triangle, Frequency="+ frequencyMod.get()
                +", Amplitude="+ amplitudeMod.get() +", Phase="+ phase
                +", SampleRate="+ sampleRate +"]";
    }
}
