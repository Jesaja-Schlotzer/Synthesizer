package audio.components.modulators.envelopes;

import audio.interfaces.ModulationInterface;
import audio.interfaces.Modulator;
import io.interfaces.Pressable;

public class ADSREnvelope extends Envelope implements Modulator {


    private double attack;
    private double decay;
    private double sustain;
    private double release;

    private double attackSlope;
    private double decaySlope;
    private double releaseSlope;
    private double value;

    private STAGE state;


    public ADSREnvelope(double attack, double decay, double sustain, double release) {
        this.attack = Math.max(attack, 0);
        this.decay = Math.max(decay, 0);
        this.sustain = Math.min(Math.max(sustain, 0), 1);
        this.release = Math.max(release, 0);

        attackSlope = 1 / attack;
        decaySlope = -((1-sustain) / decay);
        releaseSlope = -(sustain / release);

        state = STAGE.NONE;
    }


    // TODO TODO TODO TODO !!! countedSamples von "ausen" geziehen (vielleicht als parameter durchreichen) so könnte Env an vielen Stellen gleichzeitig verwendet werden"
//TODO release wenn decay evtl. broken

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
                    state = STAGE.DECAY;
                }
                return value * in;

            case DECAY:
                value += decaySlope;
                if(value <= sustain) {
                    state = STAGE.SUSTAIN;
                    value = sustain;
                }
                return value * in;

            case SUSTAIN:
                return sustain * in;

            case RELEASE:
                value += releaseSlope;
                if(value <= 0) {
                    state = STAGE.NONE;
                }
                return value * in;

            default: // case NONE
                return 0;
        }
    }


    @Override
    public void press() {
        if(attack > 0) {
            state = STAGE.ATTACK;
            value = 0;
        }else {
            value = 1;
            if(decay > 0) {
                state = STAGE.DECAY;
            }else {
                value = sustain;
                state = STAGE.SUSTAIN;
            }
        }
    }

    @Override
    public void release() {
        if(state == STAGE.NONE) {
            return;
        }
        state = STAGE.RELEASE;
    }




    @Override
    public String toString() {
        return "Envelope[Type=ADSR, Attack="+attack+", Decay="+decay+", Sustain="+sustain+", Release="+release+"]";
    }

    @Override
    public Envelope clone() {
        return new ADSREnvelope(attack, decay, sustain, release);
    }
}
