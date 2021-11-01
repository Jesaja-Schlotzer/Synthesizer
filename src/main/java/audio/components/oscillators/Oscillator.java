package audio.components.oscillators;

import audio.components.Generator;
import audio.interfaces.ModulationInterface;

public abstract class Oscillator extends Generator {

    protected ModulationInterface frequencyMod; // TODO ModInterface weg damit, Port ist der neue Scheiss
    protected ModulationInterface amplitudeMod;
    protected double sampleRate;
    protected double t = 0;

    public Oscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double sampleRate) {
        this.frequencyMod = frequencyMod;
        this.amplitudeMod = amplitudeMod;
        this.sampleRate = sampleRate;

        this.t = 0;
    }



    public void reset() {
        t = 0;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
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

    public double getSampleRate() {
        return sampleRate;
    }
}
