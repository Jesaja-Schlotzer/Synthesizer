package audio.components.modulators.envelopes;

import audio.modules.io.*;
import io.interfaces.Pressable;

public abstract class Envelope implements Pressable {

    protected enum STAGES {
        NONE, ATTACK, DECAY, SUSTAIN, RELEASE, HOLD, DELAY
    }


    @InputPort
    protected Port mainInputPort = Port.NULL;

    public void setMainInputPort(Port mainInputPort) {
        if(mainInputPort != null) {
            this.mainInputPort = mainInputPort;
        }
    }


    @InputPort
    protected Port triggerInputPort = Port.NULL;

    public void setTriggerInputPort(Port triggerInputPort) {
        if(triggerInputPort != null) {
            this.triggerInputPort = triggerInputPort;
        }
    }



    @OutputPort
    protected final Port mainOutputPort = this::modulate;

    public Port getMainOutputPort() {
        return mainOutputPort;
    }


    protected double value;
    protected STAGES stage;


    protected abstract double modulate();


    public abstract String toString();

    public abstract Envelope clone();
}
