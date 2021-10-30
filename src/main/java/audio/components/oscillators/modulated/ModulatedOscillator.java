package audio.components.oscillators.modulated;

import audio.components.Generator;
import audio.interfaces.ModulationInterface;

public abstract class ModulatedOscillator extends Generator {

    protected ModulationInterface frequencyMod;
    protected ModulationInterface amplitudeMod;
    protected double phase; //TODO viellceiht so wie so unnötig / nur für LFOs wichtig (die evtl. eigene Klasse bekommen)
    protected double sampleRate;

    public ModulatedOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double phase, double sampleRate) {
        this.frequencyMod = frequencyMod;
        this.amplitudeMod = amplitudeMod;
        this.phase = phase;
        this.sampleRate = sampleRate;

        this.t = 0;
    }


    public ModulationInterface getFrequencyMod() {
        return frequencyMod;
    }

    public void setFrequencyMod(ModulationInterface frequencyMod) {
        this.frequencyMod = frequencyMod;
    }


    public ModulationInterface getAmplitudeMod() {
        return amplitudeMod;
    }

    public void setAmplitudeMod(ModulationInterface amplitudeMod) {
        this.amplitudeMod = amplitudeMod;
    }



    public double getFrequency() {
        return frequencyMod.get();
    }

    public double getAmplitude() {
        return amplitudeMod.get();
    }

    public double getPhase() {
        return phase;
    }

    public double getSampleRate() {
        return sampleRate;
    }
}
