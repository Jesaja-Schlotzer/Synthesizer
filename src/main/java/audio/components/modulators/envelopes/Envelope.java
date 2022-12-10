package audio.components.modulators.envelopes;

import audio.modules.io.*;
import io.interfaces.Pressable;

/**
 * An abstract envelope class, providing an input and an output <code>Port</code> and an additional <code>triggerInputPort</code> that triggers the envelope.
 * Also defines the <code>protected enum STAGES</code> which provides all possible stages an envelope could be in.
 */
public abstract class Envelope implements Pressable {

    /**
     * Provides all possible stages an envelope could be in.
     */
    protected enum STAGES {
        OFF, ATTACK, DECAY, SUSTAIN, RELEASE, HOLD, DELAY
    }


    @InputPort
    protected Port mainInputPort = Port.NULL;

    /**
     * @param mainInputPort The signal that should be modulated
     */
    public void setMainInputPort(Port mainInputPort) {
        if(mainInputPort != null) {
            this.mainInputPort = mainInputPort;
        }
    }


    @InputPort
    protected Port triggerInputPort = Port.NULL;

    /**
     * @param triggerInputPort The <code>inputPort</code> that supplies a trigger signal
     */
    public void setTriggerInputPort(Port triggerInputPort) {
        if(triggerInputPort != null) {
            this.triggerInputPort = triggerInputPort;
        }
    }



    @OutputPort
    protected final Port outputPort = this::modulate;

    /**
     * @return the <code>outputPort</code> that supplies the modulated signal
     */
    public Port getOutputPort() {
        return outputPort;
    }


    protected double value;
    protected STAGES stage;

    /**
     * Modulates the incoming signal in relation to the current stage.
     * @return the modulated signal
     */
    protected abstract double modulate();


    public abstract String toString();

    public abstract Envelope clone();
}
