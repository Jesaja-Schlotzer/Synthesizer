package audio.components.modulators.lfos;

import audio.components.oscillators.OldOscillator;
import audio.interfaces.ModulationInterface;
import audio.interfaces.Modulator;

public class LFO implements Modulator {

    private OldOscillator oscillator;

    public LFO(OldOscillator oscillator) {
        this.oscillator = oscillator;
    }


    public OldOscillator getOscillator() {
        return oscillator;
    }

    @Override
    public double modulate() {
        return oscillator.next();
    }

}
