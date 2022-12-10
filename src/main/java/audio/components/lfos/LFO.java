package audio.components.lfos;

import audio.components.Generator;
import audio.components.oscillators.Oscillator;

public abstract class LFO extends Generator{

    private final Oscillator sampleSource;

    LFO(Oscillator sampleSource) {
        this.sampleSource = sampleSource;
    }

    @Override
    protected double next() {
        return Math.min(Math.max(sampleSource.getOutputPort().out() + 0.5, 0), 1);
    }

    @Override
    public String toString() {
        return "LFO{" +
                "sampleSource=" + sampleSource +
                '}';
    }
}
