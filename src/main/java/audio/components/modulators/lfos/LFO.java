package audio.components.modulators.lfos;

import audio.components.oscillators.Oscillator;
import audio.interfaces.ModulationInterface;
import audio.interfaces.Modulator;

public class LFO implements Modulator {

    private Oscillator oscillator;

    public LFO(Oscillator oscillator) {
        this.oscillator = oscillator;
    }


    public Oscillator getOscillator() {
        return oscillator;
    }

    @Override
    public double modulate(double in) {
        return oscillator.next() * in;
    }

    @Override
    public ModulationInterface asInterface(double in) {
        return () -> modulate(in);
    }
}
