package audio.components.oscillators;

import audio.components.Generator;
import audio.interfaces.ModulationInterface;

public class SquareOscillator extends Oscillator {

    private ModulationInterface pulseWidthMod = () -> 0.5;


    public SquareOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double phase, double sampleRate) {
        super(frequencyMod, amplitudeMod, phase, sampleRate);
    }

    public SquareOscillator(ModulationInterface frequencyMod, ModulationInterface amplitudeMod, double phase, double sampleRate, ModulationInterface pulseWidthMod) {
        super(frequencyMod, amplitudeMod, phase, sampleRate);

        this.pulseWidthMod = pulseWidthMod;
    }



    @Override
    public double next() {
        double value = Math.sin(t + phase);
        t += (2 * Math.PI * frequencyMod.get()) / sampleRate;
        if(value < pulseWidthMod.get()) {
            return -amplitudeMod.get();
        }else {
            return amplitudeMod.get();
        }
    }



    public double getThreshold() {
        return pulseWidthMod.get();
    }

    public void setThresholdMod(ModulationInterface thresholdMod) {
        this.pulseWidthMod = thresholdMod;
    }

    public ModulationInterface getThresholdMod() {
        return pulseWidthMod;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SquareOscillator osc) {
            return osc.frequencyMod == this.frequencyMod &&
                    osc.amplitudeMod == this.amplitudeMod &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate &&
                    osc.pulseWidthMod == this.pulseWidthMod;
        }
        return false;
    }

    @Override
    public Generator clone() {
        return new SquareOscillator(frequencyMod, amplitudeMod, phase, sampleRate, pulseWidthMod);
    }

    @Override
    public String toString() {
        return "Oscillator[Type=Square, Frequency="+ frequencyMod.get()
                +", Amplitude="+ amplitudeMod.get() +", Phase="+ phase
                +", SampleRate="+ sampleRate +", Threshold="+pulseWidthMod.get()+"]";
    }
}
