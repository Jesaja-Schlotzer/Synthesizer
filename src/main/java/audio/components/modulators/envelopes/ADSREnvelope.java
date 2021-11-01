package audio.components.modulators.envelopes;

import audio.interfaces.ModulationInterface;
import audio.interfaces.Modulator;
import audio.modules.io.*;

public class ADSREnvelope extends Envelope implements Modulator{

    private ModulationInterface attack; // TODO ModTntf. zu Port wechseln Ports sind der heiße Sch. Ja ja und so weiter
    private ModulationInterface decay;
    private ModulationInterface sustain;
    private ModulationInterface release;


    private double lastTriggerSignal;


    public ADSREnvelope(double attack, double decay, double sustain, double release) {
        this(() -> Math.max(attack, 0), () -> Math.max(decay, 0), () -> Math.min(Math.max(sustain, 0), 1), () -> Math.max(release, 0));
    }


    public ADSREnvelope(ModulationInterface attack, ModulationInterface decay, ModulationInterface sustain, ModulationInterface release) {
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
        this.release = release;

        stage = STAGES.NONE;

        triggerInput = Port.NULL;
        mainInput = Port.NULL;

        mainOutput = this::modulate;
    }


//TODO release wenn decay evtl. broken



    @Override
    public double modulate() {
        if(lastTriggerSignal < triggerInput.out()) {
            press();
        }

        if(lastTriggerSignal > triggerInput.out()) {
            release();
        }

        lastTriggerSignal = triggerInput.out();

        switch (stage) {
            case ATTACK:
                value += 1 / this.attack.get();
                if(value >= 1) {
                    stage = STAGES.DECAY;
                }
                return value  * mainInput.out();

            case DECAY:
                value -= (1-sustain.get()) / decay.get();
                if(value <= sustain.get()) {
                    stage = STAGES.SUSTAIN;
                    value = sustain.get();
                }
                return value  * mainInput.out();

            case SUSTAIN:
                return sustain.get() * mainInput.out();

            case RELEASE:
                value -= sustain.get() / release.get();
                if(value <= 0) {
                    stage = STAGES.NONE;
                }
                return value  * mainInput.out();

            default: // case NONE
                return 0;
        }
    }



    @Override
    public void press() {
        if(attack.get() > 0) {
            stage = STAGES.ATTACK;
            value = 0;
        }else {
            value = 1;
            if(decay.get() > 0) {
                stage = STAGES.DECAY;
            }else {
                value = sustain.get();
                stage = STAGES.SUSTAIN;
            }
        }
    }


    @Override
    public void release() {
        if(stage == STAGES.NONE) {
            return;
        }
        stage = STAGES.RELEASE;
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
