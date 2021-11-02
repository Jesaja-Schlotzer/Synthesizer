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
    private Port mainInput;

    public void setMainInput(Port mainInput) {
        this.mainInput = mainInput;
    }


    @OutputPort
    private final Port mainOutput;

    public Port getMainOutput() {
        return mainOutput;
    }



    public BasicADSRModule() {
        attackKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        decayKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        sustainKnob = new ControlKnob(ControlKnob.ZERO_TO_ONE);
        releaseKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);

        adsrEnvelope = new ADSREnvelope(attackKnob::getValue, decayKnob::getValue, sustainKnob::getValue, releaseKnob::getValue);

        mainOutput = adsrEnvelope.getMainOutputPort();
    }


    public void setTriggerInput(Port triggerInput) {
        adsrEnvelope.setTriggerInputPort(triggerInput);
    }
}
