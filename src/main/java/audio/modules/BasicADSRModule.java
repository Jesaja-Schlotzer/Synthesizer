package audio.modules;

import audio.components.modulators.envelopes.ADSREnvelope;
import audio.modules.controls.ControlKnob;
import audio.modules.io.*;

public class BasicADSRModule {

    public final ControlKnob attackKnob;
    public final ControlKnob decayKnob;
    public final ControlKnob sustainKnob;
    public final ControlKnob releaseKnob;

    private final ADSREnvelope adsrEnvelope;


    @InputPort
    private Port mainInputPort = Port.NULL;

    public void setMainInputPort(Port mainInputPort) {
        if (mainInputPort != null) {
            this.mainInputPort = mainInputPort;
        }
    }


    @OutputPort
    private final Port mainOutputPort;

    public Port getMainOutputPort() {
        return mainOutputPort;
    }


    public BasicADSRModule() {
        attackKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        decayKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        sustainKnob = new ControlKnob(ControlKnob.ZERO_TO_ONE);
        releaseKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);

        adsrEnvelope = new ADSREnvelope(attackKnob::getValue, decayKnob::getValue, sustainKnob::getValue, releaseKnob::getValue);

        mainOutputPort = adsrEnvelope.getMainOutputPort();
    }


    public void setTriggerInputPort(Port triggerInputPort) {
        if (triggerInputPort != null) {
            adsrEnvelope.setTriggerInputPort(triggerInputPort);
        }
    }
}
