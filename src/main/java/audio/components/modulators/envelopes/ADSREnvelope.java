package audio.components.modulators.envelopes;

import audio.interfaces.ModulationInterface;
import audio.interfaces.Modulator;
import io.interfaces.Pressable;

public class ADSREnvelope implements Modulator, Pressable {

    private static final int NONE = -1;
    private static final int ATTACK = 0;
    private static final int DECAY = 1;
    private static final int SUSTAIN = 2;
    private static final int RELEASE = 3;


    private double attack;
    private double decay;
    private double sustain;
    private double release;

    private double attackSlope;
    private double decaySlope;
    private double releaseSlope;
    private double value;

    private int state;


    public ADSREnvelope(double attack, double decay, double sustain, double release) {
        this.attack = attack;
        this.decay = decay;
        this.sustain = Math.min(Math.max(sustain, 0), 1);
        this.release = release;

        attackSlope = 1 / attack;
        decaySlope = -((1-sustain) / decay);
        releaseSlope = -(sustain / release);

        state = NONE;
    }


    // TODO TODO TODO TODO !!! countedSamples von "ausen" geziehen (vielleicht als parameter durchreichen) so könnte Env an vielen Stellen gleichzeitig verwendet werden"


    @Override
    public ModulationInterface asInterface(double in) {
        return () -> this.modulate(in);
    }



    @Override
    public double modulate(double in) {
        switch (state) {
            case ATTACK:
                value += attackSlope;
                if(value >= 1) {
                    state = DECAY;
                }
                return value * in;

            case DECAY:
                value += decaySlope;
                if(value <= sustain) {
                    state = SUSTAIN;
                }
                return value * in;

            case SUSTAIN:
                return sustain * in;

            case RELEASE:
                value += releaseSlope;
                if(value <= 0) {
                    state = NONE;
                }
                return value * in;

            default: // case NONE
                return 0;
        }
    }


    @Override
    public void press() {
        state = ATTACK;
    }

    @Override
    public void release() {
        if(state == NONE) {
            return;
        }
        state = RELEASE;
    }
}
