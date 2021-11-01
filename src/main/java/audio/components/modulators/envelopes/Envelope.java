package audio.components.modulators.envelopes;

import audio.interfaces.Modulator;
import audio.modules.io.InputPort;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;
import io.interfaces.Pressable;

public abstract class Envelope implements Modulator, Pressable {

    enum STAGES {
        NONE, ATTACK, DECAY, SUSTAIN, RELEASE, HOLD, DELAY
    }


    @InputPort
    protected Port mainInput;

    public void setMainInput(Port mainInput) {
        this.mainInput = mainInput;
    }


    @InputPort
    protected Port triggerInput;

    public void setTriggerInput(Port triggerInput) {
        this.triggerInput = triggerInput;
    }



    @OutputPort
    protected Port mainOutput;

    public Port getMainOutput() {
        return mainOutput;
    }


    protected double value;
    protected STAGES stage;


    public abstract String toString();

    public abstract Envelope clone();
}
