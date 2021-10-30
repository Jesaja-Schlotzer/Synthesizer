package audio.components.oscillators.modulated;

import audio.interfaces.ModulationInterface;

public class ModulatedSquareOscillator extends ModulatedOscillator {

    private ModulationInterface thresholdMod = () -> 0.5;


    public ModulatedSquareOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double phase, double sampleRate) {
        super(frequencyMod, amplitudeMod, phase, sampleRate);
    }

    public ModulatedSquareOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double phase, double sampleRate, ModulationInterface thresholdMod) {
        super(frequencyMod, amplitudeMod, phase, sampleRate);

        this.thresholdMod = thresholdMod;
    }



    @Override
    public double next() {
        double value = Math.sin(t + phase);
        t += (2 * Math.PI * frequencyMod.get()) / sampleRate;
        if(value < thresholdMod.get()) {
            return -amplitudeMod.get();
        }else {
            return amplitudeMod.get();
        }
    }



    public double getThreshold() {
        return thresholdMod.get();
    }

    public void setThresholdMod(ModulationInterface thresholdMod) {
        this.thresholdMod = thresholdMod;
    }

    public ModulationInterface getThresholdMod() {
        return thresholdMod;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModulatedSquareOscillator osc) {
            return osc.frequencyMod == this.frequencyMod &&
                    osc.amplitudeMod == this.amplitudeMod &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate &&
                    osc.thresholdMod == this.thresholdMod;
        }
        return false;
    }

    @Override
    public Object clone() {
        return new ModulatedSquareOscillator(frequencyMod, amplitudeMod, phase, sampleRate, thresholdMod);
    }

    @Override
    public String toString() {
        return "ModulatedOscillator[Type=Square, Frequency="+ frequencyMod.get()
                +", Amplitude="+ amplitudeMod.get() +", Phase="+ phase
                +", SampleRate="+ sampleRate +", Threshold="+thresholdMod.get()+"]";
    }
}
