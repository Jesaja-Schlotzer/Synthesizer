package audio.components.oscillators;

import audio.components.Generator;
import audio.interfaces.ModulationInterface;

public class SawtoothOscillator extends Oscillator {

    private double period;

    public SawtoothOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double sampleRate) {
        super(frequencyMod, amplitudeMod, sampleRate);
    }


    @Override
    public double next() {
        period = sampleRate / frequencyMod.get();

        double div = t / period;
        double val = 2 * (div - Math.floor(0.5 + div));
        t++;
        return val * amplitudeMod.get();
    }




    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SawtoothOscillator osc) {
            return osc.frequencyMod == this.frequencyMod &&
                    osc.amplitudeMod == this.amplitudeMod &&
                    osc.sampleRate == this.sampleRate &&
                    osc.period == this.period;
        }
        return false;
    }

    @Override
    public Generator clone(){
        return new SawtoothOscillator(frequencyMod, amplitudeMod, sampleRate);
    }



    @Override
    public String toString() {
        return "Oscillator[Type=Sawtooth, Frequency="+ frequencyMod.get()
                +", Amplitude="+ amplitudeMod.get()
                +", SampleRate="+ sampleRate +"]";
    }
}
